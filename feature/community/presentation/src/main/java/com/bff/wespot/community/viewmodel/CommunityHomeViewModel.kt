package com.bff.wespot.community.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.community.state.CommunityAction
import com.bff.wespot.community.state.CommunitySideEffect
import com.bff.wespot.community.state.CommunityUiState
import com.bff.wespot.community.uimodel.PostItemUiModel
import com.bff.wespot.community.uimodel.chip.FilterChipUiModel
import com.bff.wespot.community.uimodel.chip.toUiModel
import com.bff.wespot.community.uimodel.toUiModel
import com.bff.wespot.domain.repository.community.CommunityRepository
import com.bff.wespot.ui.base.BaseViewModel
import com.bff.wespot.ui.model.SideEffect.Companion.toSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

            is CommunityAction.OnCommunityEnter -> {
                onFilterChipClicked("", "")
            }

            is CommunityAction.OnReactionClick -> {
                onReactionClick(action.id, action.reaction)
            }

            is CommunityAction.OnScrapClick -> {
                onScrapClick(action.id)
            }

            is CommunityAction.OnRefresh -> {
                refreshPosts()
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

    private fun onReactionClick(
        postId: String,
        reaction: PostItemUiModel.PostContentUiModel.FooterSectionUiModel.ReactionUiModel,
    ) = intent {
        when (reaction) {
            is PostItemUiModel.PostContentUiModel.FooterSectionUiModel.ReactionUiModel.LikeUiModel -> {
                val isCurrentlyLiked = state.likedPosts.contains(postId)
                reduce {
                    state.copy(
                        likedPosts = if (isCurrentlyLiked) {
                            state.likedPosts - postId
                        } else {
                            state.likedPosts + postId
                        },
                    )
                }

                val result = communityRepository.onLikeClicked(postId)
                if (!result) {
                    reduce {
                        state.copy(
                            likedPosts = if (isCurrentlyLiked) {
                                state.likedPosts + postId
                            } else {
                                state.likedPosts - postId
                            },
                        )
                    }
                }
            }

            is PostItemUiModel.PostContentUiModel.FooterSectionUiModel.ReactionUiModel.ChatUiModel -> {
            }
        }
    }

    private fun onScrapClick(postId: String) = intent {
        val isCurrentlyScrapped = state.scrappedPosts.contains(postId)
        reduce {
            state.copy(
                scrappedPosts = if (isCurrentlyScrapped) {
                    state.scrappedPosts - postId
                } else {
                    state.scrappedPosts + postId
                },
            )
        }

        val result = communityRepository.onScrapClicked(postId)
        if (!result) {
            reduce {
                state.copy(
                    scrappedPosts = if (isCurrentlyScrapped) {
                        state.scrappedPosts + postId
                    } else {
                        state.scrappedPosts - postId
                    },
                )
            }
        }
    }

    private fun refreshPosts() = intent {
        reduce { state.copy(isRefreshing = true) }

        val selectedChip = state.filterChips.find { it.id == state.selectedChipId }
        val target = when (selectedChip) {
            is FilterChipUiModel -> selectedChip.target
            else -> ""
        }

        val paging = communityRepository.getCommunityContentStream(target, 10)
            .map {
                it.map { content ->
                    content.toUiModel()
                }
            }

        reduce {
            state.copy(posts = paging)
        }

        delay(500)

        reduce {
            state.copy(isRefreshing = false)
        }
    }
}
