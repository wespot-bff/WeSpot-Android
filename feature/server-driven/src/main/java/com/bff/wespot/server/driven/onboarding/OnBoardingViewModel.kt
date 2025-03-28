package com.bff.wespot.server.driven.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bff.wespot.domain.repository.serverDriven.OnBoardingRepository
import com.bff.wespot.model.serverDriven.OnBoarding
import com.bff.wespot.model.serverDriven.OnBoardingCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository,
) : ViewModel() {
    private val _contents = MutableStateFlow<List<OnBoarding>>(emptyList())
    val contents = _contents.asStateFlow()

    fun getOnBoarding(category: OnBoardingCategory) {
        viewModelScope.launch {
            onBoardingRepository.getOnBoarding(category)
                .onSuccess {
                    _contents.value = it
                }
        }
    }
}
