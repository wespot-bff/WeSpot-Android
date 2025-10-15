package com.bff.wespot.server.driven.onboarding.state

import com.bff.wespot.model.serverDriven.OnBoardingCategory

sealed class OnBoardingNotificationAction {
    data class GetOnBoarding(
        val category: OnBoardingCategory,
    ) : OnBoardingNotificationAction()
    data class ViewedOnBoarding(
        val category: OnBoardingCategory,
    ) : OnBoardingNotificationAction()
}
