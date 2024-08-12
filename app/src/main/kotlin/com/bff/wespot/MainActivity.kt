package com.bff.wespot

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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.bff.wespot.designsystem.R
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.indicator.WSToast
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.model.ToastState
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

        setContent {
            WeSpotTheme {
                MainScreen(navigator)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen(navigator: Navigator) {
    val navController = rememberNavController()
    var toast by remember { mutableStateOf(ToastState()) }

    val isTopNavigationScreen by navController.checkCurrentScreen(NavigationBarPosition.TOP)
    val isBottomNavigationScreen by navController.checkCurrentScreen(NavigationBarPosition.BOTTOM)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (isTopNavigationScreen) {
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
            if (isBottomNavigationScreen) {
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
            navigator = navigator,
            showToast = { toastState -> toast = toastState }
        )
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

private fun NavController.navigateToNavGraph(navGraph: NavGraphSpec) {
    this.navigate(navGraph) {
        launchSingleTop = true
        restoreState = true

        popUpTo(this@navigateToNavGraph.graph.findStartDestination().id) {
            saveState = true
        }
    }
}
