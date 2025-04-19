package com.bff.wespot.message.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.designsystem.component.indicator.WSToastType
import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.domain.repository.message.MessageStorageRepository
import com.bff.wespot.message.R
import com.bff.wespot.message.common.ALL_MESSAGE_INDEX
import com.bff.wespot.message.common.BOOKMARKED_MESSAGE_INDEX
import com.bff.wespot.message.model.MessageOptionType
import com.bff.wespot.message.state.storage.StorageAction
import com.bff.wespot.message.state.storage.StorageSideEffect
import com.bff.wespot.message.state.storage.StorageUiState
import com.bff.wespot.model.message.response.Message
import com.bff.wespot.ui.base.BaseViewModel
import com.bff.wespot.ui.model.SideEffect
import com.bff.wespot.ui.model.SideEffect.Companion.toSideEffect
import com.bff.wespot.ui.model.ToastState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StorageViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    private val messageStorageRepository: MessageStorageRepository,
) : BaseViewModel(), ContainerHost<StorageUiState, StorageSideEffect> {
    override val container = container<StorageUiState, StorageSideEffect>(StorageUiState())

    fun onAction(action: StorageAction) {
        when (action) {
            StorageAction.OnMessageDeleteButtonClicked -> handleMessageDeleteButtonClicked()
            StorageAction.OnMessageBlockButtonClicked -> handleMessageBlockButtonClicked()
            StorageAction.OnMessageReportButtonClicked -> handleMessageReportButtonClicked()
            is StorageAction.OnStorageChipSelected -> {
                when (action.screenIndex) {
                    ALL_MESSAGE_INDEX -> getMessageList()
                    BOOKMARKED_MESSAGE_INDEX -> getBookmarkedMessageList()
                }
            }
            is StorageAction.OnPushNotificationNavigated -> {
                handleNavigatedByPushNotification(action.messageId)
            }
            is StorageAction.OnMessageClicked -> {
                handleMessageClicked(action.message)
            }
            is StorageAction.OnBookmarkButtonClicked -> {
                handleBookmarkButtonClicked(action.messageId)
            }
            is StorageAction.OnOptionButtonClicked -> {
                handleOptionButtonClicked(action.messageId)
            }
            is StorageAction.OnOptionBottomSheetClicked -> {
                handleOptionBottomSheetClicked(action.messageOptionType)
            }
            StorageAction.OnOptionBottomSheetClosed -> {
                handleOptionBottomSheetClosed()
            }
            StorageAction.OnOptionDialogClosed -> {
                handleOptionDialogClosed()
            }
            StorageAction.OnMessageReportScreenClosed -> {
                handleMessageReportScreenClosed()
            }
        }
    }

    private fun getMessageList() = intent {
        viewModelScope.launch(coroutineDispatcher) {
            val messageList = messageStorageRepository.fetchMessagesStream()
                .cachedIn(viewModelScope)
                .catch { exception ->
                    Timber.e(exception)
                    postSideEffect(SideEffect.toToastEffect())
                }
            reduce {
                state.copy(messageList = messageList)
            }
        }
    }

    private fun getBookmarkedMessageList() = intent {
        viewModelScope.launch(coroutineDispatcher) {
            val messageList = messageStorageRepository.fetchBookmarkedMessagesStream()
                .cachedIn(viewModelScope)
                .catch { exception ->
                    Timber.e(exception)
                    postSideEffect(SideEffect.toToastEffect())
                }
            reduce {
                state.copy(messageList = messageList)
            }
        }
    }

    private fun handleNavigatedByPushNotification(messageId: Int) = intent {
        /*reduce { state.copy(isLoading = true) }

        viewModelScope.launch {
            messageRepository.getMessage(messageId)
                .onSuccess { message ->
                    reduce {
                        state.copy(
                            messageDialogContent = MessageContent(
                                receiver = message.receiver.toDescription(),
                                sender = if (message.isAnonymous) message.senderName else message.sender.name,
                                content = message.content,
                            ),
                        )
                    }

                    if (type == MessageType.RECEIVED && message.isRead.not()) {
                        updateMessageReadStatus(messageId = message.id)
                    }

                    postSideEffect(StorageSideEffect.ShowMessageDialog)
                }.onNetworkFailure {
                    postSideEffect(it.toSideEffect())
                }.also {
                    reduce { state.copy(isLoading = false) }
                }
        }*/
    }

    private fun handleMessageClicked(message: Message) = intent {
    }

    private fun handleBookmarkButtonClicked(messageId: Int) = intent {
        viewModelScope.launch {
            messageStorageRepository.updateMessageBookmarkStatus(messageId)
        }
    }

    private fun handleOptionButtonClicked(messageId: Int) = intent {
        reduce {
            state.copy(
                optionButtonClickedMessageId = messageId,
            )
        }
        postSideEffect(StorageSideEffect.ShowOptionBottomSheet)
    }

    private fun handleOptionBottomSheetClicked(messageOptionType: MessageOptionType) = intent {
        reduce {
            state.copy(messageOptionType = messageOptionType)
        }
        postSideEffect(StorageSideEffect.ShowOptionDialog)
    }

    private fun updateMessageReadStatus(messageId: Int) {
        viewModelScope.launch {
            messageStorageRepository.updateMessageReadStatus(messageId)
        }
    }

    private fun handleMessageDeleteButtonClicked() = intent {
        postSideEffect(StorageSideEffect.CloseOptionDialog)
        postSideEffect(StorageSideEffect.CloseOptionBottomSheet)

        viewModelScope.launch {
            messageStorageRepository.deleteMessage(state.optionButtonClickedMessageId)
                .onSuccess {
                    postSideEffect(
                        StorageSideEffect.ShowToast(
                            ToastState(
                                show = true,
                                message = R.string.delete_done,
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

    private fun handleMessageBlockButtonClicked() = intent {
        postSideEffect(StorageSideEffect.CloseOptionDialog)
        postSideEffect(StorageSideEffect.CloseOptionBottomSheet)

        viewModelScope.launch {
            messageStorageRepository.blockMessage(state.optionButtonClickedMessageId)
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

    private fun handleMessageReportButtonClicked() = intent {
        postSideEffect(StorageSideEffect.CloseOptionDialog)
        postSideEffect(StorageSideEffect.CloseOptionBottomSheet)
        postSideEffect(StorageSideEffect.ShowReportMessageScreen)
    }

    private fun handleOptionBottomSheetClosed() = intent {
        postSideEffect(StorageSideEffect.CloseOptionBottomSheet)
    }

    private fun handleOptionDialogClosed() = intent {
        postSideEffect(StorageSideEffect.CloseOptionDialog)
    }

    private fun handleMessageReportScreenClosed() = intent {
        postSideEffect(StorageSideEffect.CloseReportMessageScreen)
    }
}
