package com.bff.wespot.community.write

import androidx.lifecycle.viewModelScope
import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.community.write.state.WritePostAction
import com.bff.wespot.community.write.state.WritePostParams
import com.bff.wespot.community.write.state.WritePostSideEffect
import com.bff.wespot.community.write.state.WritePostUiState
import com.bff.wespot.domain.repository.CommonRepository
import com.bff.wespot.domain.repository.community.WritePostRepository
import com.bff.wespot.model.community.PostInfo
import com.bff.wespot.model.community.chip.CategoryItem
import com.bff.wespot.model.exception.NetworkException
import com.bff.wespot.ui.base.BaseViewModel
import com.bff.wespot.ui.model.SideEffect.Companion.toSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
internal class WritePostViewModel @Inject constructor(
    private val writePostRepository: WritePostRepository,
    private val commonRepository: CommonRepository,
    private val params: WritePostParams,
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
                .onSuccess { categories ->
                    reduce {
                        val updatedState = state.copy(categories = categories)

                        if (params.isEditing) {
                            val selectedCategory = categories
                                .flatMap { it.chips }
                                .find { it.text == params.category }
                                ?: CategoryItem.EMPTY

                            updatedState.copy(
                                title = params.title,
                                description = params.description,
                                images = params.images,
                                selectedCategory = selectedCategory,
                            )
                        } else {
                            updatedState
                        }
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

                is WritePostAction.UploadPost -> {
                    handlePostCreate()
                }

                is WritePostAction.ClosePage -> {
                    closePage(action.force)
                }

                is WritePostAction.CloseDialog -> {
                    intent {
                        reduce { state.copy(showWarning = false) }
                    }
                }
            }
        }
    }

    private fun handlePostCreate() = intent {
        viewModelScope.launch {
            reduce { state.copy(isLoading = true) }

            val images = state.images
            if (images.isEmpty()) {
                uploadPost(emptyList())
                return@launch
            }

            val uploadedImages = images.mapNotNull {
                uploadImage(it).getOrNull()
            }

            uploadPost(uploadedImages)
        }
    }

    private suspend fun uploadImage(profilePath: String): Result<String> =
        runCatching {
            commonRepository.uploadImage(profilePath)
        }.mapCatching { uploadResult ->
            if (!uploadResult.isSuccess) throw NetworkException()
            uploadResult.getOrThrow()
        }

    private fun uploadPost(urls: List<String>) = intent {
        runCatching {
            val result = writePostRepository.createPost(
                info = PostInfo(
                    categoryId = state.selectedCategory.id,
                    title = state.title,
                    description = state.description,
                    imagesRequest = urls,
                ),
            )

            if (result) {
                postSideEffect(WritePostSideEffect.ClosePage)
            }
        }.onNetworkFailure {
            postSideEffect(it.toSideEffect())
        }.also {
            reduce { state.copy(isLoading = false) }
        }
    }

    private fun closePage(force: Boolean) = intent {
        if (state.haveContent && !force) {
            reduce {
                state.copy(showWarning = true)
            }
        } else {
            postSideEffect(WritePostSideEffect.ClosePage)
        }
    }
}
