package com.bff.wespot.community.search

import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.bff.wespot.community.search.state.SearchAction
import com.bff.wespot.community.search.state.SearchSideEffect
import com.bff.wespot.community.search.state.SearchUiState
import com.bff.wespot.community.uimodel.PostItemUiModel.PostContentUiModel.FooterSectionUiModel.ReactionUiModel
import com.bff.wespot.community.uimodel.toUiModel
import com.bff.wespot.domain.repository.community.CommunityRepository
import com.bff.wespot.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
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
internal class SearchViewModel @Inject constructor(
    private val communityRepository: CommunityRepository,
) : BaseViewModel(), ContainerHost<SearchUiState, SearchSideEffect> {
    override val container: Container<SearchUiState, SearchSideEffect> = container(
        SearchUiState(),
    )

    private val searches = MutableStateFlow("")

    fun onAction(action: SearchAction) {
        when (action) {
            is SearchAction.HandleSearchChange -> handleSearchChange(action.keyword)
            is SearchAction.NavigateToDetail -> {
                intent {
                    postSideEffect(SearchSideEffect.NavigateToDetail(action.postId))
                }
            }

            is SearchAction.MonitorUserInput -> monitorUserInput()
            is SearchAction.OnReactionClick -> {
                onReactionClick(action.id, action.reaction)
            }

            is SearchAction.OnScrapClick -> {
                onScrapClick(action.id)
            }
            is SearchAction.NavigateToCategory -> {
                intent {
                    postSideEffect(SearchSideEffect.NavigateToSearch(action.categoryId))
                }
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun monitorUserInput() {
        viewModelScope.launch(coroutineDispatcher) {
            searches
                .debounce(INPUT_DEBOUNCE_TIME)
                .distinctUntilChanged()
                .collect {
                    fetchCommunitySearch(it)
                }
        }
    }

    private fun handleSearchChange(text: String) = intent {
        reduce {
            searches.value = text
            state.copy(
                keyword = text,
            )
        }
    }

    private fun fetchCommunitySearch(keyword: String) {
        viewModelScope.launch {
            intent {
                reduce {
                    state.copy(
                        searches = communityRepository.getCommunitySearchStream(keyword)
                            .map { it.map { content -> content.toUiModel() } },
                    )
                }
            }
        }
    }

    private fun onReactionClick(postId: String, reaction: ReactionUiModel) = intent {
        when (reaction) {
            is ReactionUiModel.LikeUiModel -> {
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

            is ReactionUiModel.ChatUiModel -> {
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

    companion object {
        private const val INPUT_DEBOUNCE_TIME = 500L
    }
}
