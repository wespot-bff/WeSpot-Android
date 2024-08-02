package com.bff.wespot.vote.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bff.wespot.domain.repository.CommonRepository
import com.bff.wespot.model.common.BackgroundColor
import com.bff.wespot.model.common.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CharacterSettingViewModel @Inject constructor(
    private val commonRepository: CommonRepository,
) : ViewModel() {
    val characters: StateFlow<List<Character>> = flow {
        commonRepository.getCharacters()
            .onSuccess {
                emit(it)
            }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList(),
    )

    val backgroundColor: StateFlow<List<BackgroundColor>> = flow {
        commonRepository.getBackgroundColors()
            .onSuccess {
                emit(it)
            }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList(),
    )
}
