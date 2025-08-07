package com.bff.wespot.community.detail

import androidx.lifecycle.viewModelScope
import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.community.detail.state.PostDetailAction
import com.bff.wespot.community.detail.state.PostDetailParams
import com.bff.wespot.community.detail.state.PostDetailSideEffect
import com.bff.wespot.community.detail.state.PostDetailUiState
import com.bff.wespot.community.uimodel.PostCommentUiModel.Companion.toUiModel
import com.bff.wespot.community.uimodel.PostDetailUiModel.Companion.toUiModel
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
    ioDispatcher: CoroutineDispatcher,
) : BaseViewModel(), ContainerHost<PostDetailUiState, PostDetailSideEffect> {
    override val container = container<PostDetailUiState, PostDetailSideEffect>(
        PostDetailUiState(),
    )

    init {
        viewModelScope.launch(ioDispatcher) {
            postDetailRepository.getPostDetail(postId = param.postId)
                .onNetworkFailure {
                    postSideEffect(it.toSideEffect())
                }
                .onSuccess {
                    intent {
                        reduce {
                            state.copy(detail = it.toUiModel())
                        }
                    }
                }
        }

        viewModelScope.launch(ioDispatcher) {
            postDetailRepository.getPostComments(param.postId)
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
                    when (action.reaction) {
                        "like" -> {
                            val currentLiked = state.isLiked
                            reduce {
                                state.copy(isLiked = !currentLiked)
                            }

                            val result = communityRepository.onLikeClicked(state.detail.id)
                            if (!result) {
                                reduce {
                                    state.copy(isLiked = currentLiked)
                                }
                            }
                        }
                    }
                }

                is PostDetailAction.OnScrapClick -> {
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

                is PostDetailAction.OnNotificationClick -> {
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

                is PostDetailAction.OnCommentChange -> {
                    reduce {
                        state.copy(commentInput = action.content)
                    }
                }

                is PostDetailAction.OnCommentSend -> {
                    val postId = state.detail.id.toIntOrNull() ?: return@intent
                    val result = postDetailRepository.sendComment(postId, action.content)
                    if (result) {
                        reduce {
                            state.copy(commentInput = "")
                        }
                    }
                }

                is PostDetailAction.OnCommentLike -> {
                    val commentIndex = state.comments.indexOfFirst { it.id == action.commentId }
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

                        val result = postDetailRepository.likeComment(action.commentId)
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

                is PostDetailAction.OnCommentReport -> {
                    postDetailRepository.reportComment(action.commentId)
                }

                is PostDetailAction.OnEditPost -> {
                    postSideEffect(
                        PostDetailSideEffect.NavigateToEditPost(
                            state.detail.id,
                            state.detail.content,
                        ),
                    )
                }

                else -> {}
            }
        }
    }
}
