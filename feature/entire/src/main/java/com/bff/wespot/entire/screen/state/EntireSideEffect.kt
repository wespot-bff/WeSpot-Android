package com.bff.wespot.entire.screen.state

import com.bff.wespot.navigation.Navigator

sealed class EntireSideEffect {
    data class NavigateToAuth(val navigator: Navigator) : EntireSideEffect()
}
