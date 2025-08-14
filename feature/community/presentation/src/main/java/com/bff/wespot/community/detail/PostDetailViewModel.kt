package com.bff.wespot.community.detail

import androidx.lifecycle.viewModelScope
import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.community.detail.state.PostDetailAction
import com.bff.wespot.community.detail.state.PostDetailParams
import com.bff.wespot.community.detail.state.PostDetailSideEffect
import com.bff.wespot.community.detail.state.PostDetailUiState
import com.bff.wespot.community.detail.state.PostDetailUiState.SheetItem.SheetType
import com.bff.wespot.community.uimodel.PostCommentUiModel.Companion.toUiModel
import com.bff.wespot.community.uimodel.PostDetailUiModel
import com.bff.wespot.community.uimodel.PostDetailUiModel.Companion.toUiModel
import com.bff.wespot.community.uimodel.PostDetailUiModel.PostDetailContentUiModel.FooterSectionUiModel.ReactionUiModel
import com.bff.wespot.domain.repository.community.CommunityRepository
import com.bff.wespot.domain.repository.community.PostDetailRepository
import com.bff.wespot.ui.base.BaseViewModel
import com.bff.wespot.ui.model.SideEffect.Companion.toSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    param: PostDetailParams,
    private val postDetailRepository: PostDetailRepository,
    private val communityRepository: CommunityRepository,
    private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel(), ContainerHost<PostDetailUiState, PostDetailSideEffect> {
    override val container = container<PostDetailUiState, PostDetailSideEffect>(
        PostDetailUiState(scrollToComments = param.scrollToComment),
    )

    private val postId = param.postId

    init {
        loadPostDetails()
    }

    private fun loadPostDetails() {
        viewModelScope.launch(ioDispatcher) {
            postDetailRepository.getPostDetail(postId = postId)
                .onNetworkFailure {
                    postSideEffect(it.toSideEffect())
                }
                .onSuccess {
                    intent {
                        val postDetail = it.toUiModel()
                        val likeReaction = postDetail.content.footerSection.reactions
                            .filterIsInstance<PostDetailUiModel.PostDetailContentUiModel.FooterSectionUiModel.ReactionUiModel.LikeUiModel>()
                            .firstOrNull()
                        val chatReaction = postDetail.content.footerSection.reactions
                            .filterIsInstance<PostDetailUiModel.PostDetailContentUiModel.FooterSectionUiModel.ReactionUiModel.ChatUiModel>()
                            .firstOrNull()
                        val initialLikeCount = likeReaction?.count?.text?.toIntOrNull() ?: 0
                        val initialCommentCount = chatReaction?.count?.text?.toIntOrNull() ?: 0
                        val isLiked = likeReaction?.selected ?: false

                        reduce {
                            state.copy(
                                detail = postDetail,
                                likeCount = initialLikeCount,
                                commentCount = initialCommentCount,
                                isLiked = isLiked,
                            )
                        }
                    }
                }
        }

        viewModelScope.launch(ioDispatcher) {
            postDetailRepository.getPostComments(postId)
                .onNetworkFailure {
                    postSideEffect(it.toSideEffect())
                }
                .onSuccess {
                    intent {
                        reduce {
                            state.copy(comments = it.map { it.toUiModel() })
                        }
                    }
                }
        }
    }

    fun onAction(action: PostDetailAction) {
        intent {
            when (action) {
                is PostDetailAction.OnReactionClick -> {
                    onReactionClicked(action.reaction)
                }

                is PostDetailAction.OnScrapClick -> {
                    onScrapClicked()
                }

                is PostDetailAction.OnNotificationClick -> {
                    onNotificationClicked()
                }

                is PostDetailAction.OnCommentChange -> {
                    reduce {
                        state.copy(commentInput = action.content)
                    }
                }

                is PostDetailAction.OnCommentSend -> {
                    onCommentSend(action.content)
                }

                is PostDetailAction.OnCommentLike -> {
                    onCommentLike(action.commentId)
                }

                is PostDetailAction.OnCommentReport -> {
                    postDetailRepository.reportComment(action.commentId)
                }

                is PostDetailAction.OnCommentDelete -> {
                    onCommentDelete(action.commentId)
                }

                is PostDetailAction.RefreshPost -> {
                    loadPostDetails()
                }

                is PostDetailAction.OnCategoryClick -> {
                    postSideEffect(
                        PostDetailSideEffect.OnCategoryClick(
                            action.target,
                            action.categoryText,
                        ),
                    )
                }

                is PostDetailAction.OnBackClick -> {
                    postSideEffect(PostDetailSideEffect.OnBackClick)
                }

                is PostDetailAction.OnSheetItemClicked -> {
                    onSheetItemClicked(action.option)
                }

                is PostDetailAction.OnMoreOptionClicked -> {
                    reduce {
                        state.copy(showPostOptionsBottomSheet = true)
                    }
                }

                is PostDetailAction.OnDismissPostOptions -> {
                    reduce {
                        state.copy(showPostOptionsBottomSheet = false)
                    }
                }

                is PostDetailAction.OnDismissDeleteDialog -> {
                    reduce {
                        state.copy(showDeleteDialog = false)
                    }
                }

                is PostDetailAction.OnConfirmDelete -> {
                    onConfirmDelete()
                }

                is PostDetailAction.OnDismissBlockDialog -> {
                    reduce {
                        state.copy(showBlockDialog = false)
                    }
                }

                is PostDetailAction.OnConfirmBlock -> {
                    onConfirmBlock()
                }
            }
        }
    }

    private fun onReactionClicked(
        reaction: ReactionUiModel,
    ) =
        intent {
            when (reaction) {
                is ReactionUiModel.LikeUiModel -> {
                    val currentLiked = state.isLiked
                    val currentCount = state.likeCount
                    val newLiked = !currentLiked
                    val newCount = if (newLiked) currentCount + 1 else currentCount - 1

                    reduce {
                        state.copy(
                            isLiked = newLiked,
                            likeCount = newCount,
                        )
                    }

                    val result = communityRepository.onLikeClicked(state.detail.id)
                    if (!result) {
                        reduce {
                            state.copy(
                                isLiked = currentLiked,
                                likeCount = currentCount,
                            )
                        }
                    }
                }

                is ReactionUiModel.ChatUiModel -> {
                }
            }
        }

    private fun onScrapClicked() = intent {
        val currentScrapped = state.isScrapped
        reduce {
            state.copy(isScrapped = !currentScrapped)
        }

        val result = communityRepository.onScrapClicked(state.detail.id)
        if (!result) {
            reduce {
                state.copy(isScrapped = currentScrapped)
            }
        }
    }

    private fun onNotificationClicked() = intent {
        val registered = state.registered
        reduce {
            state.copy(registered = !registered)
        }
        val result = postDetailRepository.registerNotification(state.detail.id)
        if (!result) {
            reduce {
                state.copy(registered = registered)
            }
        }
    }

    private fun onCommentSend(content: String) = intent {
        val postId = state.detail.id.toIntOrNull() ?: return@intent

        viewModelScope.launch(ioDispatcher) {
            val result = postDetailRepository.sendComment(postId, content)
            if (result) {
                intent {
                    reduce {
                        state.copy(commentInput = "")
                    }
                }

                postDetailRepository.getPostComments(state.detail.id)
                    .onSuccess { comments ->
                        intent {
                            reduce {
                                state.copy(
                                    comments = comments.map { it.toUiModel() },
                                    commentCount = comments.size,
                                )
                            }
                        }
                    }
            }
        }
    }

    private fun onCommentLike(commentId: String) = intent {
        val commentIndex = state.comments.indexOfFirst { it.id == commentId }
        if (commentIndex != -1) {
            val comment = state.comments[commentIndex]
            val updatedComment = comment.copy(
                pushedLike = !comment.pushedLike,
                likeCount = if (comment.pushedLike) comment.likeCount - 1 else comment.likeCount + 1,
            )

            reduce {
                state.copy(
                    comments = state.comments.toMutableList().apply {
                        set(commentIndex, updatedComment)
                    },
                )
            }

            val result = postDetailRepository.likeComment(commentId)
            if (!result) {
                reduce {
                    state.copy(
                        comments = state.comments.toMutableList().apply {
                            set(commentIndex, comment)
                        },
                    )
                }
            }
        }
    }

    private fun onSheetItemClicked(item: SheetType) = intent {
        when (item) {
            SheetType.EDIT -> {
                reduce {
                    state.copy(showPostOptionsBottomSheet = false)
                }
                postSideEffect(
                    PostDetailSideEffect.NavigateToEditPost(
                        state.detail.id,
                        state.detail.content,
                    ),
                )
            }

            SheetType.DELETE -> {
                reduce {
                    state.copy(
                        showPostOptionsBottomSheet = false,
                        showDeleteDialog = true,
                    )
                }
            }

            SheetType.REPORT -> {
                reduce {
                    state.copy(showPostOptionsBottomSheet = false)
                }
                postSideEffect(PostDetailSideEffect.NavigateToReportScreen)
            }

            SheetType.BLOCK -> {
                reduce {
                    state.copy(
                        showPostOptionsBottomSheet = false,
                        showBlockDialog = true,
                    )
                }
            }
        }
    }

    private fun onConfirmDelete() = intent {
        reduce {
            state.copy(showDeleteDialog = false)
        }

        viewModelScope.launch(ioDispatcher) {
            postDetailRepository.deletePost(postId)
                .onNetworkFailure {
                    postSideEffect(it.toSideEffect())
                }
                .onSuccess {
                    postSideEffect(PostDetailSideEffect.OnPostDeletedOrBlocked)
                }
        }
    }

    private fun onCommentDelete(commentId: String) = intent {
        viewModelScope.launch(ioDispatcher) {
            postDetailRepository.deleteComment(commentId)
                .onNetworkFailure {
                    postSideEffect(it.toSideEffect())
                }
                .onSuccess {
                    reduce {
                        state.copy(
                            comments = state.comments.filter { it.id != commentId },
                            commentCount = (state.commentCount - 1).coerceAtLeast(0),
                        )
                    }
                }
        }
    }

    private fun onConfirmBlock() = intent {
        reduce {
            state.copy(showBlockDialog = false)
        }

        viewModelScope.launch(ioDispatcher) {
            postDetailRepository.blockPost(postId)
                .onNetworkFailure {
                    postSideEffect(it.toSideEffect())
                }
                .onSuccess {
                    postSideEffect(PostDetailSideEffect.OnPostDeletedOrBlocked)
                }
        }
    }
}
