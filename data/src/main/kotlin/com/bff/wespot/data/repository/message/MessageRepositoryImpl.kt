package com.bff.wespot.data.repository.message

import com.bff.wespot.data.mapper.message.toWrittenMessageDto
import com.bff.wespot.data.remote.source.message.MessageDataSource
import com.bff.wespot.domain.repository.DataStoreRepository
import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.domain.util.DataStoreKey
import com.bff.wespot.model.message.request.WrittenMessage
import com.bff.wespot.model.message.response.Message
import com.bff.wespot.model.message.response.MessageStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val messageDataSource: MessageDataSource,
    private val dataStoreRepository: DataStoreRepository,
) : MessageRepository {
    override suspend fun postMessage(writtenMessage: WrittenMessage): Result<String> {
        return messageDataSource.postMessage(writtenMessage.toWrittenMessageDto()).mapCatching {
            it.toString()
        }
    }

    override suspend fun getMessageStatus(): Result<MessageStatus> {
        return messageDataSource.getMessageStatus().mapCatching { messageStatusDto ->
            messageStatusDto.toMessageStatus()
        }
    }

    override suspend fun editMessage(messageId: Int, writtenMessage: WrittenMessage): Result<Unit> =
        messageDataSource.editMessage(messageId, writtenMessage.toWrittenMessageDto())

    override suspend fun getMessage(messageId: Int): Result<Message> =
        messageDataSource.getMessage(messageId).mapCatching { messageDto ->
            messageDto.toMessage()
        }

    override suspend fun getMessageHomeTitle(): String? {
        checkAndCacheMessageHomeTitle()
        return dataStoreRepository.getString(DataStoreKey.MESSAGE_HOME_TITLE).firstOrNull()
    }

    private fun checkAndCacheMessageHomeTitle() {
        CoroutineScope(dispatcher).launch {
            val cachedTimeString = dataStoreRepository.getString(
                DataStoreKey.MESSAGE_HOME_TITLE_CACHED_TIME,
            ).firstOrNull()
            val cachedTime = cachedTimeString?.toLongOrNull() ?: 0L
            val currentTime = System.currentTimeMillis()

            /** 아직 캐싱 유효 시간이 지나지 않았다면 바로 반환한다. */
            if ((currentTime - cachedTime) <= CACHE_EXPIRATION_TIME_MILLIS) return@launch

            messageDataSource.getMessageHomeTitle()
                .onSuccess {
                    dataStoreRepository.saveString(DataStoreKey.MESSAGE_HOME_TITLE, it.title)
                    dataStoreRepository.saveString(
                        DataStoreKey.MESSAGE_HOME_TITLE_CACHED_TIME,
                        currentTime.toString()
                    )
                }
        }
    }

    companion object {
        private const val CACHE_EXPIRATION_TIME_MILLIS = 2 * 3600 * 1000L  // 2시간
    }
}
