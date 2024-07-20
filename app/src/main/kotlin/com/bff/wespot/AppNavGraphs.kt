package com.bff.wespot

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import com.bff.wespot.message.screen.destinations.MessageScreenDestination
import com.danggeun.entire.screen.destinations.EntireScreenDestination
import com.danggeun.vote.screen.destinations.VoteHomeScreenDestination
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.ramcosta.composedestinations.scope.DestinationScope
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec

object NavGraphs {
    val vote = object : NavGraphSpec {
        override val route = "vote"

        override val startRoute = VoteHomeScreenDestination routedIn this

        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            VoteHomeScreenDestination
        ).routedIn(this)
            .associateBy { it.route }
    }

    val message = object : NavGraphSpec {
        override val route = "message"

        override val startRoute = MessageScreenDestination routedIn this

        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            MessageScreenDestination
        ).routedIn(this)
            .associateBy { it.route }
    }

    val entire = object : NavGraphSpec {
        override val route = "entire"

        override val startRoute = EntireScreenDestination routedIn this

        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            EntireScreenDestination
        ).routedIn(this)
            .associateBy { it.route }
    }

    val root = object : NavGraphSpec {
        override val route = "root"

        override val startRoute = vote

        override val destinationsByRoute = emptyMap<String, DestinationSpec<*>>()

        override val nestedNavGraphs = listOf(
            vote,
            message,
            entire
        )
    }
}

fun NavDestination.navGraph(): NavGraphSpec {
    hierarchy.forEach { destination ->
        NavGraphs.root.nestedNavGraphs.forEach { navGraph ->
            if (destination.route == navGraph.route) {
                return navGraph
            }
        }
    }

    throw RuntimeException("Unknown nav graph for destination $route")
}

fun DestinationScope<*>.currentNavigator(openSettings: () -> Unit): CommonNavGraphNavigator {
    return CommonNavGraphNavigator(
        navBackStackEntry.destination.navGraph(),
        navController,
    )
}

@Composable
internal fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val engine = rememberNavHostEngine(
        rootDefaultAnimations = RootNavGraphDefaultAnimations(
            enterTransition = { defaultEnterTransition(initialState, targetState) },
            exitTransition = { defaultExitTransition(initialState, targetState) },
            popEnterTransition = { defaultPopEnterTransition() },
            popExitTransition = { defaultPopExitTransition() },
        ),
    )

    DestinationsNavHost(
        navGraph = NavGraphs.root,
        navController = navController,
        engine = engine,
        modifier = modifier,
    )
}


private fun AnimatedContentTransitionScope<*>.defaultEnterTransition(
    initial: NavBackStackEntry,
    target: NavBackStackEntry,
): EnterTransition {
    val initialNavGraph = initial.destination.hostNavGraph
    val targetNavGraph = target.destination.hostNavGraph
    if (initialNavGraph.id != targetNavGraph.id) {
        return fadeIn()
    }
    return fadeIn() + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)
}

private fun AnimatedContentTransitionScope<*>.defaultExitTransition(
    initial: NavBackStackEntry,
    target: NavBackStackEntry,
): ExitTransition {
    val initialNavGraph = initial.destination.hostNavGraph
    val targetNavGraph = target.destination.hostNavGraph
    if (initialNavGraph.id != targetNavGraph.id) {
        return fadeOut()
    }
    return fadeOut() + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start)
}

private val NavDestination.hostNavGraph: NavGraph
    get() = hierarchy.first { it is NavGraph } as NavGraph

private fun AnimatedContentTransitionScope<*>.defaultPopEnterTransition(): EnterTransition {
    return fadeIn() + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End)
}

private fun AnimatedContentTransitionScope<*>.defaultPopExitTransition(): ExitTransition {
    return fadeOut() + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
}
