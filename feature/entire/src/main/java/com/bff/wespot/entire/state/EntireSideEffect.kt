package com.bff.wespot.entire.state

sealed class EntireSideEffect {
    data object NavigateToAuth : EntireSideEffect()
    data object CloseSignOutDialog : EntireSideEffect()
}
