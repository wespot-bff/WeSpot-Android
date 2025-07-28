package com.bff.wespot.community.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.community.state.CommunityAction
import com.bff.wespot.community.state.CommunitySideEffect
import com.bff.wespot.community.state.CommunityUiState
import com.bff.wespot.community.uimodel.chip.toUiModel
import com.bff.wespot.community.uimodel.toUiModel
import com.bff.wespot.domain.repository.community.CommunityRepository
import com.bff.wespot.ui.base.BaseViewModel
import com.bff.wespot.ui.model.SideEffect.Companion.toSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class CommunityHomeViewModel @Inject constructor(
    private val communityRepository: CommunityRepository,
) : BaseViewModel(), ContainerHost<CommunityUiState, CommunitySideEffect> {
    override val container = container<CommunityUiState, CommunitySideEffect>(
        CommunityUiState(),
    )

    init {
        viewModelScope.launch {
            intent {
                communityRepository.getCommunityChips()
                    .onNetworkFailure {
                        postSideEffect(it.toSideEffect())
                    }
                    .onSuccess {
                        reduce {
                            state.copy(
                                filterChips = it.map { it.toUiModel() },
                                selectedChipId = it.firstOrNull()?.toUiModel()?.id ?: "",
                            )
                        }
                    }
            }
        }
    }

    fun onAction(action: CommunityAction) = intent {
        when (action) {
            is CommunityAction.OnFilterChipClicked -> {
                onFilterChipClicked(action.id, action.target)
            }

            is CommunityAction.OnMoreClicked -> {}
            is CommunityAction.OnWritePostClicked -> {
                postSideEffect(CommunitySideEffect.NavigateToWriteActivity)
            }
        }
    }

    private fun onFilterChipClicked(id: String, target: String, inquirySize: Int = 10) = intent {
        val paging = communityRepository.getCommunityContentStream(target, inquirySize)
            .map {
                it.map { content ->
                    content.toUiModel()
                }
            }
        reduce {
            state.copy(
                posts = paging,
                selectedChipId = id,
            )
        }
    }
}
