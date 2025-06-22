package com.bff.wespot.server.driven.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bff.wespot.domain.repository.serverDriven.OnBoardingRepository
import com.bff.wespot.model.serverDriven.OnBoarding
import com.bff.wespot.model.serverDriven.OnBoardingCategory
import com.bff.wespot.server.driven.onboarding.state.OnBoardingNotificationAction
import com.bff.wespot.server.driven.onboarding.state.OnBoardingSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository,
) : ViewModel() {
    private val _contents = MutableStateFlow<List<OnBoarding>>(emptyList())
    val contents = _contents.asStateFlow()

    private val _sideEffect = Channel<OnBoardingSideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    fun onAction(action: OnBoardingNotificationAction) {
        when (action) {
            is OnBoardingNotificationAction.GetOnBoarding -> getOnBoarding(action.category)
            is OnBoardingNotificationAction.ViewedOnBoarding -> viewedOnBoarding(action.category)
        }
    }

    private fun getOnBoarding(category: OnBoardingCategory) {
        viewModelScope.launch {
            onBoardingRepository.getOnBoarding(category)
                .onSuccess {
                    if (it.isEmpty()) {
                        _sideEffect.send(OnBoardingSideEffect.CloseOnBoarding)
                        return@launch
                    }
                    _contents.value = it
                }
                .onFailure {
                    _sideEffect.send(OnBoardingSideEffect.CloseOnBoarding)
                }
        }
    }

    private fun viewedOnBoarding(category: OnBoardingCategory) {
        viewModelScope.launch {
            onBoardingRepository.viewedOnBoarding(category)
        }
    }
}
