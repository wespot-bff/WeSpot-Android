package com.bff.wespot.data.remote.source.serverDriven

import com.bff.wespot.data.remote.model.serverDriven.OnBoardingDto
import com.bff.wespot.model.serverDriven.OnBoardingCategory

interface OnBoardingDataSource {
    suspend fun getOnBoarding(category: OnBoardingCategory): Result<List<OnBoardingDto>>
    suspend fun viewedOnBoarding(category: OnBoardingCategory): Result<Unit>
}