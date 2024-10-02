package com.bff.wespot.vote.viewmodel

import androidx.lifecycle.viewModelScope
import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.domain.repository.CommonRepository
import com.bff.wespot.domain.repository.DataStoreRepository
import com.bff.wespot.domain.util.DataStoreKey
import com.bff.wespot.model.common.BackgroundColor
import com.bff.wespot.model.common.Character
import com.bff.wespot.ui.base.BaseViewModel
import com.bff.wespot.ui.model.SideEffect.Companion.toSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CharacterSettingViewModel @Inject constructor(
    private val commonRepository: CommonRepository,
    private val dataStoreRepository: DataStoreRepository,
) : BaseViewModel() {
    val characters: StateFlow<List<Character>> = flow {
        commonRepository.getCharacters()
            .onSuccess {
                emit(it)
            }
            .onNetworkFailure {
                postSideEffect(it.toSideEffect())
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
            .onNetworkFailure {
                postSideEffect(it.toSideEffect())
            }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList(),
    )

    val name = dataStoreRepository.getString(DataStoreKey.NAME)
}
