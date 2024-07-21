package com.bff.wespot

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import com.bff.wespot.entire.screen.destinations.EntireScreenDestination
import com.bff.wespot.message.screen.destinations.MessageScreenDestination
import com.bff.wespot.vote.screen.destinations.VoteHomeScreenDestination
import com.bff.wespot.vote.screen.destinations.VotingScreenDestination
import com.bff.wespot.vote.viewmodel.VotingViewModel
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.ramcosta.composedestinations.scope.DestinationScopeWithNoDependencies
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec

object AppNavGraphs {
    val vote = object : NavGraphSpec {
        override val route = "vote"

        override val startRoute = VoteHomeScreenDestination routedIn this

        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            VoteHomeScreenDestination,
            VotingScreenDestination,
        ).routedIn(this)
            .associateBy { it.route }
    }

    val message = object : NavGraphSpec {
        override val route = "message"

        override val startRoute = MessageScreenDestination routedIn this

        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            MessageScreenDestination,
        ).routedIn(this)
            .associateBy { it.route }
    }

    val entire = object : NavGraphSpec {
        override val route = "entire"

        override val startRoute = EntireScreenDestination routedIn this

        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            EntireScreenDestination,
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
            entire,
        )
    }
}

private val tabScreenNames = listOf(
    "vote/vote_home_screen",
    "message/message_screen",
    "entire/entire_screen",
)

fun NavDestination.navGraph(): NavGraphSpec {
    hierarchy.forEach { destination ->
        AppNavGraphs.root.nestedNavGraphs.forEach { navGraph ->
            if (destination.route == navGraph.route) {
                return navGraph
            }
        }
    }

    throw ClassNotFoundException("Unknown nav graph for destination $route")
}

fun NavDestination.checkDestination(): Boolean {
    hierarchy.forEach { destination ->
        tabScreenNames.forEach { name ->
            if (destination.route == name) {
                return true
            }
        }
    }
    return false
}

fun DestinationScopeWithNoDependencies<*>.currentNavigator(): CommonNavGraphNavigator {
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
        ),
    )

    DestinationsNavHost(
        navGraph = AppNavGraphs.root,
        navController = navController,
        engine = engine,
        modifier = modifier,
        dependenciesContainerBuilder = {
            dependency(currentNavigator())
            dependency(hiltViewModel<VotingViewModel>())
        },
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
    return fadeOut() + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
}

private val NavDestination.hostNavGraph: NavGraph
    get() = hierarchy.first { it is NavGraph } as NavGraph