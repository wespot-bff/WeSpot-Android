package com.bff.wespot.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bff.wespot.domain.usecase.CacheProfileUseCase
import com.bff.wespot.state.MainAction
import com.bff.wespot.state.MainSideEffect
import com.bff.wespot.state.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val cacheProfileUseCase: CacheProfileUseCase,
): ViewModel(), ContainerHost<MainUiState, MainSideEffect> {
    override val container = container<MainUiState, MainSideEffect>(MainUiState())

    fun onAction(action: MainAction) {
        when (action) {
            MainAction.OnMainScreenEntered -> handleMainScreenEntered()
            MainAction.OnNavigateByPushNotification -> handleNavigateByPushNotification()
        }
    }

    private fun handleMainScreenEntered() {
        viewModelScope.launch {
            cacheProfileUseCase()
        }
    }

    private fun handleNavigateByPushNotification() = intent {
        reduce { state.copy(isPushNotificationNavigation = false) }
    }
}
