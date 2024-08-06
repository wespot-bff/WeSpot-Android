package com.bff.wespot.entire.screen.state

sealed class EntireSideEffect {
    data object NavigateToAuth : EntireSideEffect()
    data class ShowToast(val message: String) : EntireSideEffect()
}
