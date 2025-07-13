package com.bff.wespot.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.bff.wespot.navigation.AppNavGraphs
import com.bff.wespot.main.model.BarType
import com.bff.wespot.main.model.NavigationBarPosition
import com.bff.wespot.navigation.checkDestination
import com.bff.wespot.navigation.navGraph
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.NavGraphSpec
import timber.log.Timber

internal fun NavController.navigateToNavGraph(navGraph: NavGraphSpec) {
    this.navigate(navGraph) {
        launchSingleTop = true
        popUpTo(this@navigateToNavGraph.graph.findStartDestination().id)
    }
}

@Stable
@Composable
internal fun NavController.checkCurrentScreen(position: NavigationBarPosition): State<BarType> {
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

@Stable
@Composable
internal fun NavController.currentScreenAsState(): State<NavGraphSpec> {
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