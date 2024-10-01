package com.bff.wespot

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.bff.wespot.R.string
import com.bff.wespot.analytic.AnalyticsHelper
import com.bff.wespot.analytic.LocalAnalyticsHelper
import com.bff.wespot.designsystem.R
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.button.WSButtonType
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.entire.screen.destinations.SettingScreenDestination
import com.bff.wespot.model.ToastState
import com.bff.wespot.model.common.RestrictionType
import com.bff.wespot.model.notification.NotificationType
import com.bff.wespot.navigation.Navigator
import com.bff.wespot.navigation.util.EXTRA_TARGET_ID
import com.bff.wespot.navigation.util.EXTRA_TYPE
import com.bff.wespot.navigation.util.EXTRA_USER_ID
import com.bff.wespot.notification.screen.NotificationNavigator
import com.bff.wespot.state.MainAction
import com.bff.wespot.state.MainUiState
import com.bff.wespot.data.remote.extensions.toLocalDateFromDashPattern
import com.bff.wespot.navigation.util.EXTRA_DATE
import com.bff.wespot.ui.TopToast
import com.bff.wespot.ui.WSBottomSheet
import com.bff.wespot.util.clickableSingle
import com.bff.wespot.viewmodel.MainViewModel
import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    private val notificationPermissionLauncher by lazy {
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            viewModel.onAction(MainAction.OnNotificationSet(isGranted))
        }
    }

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermission()

        val navArgs = getMainScreenArgsFromIntent()
        checkEnteredFromPushNotification(navArgs)

        setContent {
            WeSpotTheme {
                MainScreen(
                    navigator = navigator,
                    navArgs = navArgs,
                    analyticsHelper = analyticsHelper,
                    viewModel = viewModel,
                )
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun checkEnteredFromPushNotification(data: MainScreenNavArgs) {
        if (data.type != NotificationType.IDLE) {
            viewModel.onAction(MainAction.OnEnteredByPushNotification(data))
        }
    }

    private fun getMainScreenArgsFromIntent(): MainScreenNavArgs = with(intent) {
        val targetId = getIntExtra(EXTRA_TARGET_ID, -1)
        val userId = getStringExtra(EXTRA_USER_ID).orEmpty()
        val type = NotificationType.convertNotificationType(getStringExtra(EXTRA_TYPE).orEmpty())
        val date = getStringExtra(EXTRA_DATE).orEmpty()

        removeExtra(EXTRA_TARGET_ID)
        removeExtra(EXTRA_USER_ID)
        removeExtra(EXTRA_TYPE)
        removeExtra(EXTRA_DATE)

        MainScreenNavArgs(
            targetId = targetId,
            userId = userId,
            type = type,
            date = date,
        )
    }
}

data class MainScreenNavArgs(
    val type: NotificationType,
    val userId: String,
    val targetId: Int,
    val date: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen(
    navigator: Navigator,
    navArgs: MainScreenNavArgs,
    analyticsHelper: AnalyticsHelper,
    viewModel: MainViewModel,
) {
    val state by viewModel.collectAsState()
    val action = viewModel::onAction

    val navController = rememberNavController()
    var toast by remember { mutableStateOf(ToastState()) }

    val isTopNavigationScreen by navController.checkCurrentScreen(NavigationBarPosition.TOP)
    val isBottomNavigationScreen by navController.checkCurrentScreen(NavigationBarPosition.BOTTOM)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AnimatedContent(
                targetState = isTopNavigationScreen,
                transitionSpec = {
                    fadeIn(animationSpec = tween()) togetherWith fadeOut(animationSpec = tween())
                },
                label = stringResource(string.bottom_bar_animated_content_label),
            ) { targetState ->
                if (targetState != BarType.NONE) {
                    WSTopBar(
                        title = "",
                        navigation = {
                            if (isTopNavigationScreen == BarType.DEFAULT) {
                                Image(
                                    modifier = Modifier
                                        .padding(top = 8.dp, bottom = 8.dp, start = 16.dp)
                                        .size(width = 112.dp, height = 44.dp),
                                    painter = painterResource(id = R.drawable.main_logo),
                                    contentDescription = stringResource(
                                        id = com.bff.wespot.message.R.string.wespot_logo,
                                    ),
                                )
                            }
                        },
                        action = {
                            if (isTopNavigationScreen == BarType.DEFAULT) {
                                IconButton(
                                    modifier = Modifier.padding(end = 8.dp),
                                    onClick = {
                                        navController.navigateToNavGraph(
                                            navGraph = AppNavGraphs.notification,
                                        )
                                    },
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.icn_alarm),
                                        contentDescription = stringResource(
                                            id = R.string.notification_icon,
                                        ),
                                    )
                                }
                            } else if (isTopNavigationScreen == BarType.ENTIRE) {
                                IconButton(
                                    modifier = Modifier.padding(end = 8.dp),
                                    onClick = {
                                        navController.navigate(
                                            SettingScreenDestination within AppNavGraphs.entire,
                                        )
                                    },
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.icn_settings),
                                        contentDescription = stringResource(
                                            id = R.string.setting_icon,
                                        ),
                                    )
                                }
                            }
                        },
                    )
                }
            }
        },
        bottomBar = {
            AnimatedContent(
                targetState = isBottomNavigationScreen,
                transitionSpec = {
                    fadeIn(animationSpec = tween()) togetherWith fadeOut(animationSpec = tween())
                },
                label = stringResource(string.top_bar_animated_content_label)
            ) { targetState ->
                if (targetState == BarType.DEFAULT) {
                    val currentSelectedItem by navController.currentScreenAsState()
                    BottomNavigationTab(
                        selectedNavigation = currentSelectedItem,
                        onNavigationSelected = { selected ->
                            navController.navigateToNavGraph(selected)
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        },
    ) {
        CompositionLocalProvider(
            LocalAnalyticsHelper provides analyticsHelper,
        ) {
            AppNavigation(
                navController = navController,
                modifier = Modifier.padding(it),
                navigator = navigator,
                showToast = { toastState -> toast = toastState },
                restricted = state.restriction.restrictionType != RestrictionType.NONE,
            )
        }

        if (state.isPushNotificationNavigation) {
            action(MainAction.OnNavigateByPushNotification)
            navigateScreenFromNavArgs(navArgs, NotificationNavigatorImpl(navController))
        }
    }

    TopToast(
        message = stringResource(toast.message),
        toastType = toast.type,
        showToast = toast.show
    ) {
        toast = toast.copy(show = false)
    }

    if (state.restriction.restrictionType != RestrictionType.NONE) {
        RestrictionBottomSheet(
            content = when (state.restriction.restrictionType) {
                RestrictionType.TEMPORARY_BAN_MESSAGE_REPORT -> RestrictionContent.TYPE1
                RestrictionType.PERMANENT_BAN_MESSAGE_REPORT -> RestrictionContent.TYPE2
                RestrictionType.PERMANENT_BAN_VOTE_REPORT -> RestrictionContent.TYPE3
                else -> RestrictionContent.TYPE1
            },
            state = state,
            navigator = navigator,
        )
    }

    LaunchedEffect(Unit) {
        action(MainAction.OnMainScreenEntered)
    }
}

@Composable
private fun BottomNavigationTab(
    selectedNavigation: NavGraphSpec,
    onNavigationSelected: (NavGraphSpec) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        containerColor = WeSpotThemeManager.colors.naviColor,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BottomBarDestinations.values().forEach { destination ->
                TabItem(
                    icon = painterResource(id = destination.icon),
                    emptyIcon = painterResource(id = destination.emptyIcon),
                    title = stringResource(id = destination.title),
                    description = stringResource(id = destination.title),
                    selected = selectedNavigation == destination.screen,
                ) {
                    onNavigationSelected(destination.screen)
                }
            }
        }
    }
}

