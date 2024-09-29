package com.bff.wespot.model

import com.bff.wespot.model.exception.NetworkException
import com.bff.wespot.model.exception.NetworkExceptionViewType

sealed class SideEffect {
    data object None : SideEffect()
    data object Redirect : SideEffect()
    data class ShowToast(val message: String) : SideEffect()
    data class ShowDialog(val message: String) : SideEffect()

    companion object {
        fun NetworkException.toSideEffect(): SideEffect =
            when (this.viewType) {
                NetworkExceptionViewType.TOAST -> ShowToast(this.detail)
                NetworkExceptionViewType.DIALOG -> ShowDialog(this.detail)
                NetworkExceptionViewType.REDIRECT -> Redirect
            }
    }
}
