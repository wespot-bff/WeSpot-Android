package com.bff.wespot.model.serverDriven

data class OnBoarding(
    val id: Int,
    val name: String,
    val data: List<OnBoardingContent>,
)

data class OnBoardingContent(
    val page: Int,
    val data: List<BaseComponent>,
)
