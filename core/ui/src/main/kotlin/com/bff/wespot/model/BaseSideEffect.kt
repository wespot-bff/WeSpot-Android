package com.bff.wespot.model

import com.bff.wespot.model.exception.NetworkException
import com.bff.wespot.model.exception.NetworkExceptionViewType

sealed class BaseSideEffect {
    data class ShowToast(val message: String) : BaseSideEffect()
    data class ShowDialog(val message: String) : BaseSideEffect()
    data object NavigateUp : BaseSideEffect()

    companion object {
        private const val DEFAULT_TOAST_MESSAGE = "알 수 없는 예외가 발생하였습니다."

        fun NetworkException.toBaseSideEffect(): BaseSideEffect =
            when (this.viewType) {
                NetworkExceptionViewType.TOAST -> ShowToast(this.detail)
                NetworkExceptionViewType.DIALOG -> ShowDialog(this.detail)
                NetworkExceptionViewType.REDIRECT -> NavigateUp
            }

        fun Throwable.toToastSideEffect(
            message: String = DEFAULT_TOAST_MESSAGE,
        ) = ShowToast(message)
    }
}
