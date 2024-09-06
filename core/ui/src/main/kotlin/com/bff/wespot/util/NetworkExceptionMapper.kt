package com.bff.wespot.util

import com.bff.wespot.model.BaseSideEffect
import com.bff.wespot.model.exception.NetworkException
import com.bff.wespot.model.exception.NetworkExceptionViewType

fun NetworkException.toBaseSideEffect(): BaseSideEffect =
    when (this.viewType) {
        NetworkExceptionViewType.TOAST -> BaseSideEffect.ShowToast(this.detail)
        NetworkExceptionViewType.DIALOG -> BaseSideEffect.ShowDialog(this.detail)
        NetworkExceptionViewType.REDIRECT -> BaseSideEffect.NavigateUp
    }
