package com.bff.wespot.model

import com.bff.wespot.model.exception.NetworkException
import com.bff.wespot.model.exception.NetworkExceptionViewType

sealed class SideEffect {
    data object None : SideEffect()
    data object Redirect : SideEffect()
    data class ShowToast(val message: String) : SideEffect()
    data class ShowDialog(val message: String) : SideEffect()

    companion object {
        private const val DEFAULT_TOAST_MESSAGE = "알 수 없는 에러가 발생하였습니다."

        fun NetworkException.toSideEffect(): SideEffect =
            when (this.viewType) {
                NetworkExceptionViewType.TOAST -> ShowToast(this.detail)
                NetworkExceptionViewType.DIALOG -> ShowDialog(this.detail)
                NetworkExceptionViewType.REDIRECT -> Redirect
            }

        fun toToastEffect(message: String = DEFAULT_TOAST_MESSAGE) = ShowToast(message)
    }
}
