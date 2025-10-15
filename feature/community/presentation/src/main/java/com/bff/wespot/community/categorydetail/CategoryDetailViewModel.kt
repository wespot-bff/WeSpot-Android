package com.bff.wespot.community.categorydetail

import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.community.categorydetail.state.CategoryDetailAction
import com.bff.wespot.community.categorydetail.state.CategoryDetailParams
import com.bff.wespot.community.categorydetail.state.CategoryDetailSideEffect
import com.bff.wespot.community.categorydetail.state.CategoryDetailUiState
import com.bff.wespot.community.uimodel.PostItemUiModel
import com.bff.wespot.community.uimodel.toUiModel
import com.bff.wespot.domain.repository.community.CommunityRepository
import com.bff.wespot.domain.repository.community.WritePostRepository
import com.bff.wespot.model.community.chip.CategoryItem
import com.bff.wespot.ui.base.BaseViewModel
import com.bff.wespot.ui.model.SideEffect.Companion.toSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class CategoryDetailViewModel @Inject constructor(
    params: CategoryDetailParams,
    private val communityRepository: CommunityRepository,
    private val writePostRepository: WritePostRepository,
    private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel(),
    ContainerHost<CategoryDetailUiState, CategoryDetailSideEffect> {
    private var categoryId: String = params.categoryId

    override val container = container<CategoryDetailUiState, CategoryDetailSideEffect>(
        CategoryDetailUiState(
            currentCategory = CategoryItem(
                id = params.categoryId,
                text = params.categoryText,
            ),
        ),
    )

    fun onAction(action: CategoryDetailAction) = intent {
        when (action) {
            is CategoryDetailAction.OnBackClick -> {
                postSideEffect(CategoryDetailSideEffect.NavigateBack)
            }

            is CategoryDetailAction.OnReactionClick -> {
                onReactionClick(action.id, action.reaction)
            }

            is CategoryDetailAction.OnScrapClick -> {
                onScrapClick(action.id)
            }

            is CategoryDetailAction.OnRefresh -> {
                loadCategoryPosts()
            }

            is CategoryDetailAction.OnCategoryChange -> {
                onCategoryChange(action.categoryId)
            }

            is CategoryDetailAction.LoadData -> {
                loadCategories()
                loadCategoryPosts()
            }

            is CategoryDetailAction.OnFABClicked -> {
                onFABClicked()
            }

            is CategoryDetailAction.NavigateToDetail -> {
                postSideEffect(CategoryDetailSideEffect.NavigateToPostDetail(action.postId))
            }
        }
    }

    private fun loadCategoryPosts() = intent {
        viewModelScope.launch(ioDispatcher) {
            val paging = communityRepository
                .getCategoryPostsStreamWithImages(
                    categoryId = categoryId,
                    onImagesLoaded = { background, thumbnail ->
                        intent {
                            reduce {
                                state.copy(
                                    backgroundImage = background,
                                    thumbnailImage = thumbnail,
                                )
                            }
                        }
                    },
                ).map {
                    it.map { content ->
                        content.toUiModel()
                    }
                }

            reduce {
                state.copy(posts = paging)
            }
        }
    }

    private fun onReactionClick(
        postId: String,
        reaction: PostItemUiModel.PostContentUiModel.FooterSectionUiModel.ReactionUiModel,
    ) = intent {
        viewModelScope.launch(ioDispatcher) {
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
                    postSideEffect(CategoryDetailSideEffect.NavigateToPostDetail(postId, true))
                }

                else -> {}
            }
        }
    }

    private fun onScrapClick(postId: String) = intent {
        viewModelScope.launch(ioDispatcher) {
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
    }

    private fun loadCategories() = intent {
        viewModelScope.launch(ioDispatcher) {
            writePostRepository
                .getCategories()
                .onNetworkFailure {
                    postSideEffect(it.toSideEffect())
                }.onSuccess {
                    reduce {
                        state.copy(categories = it)
                    }
                }
        }
    }

    private fun onCategoryChange(newCategoryId: String) = intent {
        viewModelScope.launch(ioDispatcher) {
            categoryId = newCategoryId

            val selectedCategory = state.categories
                .flatMap { it.chips }
                .find { it.id == newCategoryId } ?: CategoryItem.EMPTY

            reduce {
                state.copy(currentCategory = selectedCategory)
            }

            loadCategoryPosts()
        }
    }

    private fun onFABClicked() = intent {
        postSideEffect(CategoryDetailSideEffect.NavigateToCreate(state.currentCategory))
    }
}
