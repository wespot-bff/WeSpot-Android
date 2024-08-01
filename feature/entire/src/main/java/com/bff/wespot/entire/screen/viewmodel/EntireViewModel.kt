package com.bff.wespot.entire.screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bff.wespot.domain.repository.user.UserRepository
import com.bff.wespot.entire.screen.state.EntireAction
import com.bff.wespot.entire.screen.state.EntireSideEffect
import com.bff.wespot.entire.screen.state.EntireUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EntireViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel(), ContainerHost<EntireUiState, EntireSideEffect> {
    override val container = container<EntireUiState, EntireSideEffect>(EntireUiState())

    fun onAction(action: EntireAction) {
        when (action) {
            EntireAction.OnEntireScreenEntered -> getProfile()
        }
    }

    private fun getProfile() = intent {
        viewModelScope.launch {
            userRepository.getProfile()
                .onSuccess { profile ->
                    reduce { state.copy(profile = profile) }
                }
                .onFailure {
                    Timber.e(it)
                }
        }
    }
}
