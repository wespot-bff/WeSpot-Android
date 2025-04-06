package com.bff.wespot.domain.repository.serverDriven

import com.bff.wespot.model.serverDriven.OnBoarding
import com.bff.wespot.model.serverDriven.OnBoardingCategory

interface OnBoardingRepository {
    suspend fun getOnBoarding(category: OnBoardingCategory): Result<List<OnBoarding>>
    suspend fun viewedOnBoarding(category: OnBoardingCategory)
}