@Stable
@Composable
private fun NavController.currentScreenAsState(): State<NavGraphSpec> {
    val selectedItem = remember { mutableStateOf(AppNavGraphs.vote) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            Timber.d("Destination changed to: ${destination.route}")
            selectedItem.value = destination.navGraph()
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedItem
}

@Stable
@Composable
private fun NavController.checkCurrentScreen(position: NavigationBarPosition): State<BarType> {
    val showBar = remember { mutableStateOf(BarType.NONE) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            showBar.value = destination.checkDestination(position)
        }

        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return showBar
}

@Composable
private fun RowScope.TabItem(
    icon: Painter,
    emptyIcon: Painter,
    title: String,
    description: String,
    selected: Boolean = false,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .weight(1f)
            .clickableSingle { onClick.invoke() },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                painter = if (selected) {
                    icon
                } else {
                    emptyIcon
                },
                contentDescription = description,
            )

            Text(
                text = title,
                style = StaticTypeScale.Default.body9,
                color = if (selected) {
                    WeSpotThemeManager.colors.abledIconColor
                } else {
                    WeSpotThemeManager.colors.disableIcnColor
                },
            )
        }
    }
}

private fun navigateScreenFromNavArgs(
    navArgs: MainScreenNavArgs,
    navigator: NotificationNavigator
) {
    when (navArgs.type) {
        NotificationType.MESSAGE -> {
            navigator.navigateToReceiverSelectionScreen()
        }

        NotificationType.MESSAGE_SENT, NotificationType.MESSAGE_RECEIVED -> {
            navigator.navigateToMessageScreen(type = navArgs.type, messageId = navArgs.targetId)
        }

        NotificationType.VOTE -> {
            navigator.navigateToVotingScreen()
        }

        NotificationType.VOTE_RESULT -> {
            val voteResultDate = navArgs.date.toLocalDateFromDashPattern()
            val isTodayVoteResult = LocalDate.now().equals(voteResultDate)
            navigator.navigateToVoteResultScreen(
                isNavigateFromNotification = false,
                isTodayVoteResult = isTodayVoteResult
            )
        }

        NotificationType.VOTE_RECEIVED -> {
            navigator.navigateToVoteStorageScreen()
        }

        NotificationType.IDLE -> {
        }
    }
}

