package com.bff.wespot.message.common

import com.bff.wespot.message.state.send.SendSideEffect
import com.bff.wespot.model.BaseSideEffect

internal fun BaseSideEffect.toSendSideEffect() =
    when (this) {
        is BaseSideEffect.ShowToast -> SendSideEffect.ShowToast(this.message)
        is BaseSideEffect.ShowDialog -> SendSideEffect.ShowDialog(this.message)
        BaseSideEffect.NavigateUp -> SendSideEffect.NavigateUp
    }
