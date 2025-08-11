package com.bff.wespot.community.all

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.bff.wespot.community.all.state.CommunityAllAction
import com.bff.wespot.community.all.state.CommunityAllSideEffect
import com.bff.wespot.community.all.state.CommunityAllUiState
import com.bff.wespot.community.uimodel.PostItemUiModel.PostContentUiModel.FooterSectionUiModel
import com.bff.wespot.community.uimodel.toUiModel
import com.bff.wespot.domain.repository.community.CommunityRepository
import com.bff.wespot.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class CommunityAllViewModel @Inject constructor(
    private val communityRepository: CommunityRepository,
) : BaseViewModel(), ContainerHost<CommunityAllUiState, CommunityAllSideEffect> {
    override val container: Container<CommunityAllUiState, CommunityAllSideEffect> = container(
        CommunityAllUiState(),
    )

    fun onAction(action: CommunityAllAction) = intent {
        when (action) {
            is CommunityAllAction.LoadPostsByMenuType -> loadPostsByMenuType(action.menu)
            is CommunityAllAction.NavigateToDetail -> {
                postSideEffect(CommunityAllSideEffect.NavigateToDetail(action.id))
            }

            is CommunityAllAction.NavigateUp -> {
                postSideEffect(CommunityAllSideEffect.NavigateUp)
            }

            is CommunityAllAction.NavigateToCategory -> {
                postSideEffect(
                    CommunityAllSideEffect.NavigateToCategory(
                        action.categoryId,
                        action.categoryText,
                    ),
                )
            }

            is CommunityAllAction.OnScrapClick -> {
                onScrapClick(action.id)
            }

            is CommunityAllAction.OnReactionClick -> {
                onReactionClick(action.reaction, action.id)
            }
            is CommunityAllAction.NavigateToDetailComments -> {
                postSideEffect(CommunityAllSideEffect.NavigateToDetail(action.id, true))
            }
        }
    }

    private fun loadPostsByMenuType(menuType: String) = intent {
        val paging = communityRepository
            .getCommunityAllPostsStream(menuType)
            .cachedIn(viewModelScope)
            .map {
                it.map { it.toUiModel() }
            }

        reduce {
            state.copy(paging = paging)
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

        viewModelScope.launch {
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
    }

    private fun onReactionClick(
        reaction: FooterSectionUiModel.ReactionUiModel,
        postId: String,
    ) = intent {
        when (reaction) {
            is FooterSectionUiModel.ReactionUiModel.LikeUiModel -> {
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

                viewModelScope.launch {
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
            }

            is FooterSectionUiModel.ReactionUiModel.ChatUiModel -> {
                postSideEffect(CommunityAllSideEffect.NavigateToDetail(postId))
            }
        }
    }
}
