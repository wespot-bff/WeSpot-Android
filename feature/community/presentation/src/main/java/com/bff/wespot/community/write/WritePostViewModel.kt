package com.bff.wespot.community.write

import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.community.write.state.WritePostAction
import com.bff.wespot.community.write.state.WritePostSideEffect
import com.bff.wespot.community.write.state.WritePostUiState
import com.bff.wespot.domain.repository.community.WritePostRepository
import com.bff.wespot.ui.base.BaseViewModel
import com.bff.wespot.ui.model.SideEffect.Companion.toSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
internal class WritePostViewModel @Inject constructor(
    private val writePostRepository: WritePostRepository,
) : BaseViewModel(), ContainerHost<WritePostUiState, WritePostSideEffect> {
    override val container = container<WritePostUiState, WritePostSideEffect>(
        WritePostUiState(),
    )

    init {
        intent {
            writePostRepository.getCategories()
                .onNetworkFailure {
                    postSideEffect(it.toSideEffect())
                }
                .onSuccess {
                    reduce {
                        state.copy(
                            categories = it,
                        )
                    }
                }
        }
    }

    fun onAction(action: WritePostAction) {
        intent {
            when (action) {
                is WritePostAction.OnTitleChanged -> {
                    reduce {
                        state.copy(title = action.title)
                    }
                }

                is WritePostAction.OnDescriptionChanged -> {
                    if (action.description.length > 1200) {
                        return@intent
                    }

                    reduce {
                        state.copy(description = action.description)
                    }
                }

                is WritePostAction.OnImageChanged -> {
                    reduce {
                        state.copy(images = action.images)
                    }
                }

                is WritePostAction.OnImageDelete -> {
                    reduce {
                        state.copy(
                            images = state.images.filter { it != action.image },
                        )
                    }
                }

                is WritePostAction.OnCategoryChanged -> {
                    reduce {
                        state.copy(
                            selectedCategory = action.category,
                        )
                    }
                }
            }
        }
    }
}
