package com.bff.wespot.message.viewmodel

import androidx.lifecycle.viewModelScope
import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.designsystem.component.indicator.WSToastType
import com.bff.wespot.domain.repository.message.MessageStorageRepository
import com.bff.wespot.message.R
import com.bff.wespot.message.common.ALL_MESSAGE_INDEX
import com.bff.wespot.message.common.BOOKMARKED_MESSAGE_INDEX
import com.bff.wespot.message.state.storage.StorageAction
import com.bff.wespot.message.state.storage.StorageSideEffect
import com.bff.wespot.message.state.storage.StorageUiState
import com.bff.wespot.model.message.response.Message
import com.bff.wespot.ui.base.BaseViewModel
import com.bff.wespot.ui.model.SideEffect.Companion.toSideEffect
import com.bff.wespot.ui.model.ToastState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class StorageViewModel @Inject constructor(
    private val messageStorageRepository: MessageStorageRepository,
) : BaseViewModel(), ContainerHost<StorageUiState, StorageSideEffect> {
    override val container = container<StorageUiState, StorageSideEffect>(StorageUiState())

    fun onAction(action: StorageAction) {
        when (action) {
            is StorageAction.OnStorageChipSelected -> {
                when (action.screenIndex) {
                    ALL_MESSAGE_INDEX -> getMessageList()
                    BOOKMARKED_MESSAGE_INDEX -> getBookmarkedMessageList()
                }
            }
            is StorageAction.OnPushNotificationNavigated -> {
                // TODO Handle Push Message
            }
            is StorageAction.OnMessageClicked -> {
                handleMessageClicked(action.message)
            }
            is StorageAction.OnOptionButtonClicked -> {
                handleOptionButtonClicked(action.message)
            }
            StorageAction.OnBlockBottomSheetItemClicked -> {
                handleBlockBottomSheetItemClicked()
            }
            StorageAction.OnBookmarkBottomSheetItemClicked -> {
                handleBookmarkBottomSheetItemClicked()
            }
            StorageAction.OnBlockButtonClicked -> {
                handleBlockButtonClicked()
            }
            StorageAction.OnBlockDialogClosed -> {
                handleBlockDialogClosed()
            }
            StorageAction.OnOptionBottomSheetClosed -> {
                handleOptionBottomSheetClosed()
            }
        }
    }

    private fun getMessageList() = intent {
        reduce { state.copy(isLoading = true) }
        viewModelScope.launch {
            messageStorageRepository.getMessages()
                .onSuccess {
                    reduce {
                        state.copy(messageList = it)
                    }
                }
                .onNetworkFailure {
                    postSideEffect(it.toSideEffect())
                }
                .also {
                    reduce { state.copy(isLoading = false) }
                }
        }
    }

    private fun getBookmarkedMessageList() = intent {
        reduce { state.copy(isLoading = true) }
        viewModelScope.launch {
            messageStorageRepository.getBookmarkedMessages()
                .onSuccess {
                    reduce {
                        state.copy(
                            messageList = it,
                            showEmptyBookmarkScreen = it.isEmpty(),
                        )
                    }
                }
                .onNetworkFailure {
                    postSideEffect(it.toSideEffect())
                }
                .also {
                    reduce { state.copy(isLoading = false) }
                }
        }
    }

    private fun handleMessageClicked(message: Message) = intent {
        postSideEffect(StorageSideEffect.NavigateToMessageRoom(message))
    }

    private fun handleOptionButtonClicked(message: Message) = intent {
        reduce {
            state.copy(optionButtonClickedMessage = message)
        }
        postSideEffect(StorageSideEffect.ShowOptionBottomSheet)
    }

    private fun handleBlockBottomSheetItemClicked() = intent {
        postSideEffect(StorageSideEffect.ShowBlockDialog)
        postSideEffect(StorageSideEffect.CloseOptionBottomSheet)
    }

    private fun handleBookmarkBottomSheetItemClicked() = intent {
        viewModelScope.launch {
            val message = state.optionButtonClickedMessage

            messageStorageRepository.updateMessageBookmarkStatus(messageId = message.id)
            val updatedMessageList = state.messageList.map {
                if (message.id == it.id) {
                    message.copy(isBookmarked = it.isBookmarked.not())
                } else {
                    message
                }
            }
            reduce {
                state.copy(messageList = updatedMessageList)
            }
        }
        postSideEffect(StorageSideEffect.CloseOptionBottomSheet)
    }

    private fun handleOptionBottomSheetClosed() = intent {
        postSideEffect(StorageSideEffect.CloseOptionBottomSheet)
    }

    private fun handleBlockButtonClicked() = intent {
        postSideEffect(StorageSideEffect.CloseBlockDialog)

        viewModelScope.launch {
            messageStorageRepository.blockMessage(state.optionButtonClickedMessage.id)
                .onSuccess {
                    postSideEffect(
                        StorageSideEffect.ShowToast(
                            ToastState(
                                show = true,
                                message = R.string.block_done,
                                type = WSToastType.Success,
                            ),
                        ),
                    )
                }
                .onNetworkFailure {
                    postSideEffect(it.toSideEffect())
                }
        }
    }

    private fun handleBlockDialogClosed() = intent {
        postSideEffect(StorageSideEffect.CloseBlockDialog)
    }
}
