package com.bff.wespot.data.repository.serverDriven

import com.bff.wespot.data.remote.source.serverDriven.OnBoardingDataSource
import com.bff.wespot.domain.repository.serverDriven.OnBoardingRepository
import com.bff.wespot.model.serverDriven.OnBoarding
import com.bff.wespot.model.serverDriven.OnBoardingCategory
import javax.inject.Inject

class OnBoardingRepositoryImpl @Inject constructor(
    private val onBoardingDataSource: OnBoardingDataSource
) : OnBoardingRepository {
    override suspend fun getOnBoarding(category: OnBoardingCategory): Result<List<OnBoarding>> =
        onBoardingDataSource.getOnBoarding(category)
            .mapCatching {
                it.map {
                    it.toDomain()
                }
            }
}