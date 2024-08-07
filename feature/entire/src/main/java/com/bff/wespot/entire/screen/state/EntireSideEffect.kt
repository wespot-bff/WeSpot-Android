package com.bff.wespot.entire.screen.state

sealed class EntireSideEffect {
    data object NavigateToAuth : EntireSideEffect()
    data object NavigateToEntire : EntireSideEffect()
    data object ShowToast : EntireSideEffect()
}
