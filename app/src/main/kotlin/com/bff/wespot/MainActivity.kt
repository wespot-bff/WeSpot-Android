package com.bff.wespot

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.bff.wespot.analytic.AnalyticsHelper
import com.bff.wespot.analytic.LocalAnalyticsHelper
import com.bff.wespot.designsystem.R
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.indicator.WSToast
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.message.screen.MessageScreenArgs
import com.bff.wespot.message.screen.destinations.MessageScreenDestination
import com.bff.wespot.message.screen.destinations.ReceiverSelectionScreenDestination
import com.bff.wespot.message.screen.send.ReceiverSelectionScreenArgs
import com.bff.wespot.model.ToastState
import com.bff.wespot.model.notification.NotificationType
import com.bff.wespot.model.notification.convertNotificationType
import com.bff.wespot.navigation.Navigator
import com.bff.wespot.state.MainAction
import com.bff.wespot.viewmodel.MainViewModel
import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermission()

        setContent {
            WeSpotTheme {
                MainScreen(
                    navigator = navigator,
                    navArgs = getMainScreenArgsFromIntent(),
                    analyticsHelper = analyticsHelper,
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
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }
    }

    private fun getMainScreenArgsFromIntent(): MainScreenNavArgs {
        val targetId = intent.getStringExtra("targetId")?.toInt() ?: -1
        val date = intent.getStringExtra("date") ?: ""
        val type = intent.getStringExtra("type") ?: ""

        return MainScreenNavArgs(
            targetId = targetId,
            date = date,
            type = convertNotificationType(type),
        )
    }
}

data class MainScreenNavArgs(
    val type: NotificationType,
    val date: String,
    val targetId: Int,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen(
    navigator: Navigator,
    navArgs: MainScreenNavArgs,
    analyticsHelper: AnalyticsHelper,
    viewModel: MainViewModel = hiltViewModel(),
) {
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
                label = stringResource(com.bff.wespot.R.string.bottom_bar_animated_content_label),
            ) { targetState ->
                if (targetState) {
                    WSTopBar(
                        title = "",
                        navigation = {
                            Image(
                                modifier = Modifier
                                    .padding(top = 8.dp, bottom = 8.dp, start = 16.dp)
                                    .size(width = 112.dp, height = 44.dp),
                                painter = painterResource(id = R.drawable.main_logo),
                                contentDescription = stringResource(
                                    id = com.bff.wespot.message.R.string.wespot_logo,
                                ),
                            )
                        },
                        action = {
                            IconButton(
                                modifier = Modifier.padding(end = 16.dp),
                                onClick = {
                                    navController.navigateToNavGraph(AppNavGraphs.notification)
                                },
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.icn_alarm),
                                    contentDescription = stringResource(
                                        id = com.bff.wespot.message.R.string.notification_icon,
                                    ),
                                )
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
                label = stringResource(com.bff.wespot.R.string.top_bar_animated_content_label)
            ) { targetState ->
                if (targetState) {
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
                showToast = { toastState -> toast = toastState }
            )
        }

        navigateScreenFromNavArgs(navArgs, navController)
    }

    if (toast.show) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            WSToast(
                text = stringResource(toast.message),
                showToast = toast.show,
                toastType = toast.type,
                closeToast = {
                    toast = toast.copy(show = false)
                },
            )
        }
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
private fun NavController.checkCurrentScreen(position: NavigationBarPosition): State<Boolean> {
    val showBar = remember { mutableStateOf(false) }

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
private fun TabItem(
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
            .clickable { onClick.invoke() },
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

private fun navigateScreenFromNavArgs(navArgs: MainScreenNavArgs, navController: NavController) {
    when (navArgs.type) {
        NotificationType.MESSAGE -> {
            navController.navigate(
                ReceiverSelectionScreenDestination(
                    ReceiverSelectionScreenArgs(false),
                ) within AppNavGraphs.message
            )
        }

        NotificationType.MESSAGE_SENT, NotificationType.MESSAGE_RECEIVED -> {
            navController.navigate(
                MessageScreenDestination(
                    MessageScreenArgs(
                        type = navArgs.type,
                        messageId = navArgs.targetId,
                    ),
                ) within AppNavGraphs.message
            )
        }

        NotificationType.VOTE -> {
        }

        NotificationType.VOTE_RESULT -> {
        }

        NotificationType.VOTE_RECEIVED -> {
        }

        NotificationType.IDLE -> {
        }
    }
}

private fun NavController.navigateToNavGraph(navGraph: NavGraphSpec) {
    this.navigate(navGraph) {
        launchSingleTop = true
        restoreState = true

        popUpTo(this@navigateToNavGraph.graph.findStartDestination().id) {
            saveState = true
        }
    }
}
