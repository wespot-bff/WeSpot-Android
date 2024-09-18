package com.bff.wespot.model

data class SideEffectState(
    val show: Boolean,
    val sideEffect: SideEffect,
) {
    constructor() : this(false, SideEffect.ShowToast(""))
}
