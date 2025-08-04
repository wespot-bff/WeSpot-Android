package com.bff.wespot.community.detail

import androidx.lifecycle.viewModelScope
import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.community.detail.state.PostDetailParams
import com.bff.wespot.community.detail.state.PostDetailSideEffect
import com.bff.wespot.community.detail.state.PostDetailUiState
import com.bff.wespot.community.uimodel.PostCommentUiModel.Companion.toUiModel
import com.bff.wespot.community.uimodel.PostDetailUiModel.Companion.toUiModel
import com.bff.wespot.domain.repository.community.PostDetailRepository
import com.bff.wespot.ui.base.BaseViewModel
import com.bff.wespot.ui.model.SideEffect.Companion.toSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    param: PostDetailParams,
    private val postDetailRepository: PostDetailRepository,
) : BaseViewModel(), ContainerHost<PostDetailUiState, PostDetailSideEffect> {
    override val container = container<PostDetailUiState, PostDetailSideEffect>(
        PostDetailUiState(),
    )

    init {
        viewModelScope.launch(coroutineDispatcher) {
            launch {
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

            launch {
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
    }
}
