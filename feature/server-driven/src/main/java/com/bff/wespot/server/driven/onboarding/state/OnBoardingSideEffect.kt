package com.bff.wespot.server.driven.onboarding.state

sealed class OnBoardingSideEffect {
    data object CloseOnBoarding : OnBoardingSideEffect()
}
