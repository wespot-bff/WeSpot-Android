package com.bff.wespot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
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
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.ramcosta.composedestinations.spec.NavGraphSpec
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val engine = rememberNavHostEngine()

            WeSpotTheme {
                MainScreen()
            }
        }
    }
}

@Composable
private fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {},
        bottomBar = {
            val currentSelectedItem by navController.currentScreenAsState()
            BottomNavigationTab(
                selectedNavigation = currentSelectedItem,
                onNavigationSelected = { selected ->
                    navController.navigate(selected) {
                        launchSingleTop = true
                        restoreState = true

                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) {
        AppNavigation(navController = navController, modifier = Modifier.padding(it))
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
            verticalAlignment = Alignment.CenterVertically
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
    val selectedItem = remember { mutableStateOf(NavGraphs.vote) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            selectedItem.value = destination.navGraph()
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedItem
}


@Composable
private fun TabItem(
    icon: Painter,
    title: String,
    description: String,
    selected: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clickable { onClick.invoke() },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = icon,
                contentDescription = description,
                tint = if (selected) {
                    WeSpotThemeManager.colors.abledIconColor
                } else {
                    WeSpotThemeManager.colors.disableIcnColor
                }
            )

            Text(
                text = title,
                style = StaticTypeScale.Default.body9,
                color = if (selected) {
                    WeSpotThemeManager.colors.abledIconColor
                } else {
                    WeSpotThemeManager.colors.disableIcnColor
                }
            )
        }
    }
}