private fun NavController.navigateToNavGraph(navGraph: NavGraphSpec) {
    this.navigate(navGraph) {
        launchSingleTop = true
        popUpTo(this@navigateToNavGraph.graph.findStartDestination().id)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RestrictionBottomSheet(
    content: RestrictionContent,
    state: MainUiState,
    navigator: Navigator,
) {
    val context = LocalContext.current
    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    WSBottomSheet(
        sheetState = bottomSheetState,
        closeSheet = {
            coroutineScope.launch {
                bottomSheetState.hide()
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(28.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = stringResource(content.title),
                style = StaticTypeScale.Default.body1,
                modifier = Modifier.padding(bottom = 10.dp),
                color = WeSpotThemeManager.colors.txtTitleColor,
            )

            BulletPoint(text = stringResource(content.body1))

            BulletPoint(
                text = stringResource(
                    content.body2,
                    state.restriction.toKoreanDate()
                ),
            )

            BulletPoint(text = stringResource(content.body3))

            if (content.body4 != null) {
                BulletPoint(text = stringResource(content.body4))
            }

            if (content.buttonNumber == 1) {
                WSButton(
                    onClick = {
                        coroutineScope.launch {
                            bottomSheetState.hide()
                        }
                    },
                    text = stringResource(com.bff.wespot.auth.R.string.confirm),
                    paddingValues = PaddingValues(
                        start = 0.dp,
                        end = 0.dp,
                        top = 24.dp,
                        bottom = 10.dp
                    ),
                ) {
                    it.invoke()
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        WSButton(
                            onClick = {
                                coroutineScope.launch {
                                    bottomSheetState.hide()
                                }
                            },
                            buttonType = WSButtonType.Secondary,
                            text = stringResource(R.string.close),
                            paddingValues = PaddingValues(0.dp),
                        ) {
                            it()
                        }
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        WSButton(
                            onClick = {
                                navigator.navigateToWebLink(context, state.kakaoChannel)
                            },
                            text = stringResource(com.bff.wespot.R.string.one_on_one),
                            paddingValues = PaddingValues(0.dp),
                        ) {
                            it()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BulletPoint(text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "•",
            style = StaticTypeScale.Default.body6,
            color = WeSpotThemeManager.colors.txtSubColor,
        )

        Text(
            text = text,
            style = StaticTypeScale.Default.body6,
            color = WeSpotThemeManager.colors.txtSubColor,
        )
    }
}

private enum class RestrictionContent(
    @StringRes val title: Int,
    @StringRes val body1: Int,
    @StringRes val body2: Int,
    @StringRes val body3: Int,
    @StringRes val body4: Int? = null,
    val buttonNumber: Int = 1,
) {
    TYPE1(
        com.bff.wespot.R.string.restriction_title,
        com.bff.wespot.R.string.restriction_type1_body1,
        com.bff.wespot.R.string.restriction_type1_body2,
        com.bff.wespot.R.string.restriction_type1_body3,
        com.bff.wespot.R.string.restriction_type1_body4,
    ),

    TYPE2(
        com.bff.wespot.R.string.restriction_title,
        com.bff.wespot.R.string.restriction_type1_body1,
        com.bff.wespot.R.string.restriction_type2_body2,
        com.bff.wespot.R.string.restriction_type2_body3,
    ),

    TYPE3(
        com.bff.wespot.R.string.restriction_title,
        com.bff.wespot.R.string.restriction_type1_body1,
        com.bff.wespot.R.string.restriction_type3_body2,
        com.bff.wespot.R.string.restriction_type3_body3,
        null,
        2,
    );
}