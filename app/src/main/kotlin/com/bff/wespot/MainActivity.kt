package com.bff.wespot

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.dynamic.within
import com.bff.wespot.designsystem.R
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.message.screen.MessageScreenArgs
import com.bff.wespot.message.screen.destinations.MessageScreenDestination
import com.bff.wespot.message.screen.destinations.ReceiverSelectionScreenDestination
import com.bff.wespot.message.screen.send.ReceiverSelectionScreenArgs
import com.bff.wespot.model.notification.NotificationType
import com.bff.wespot.model.notification.convertNotificationType
import com.bff.wespot.navigation.Navigator
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermission()

        setContent {
            WeSpotTheme {
                MainScreen(
                    navigator = navigator,
                    navArgs = getMainScreenArgsFromIntent(),
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

            if(!hasPermission) {
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
private fun MainScreen(navigator: Navigator, navArgs: MainScreenNavArgs) {
    val navController = rememberNavController()

    val checkScreen by navController.checkCurrentScreen()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (checkScreen) {
                WSTopBar(
                    title = "",
                    navigation = {
                        Image(
                            modifier = Modifier
                                .padding(top = 8.dp, bottom = 8.dp, start = 16.dp)
                                .size(width = 112.dp, height = 44.dp),
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = stringResource(
                                id = com.bff.wespot.message.R.string.wespot_logo,
                            ),
                        )
                    },
                    action = {
                        IconButton(
                            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, end = 4.dp),
                            onClick = {
                                navController.navigateToNavGraph(AppNavGraphs.notification)
                            },
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.notification),
                                contentDescription = stringResource(
                                    id = com.bff.wespot.message.R.string.notification_icon,
                                ),
                            )
                        }
                    },
                )
            }
        },
        bottomBar = {
            if (checkScreen) {
                val currentSelectedItem by navController.currentScreenAsState()
                BottomNavigationTab(
                    selectedNavigation = currentSelectedItem,
                    onNavigationSelected = { selected ->
                        navController.navigateToNavGraph(selected)
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
    ) {
        AppNavigation(
            navController = navController,
            modifier = Modifier.padding(it),
            navigator = navigator
        )

        navigateScreenFromNavArgs(navArgs, navController)
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
private fun NavController.checkCurrentScreen(): State<Boolean> {
    val showBar = remember { mutableStateOf(false) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            showBar.value = destination.checkDestination()
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
                painter = icon,
                contentDescription = description,
                tint = if (selected) {
                    WeSpotThemeManager.colors.abledIconColor
                } else {
                    WeSpotThemeManager.colors.disableIcnColor
                },
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
