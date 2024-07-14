package com.bff.wespot.auth.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavOptions
import com.bff.wespot.auth.screen.destinations.AuthScreenDestination
import com.bff.wespot.auth.screen.destinations.SchoolScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@RootNavGraph(start = true)
@Composable
fun AuthScreen(
    navigator: DestinationsNavigator,
) {
    navigator.navigate(
        SchoolScreenDestination(edit = false),
        navOptions = NavOptions
            .Builder()
            .setPopUpTo(AuthScreenDestination.route, inclusive = true)
            .build(),
    )
}
