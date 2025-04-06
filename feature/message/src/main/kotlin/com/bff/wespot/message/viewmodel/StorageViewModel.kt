package com.bff.wespot.message.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.designsystem.component.indicator.WSToastType
import com.bff.wespot.domain.repository.BasePagingRepository
import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.domain.repository.message.MessageStorageRepository
import com.bff.wespot.message.R
import com.bff.wespot.message.model.MessageOptionType
import com.bff.wespot.message.state.storage.StorageAction
import com.bff.wespot.message.state.storage.StorageSideEffect
import com.bff.wespot.message.state.storage.StorageUiState
import com.bff.wespot.model.common.Paging
import com.bff.wespot.model.message.request.MessageType
import com.bff.wespot.model.message.response.MessageContent
import com.bff.wespot.model.message.response.ReceivedMessage
import com.bff.wespot.model.message.response.SentMessage
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
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StorageViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    private val messageStorageRepository: MessageStorageRepository,
    private val messageSentRepository: BasePagingRepository<SentMessage, Paging<SentMessage>>,
) : BaseViewModel(), ContainerHost<StorageUiState, StorageSideEffect> {
    override val container = container<StorageUiState, StorageSideEffect>(StorageUiState())

    fun onAction(action: StorageAction) {
        when (action) {
            StorageAction.OnMessageDeleteButtonClicked -> handleMessageDeleteButtonClicked()
            StorageAction.OnMessageBlockButtonClicked -> handleMessageBlockButtonClicked()
            StorageAction.OnMessageReportButtonClicked -> handleMessageReportButtonClicked()
            is StorageAction.OnStorageChipSelected -> {
                when (action.messageType) {
                    MessageType.SENT -> getSentMessageList()
                    MessageType.RECEIVED -> getReceivedMessageList()
                }
            }
            is StorageAction.OnPushNotificationNavigated -> {
                handleNavigatedByPushNotification(action.messageId, action.type)
            }
            is StorageAction.OnSentMessageClicked -> {
                handleSentMessageClicked(action.message)
            }
            is StorageAction.OnReceivedMessageClicked -> {
                handleReceivedMessageClicked(action.message)
            }
            is StorageAction.OnOptionButtonClicked -> {
                handleOptionButtonClicked(action.messageId, action.messageType)
            }
            is StorageAction.OnOptionBottomSheetClicked -> {
                handleOptionBottomSheetClicked(action.messageOptionType)
            }
        }
    }

    private fun getReceivedMessageList() = intent {
        viewModelScope.launch(coroutineDispatcher) {
            runCatching {
                reduce {
                    state.copy(
                        receivedMessageList = messageStorageRepository.fetchReceivedMessageStream()
                            .cachedIn(viewModelScope),
                    )
                }
            }.onFailure { exception ->
                Timber.e(exception)
            }
        }
    }

    private fun getSentMessageList() = intent {
        viewModelScope.launch(coroutineDispatcher) {
            runCatching {
                reduce {
                    state.copy(
                        sentMessageList = messageSentRepository.fetchResultStream()
                            .cachedIn(viewModelScope),
                    )
                }
            }.onFailure { exception ->
                Timber.e(exception)
            }
        }
    }

    private fun handleNavigatedByPushNotification(messageId: Int, type: MessageType) = intent {
        reduce { state.copy(isLoading = true) }

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
        }
    }

    private fun handleSentMessageClicked(message: SentMessage) = intent {
        reduce {
            state.copy(
                messageDialogContent = MessageContent(
                    receiver = message.receiver.toDescription(),
                    sender = if (message.isAnonymous) message.senderName else message.sender.name,
                    content = message.content,
                ),
            )
        }
    }

    private fun handleReceivedMessageClicked(message: ReceivedMessage) = intent {
        reduce {
            state.copy(
                messageDialogContent = MessageContent(
                    receiver = message.receiver.toDescription(),
                    sender = if (message.isAnonymous) message.senderName else message.sender.name,
                    content = message.content,
                ),
            )
        }

        if (message.isRead.not()) {
            updateMessageReadStatus(messageId = message.id)
        }
    }

    private fun handleOptionButtonClicked(messageId: Int, messageType: MessageType) = intent {
        reduce {
            state.copy(
                optionButtonClickedMessageId = messageId,
                optionButtonClickedMessageType = messageType,
            )
        }
    }

    private fun handleOptionBottomSheetClicked(messageOptionType: MessageOptionType) = intent {
        reduce {
            state.copy(messageOptionType = messageOptionType)
        }
    }

    private fun updateMessageReadStatus(messageId: Int) {
        viewModelScope.launch {
            messageStorageRepository.updateMessageReadStatus(messageId)
        }
    }

    private fun handleMessageDeleteButtonClicked() = intent {
        viewModelScope.launch {
            messageStorageRepository.deleteMessage(state.optionButtonClickedMessageId)
                .onSuccess {
                    if (state.optionButtonClickedMessageType == MessageType.SENT) {
                        getSentMessageList()
                    }

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
        postSideEffect(StorageSideEffect.ShowReportMessageScreen)
    }
}
