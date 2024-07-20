package com.bff.wespot.entire.screen

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination

interface EntireNavigator {
    fun navigateUp()
}

@Destination
@Composable
internal fun EntireScreen(
    entireNavigator: EntireNavigator,
) {
    // Entire screen content
}
