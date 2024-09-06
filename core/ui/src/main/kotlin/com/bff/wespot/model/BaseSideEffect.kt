package com.bff.wespot.model

sealed class BaseSideEffect {
    data class ShowToast(val message: String) : BaseSideEffect()
    data class ShowDialog(val message: String) : BaseSideEffect()
    data object NavigateUp : BaseSideEffect()
}
