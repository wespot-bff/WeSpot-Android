package com.bff.wespot.message.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.designsystem.component.indicator.WSToastType
import com.bff.wespot.domain.repository.BasePagingRepository
import com.bff.wespot.domain.repository.CommonRepository
import com.bff.wespot.domain.repository.firebase.config.RemoteConfigRepository
import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.domain.repository.message.MessageStorageRepository
import com.bff.wespot.domain.util.RemoteConfigKey
import com.bff.wespot.message.R
import com.bff.wespot.message.model.MessageOptionType
import com.bff.wespot.message.model.TimePeriod
import com.bff.wespot.message.model.getCurrentTimePeriod
import com.bff.wespot.message.state.storage.StorageAction
import com.bff.wespot.message.state.storage.StorageSideEffect
import com.bff.wespot.message.state.storage.StorageUiState
import com.bff.wespot.model.common.Paging
import com.bff.wespot.model.common.ReportType
import com.bff.wespot.model.message.request.MessageType
import com.bff.wespot.model.message.response.BaseMessage
import com.bff.wespot.model.message.response.Message
import com.bff.wespot.model.message.response.ReceivedMessage
import com.bff.wespot.ui.base.BaseViewModel
import com.bff.wespot.ui.model.SideEffect.Companion.toSideEffect
import com.bff.wespot.ui.model.ToastState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StorageViewModel @Inject constructor(
    remoteConfigRepository: RemoteConfigRepository,
    private val messageRepository: MessageRepository,
    private val messageStorageRepository: MessageStorageRepository,
    private val commonRepository: CommonRepository,
    private val messageReceivedRepository: BasePagingRepository<ReceivedMessage, Paging<ReceivedMessage>>,
    private val messageSentRepository: BasePagingRepository<Message, Paging<Message>>,
) : BaseViewModel(), ContainerHost<StorageUiState, StorageSideEffect> {
    override val container = container<StorageUiState, StorageSideEffect>(
        StorageUiState(
            messageStartTime = remoteConfigRepository.fetchFromRemoteConfig(
                RemoteConfigKey.MESSAGE_START_TIME,
            ),
            messageReceiveTime = remoteConfigRepository.fetchFromRemoteConfig(
                RemoteConfigKey.MESSAGE_RECEIVE_TIME,
            ),
        ),
    )

    private val timePeriodCheckJob: Job = viewModelScope.launch(start = CoroutineStart.LAZY) {
        withContext(coroutineDispatcher) {
            while (isActive) {
                checkTimePeriodEveningToNight()
                delay(10000)
            }
        }
    }

    fun onAction(action: StorageAction) {
        when (action) {
            StorageAction.StartTimeTracking -> startTimePeriodChecker()
            StorageAction.CancelTimeTracking -> cancelTimePeriodChecker()
            StorageAction.OnMessageDeleteButtonClicked -> {
                handleDeleteMessageButtonClicked()
            }
            StorageAction.OnMessageReportButtonClicked -> {
                handleReportMessageButtonClicked()
            }
            StorageAction.OnMessageBlockButtonClicked -> {
                handleBlockMessageButtonClicked()
            }
            is StorageAction.OnStorageChipSelected -> {
                when (action.messageType) {
                    MessageType.SENT -> {
                        getMessageStatus()
                        getSentMessageList()
                    }

                    MessageType.RECEIVED -> getReceivedMessageList()
                }
            }
            is StorageAction.OnMessageStorageScreenOpened -> {
                handleMessageStorageScreenOpened(action.messageId, action.type)
            }
            is StorageAction.OnSentMessageClicked -> {
                handleMessageClicked(action.message, MessageType.SENT)
            }
            is StorageAction.OnReceivedMessageClicked -> {
                handleMessageClicked(action.message, MessageType.RECEIVED)
            }
            is StorageAction.OnOptionButtonClicked -> {
                handleOptionButtonClicked(action.messageId, action.messageType)
            }
            is StorageAction.OnOptionBottomSheetClicked -> {
                handleOptionBottomSheetClicked(action.messageOptionType)
            }
        }
    }

    private fun startTimePeriodChecker() {
        if (timePeriodCheckJob.isActive.not()) {
            timePeriodCheckJob.start()
        }
    }

    private fun cancelTimePeriodChecker() {
        timePeriodCheckJob.cancel()
    }

    private fun checkTimePeriodEveningToNight() = intent {
        val timePeriod = getCurrentTimePeriod(
            messageStartTime = state.messageStartTime,
            messageReceiveTime = state.messageReceiveTime,
        )
        reduce {
            state.copy(isTimePeriodEveningToNight = timePeriod == TimePeriod.EVENING_TO_NIGHT)
        }
    }

    private fun getMessageStatus() = intent {
        viewModelScope.launch {
            messageRepository.getMessageStatus()
                .onSuccess { messageStatus ->
                    reduce { state.copy(messageStatus = messageStatus) }
                }
        }
    }

    private fun getReceivedMessageList() = intent {
        viewModelScope.launch(coroutineDispatcher) {
            runCatching {
                reduce {
                    state.copy(
                        receivedMessageList = messageReceivedRepository.fetchResultStream()
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

    private fun handleMessageStorageScreenOpened(messageId: Int, type: MessageType) = intent {
        reduce { state.copy(isLoading = true) }
        viewModelScope.launch {
            messageRepository.getMessage(messageId)
                .onSuccess { message ->
                    handleMessageClicked(message, type)
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(StorageSideEffect.ShowMessageDialog)
                }
                .onNetworkFailure {
                    postSideEffect(it.toSideEffect())
                }
                .onFailure {
                    reduce { state.copy(isLoading = false) }
                }
        }
    }

    private fun handleMessageClicked(message: BaseMessage, type: MessageType) = intent {
        reduce {
            state.copy(clickedMessage = message)
        }

        if (type == MessageType.RECEIVED && message.isRead.not()) {
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
                .onSuccess {
                    getReceivedMessageList()
                }
        }
    }

    private fun handleDeleteMessageButtonClicked() = intent {
        viewModelScope.launch {
            messageStorageRepository.deleteMessage(state.optionButtonClickedMessageId)
                .onSuccess {
                    when (state.optionButtonClickedMessageType) {
                        MessageType.RECEIVED -> getReceivedMessageList()
                        MessageType.SENT -> getSentMessageList()
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

    private fun handleReportMessageButtonClicked() = intent {
        viewModelScope.launch {
            commonRepository.sendReport(ReportType.MESSAGE, state.optionButtonClickedMessageId)
                .onSuccess {
                    postSideEffect(
                        StorageSideEffect.ShowToast(
                            ToastState(
                                show = true,
                                message = R.string.report_done,
                                type = WSToastType.Success,
                            ),
                        ),
                    )
                    getReceivedMessageList()
                }
                .onNetworkFailure {
                    postSideEffect(it.toSideEffect())
                }
        }
    }

    private fun handleBlockMessageButtonClicked() = intent {
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
                    getReceivedMessageList()
                }
                .onNetworkFailure {
                    postSideEffect(it.toSideEffect())
                }
        }
    }
}
