package com.bff.wespot.data.repository.message

import com.bff.wespot.data.mapper.message.toDto
import com.bff.wespot.data.remote.source.message.MessageDataSource
import com.bff.wespot.domain.repository.DataStoreRepository
import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.model.message.request.SendMessage
import com.bff.wespot.model.message.response.MessageStatus
import com.bff.wespot.domain.util.DataStoreKey
import com.bff.wespot.model.message.response.Message
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import com.bff.wespot.model.message.response.SenderProfile
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val messageDataSource: MessageDataSource,
    private val dataStoreRepository: DataStoreRepository,
) : MessageRepository {
    override suspend fun postMessage(sendMessage: SendMessage): Result<Unit> =
        messageDataSource.postMessage(sendMessage.toDto())

    override suspend fun getSenderProfileList(receiverId: Int): Result<List<SenderProfile>> =
        messageDataSource.getSenderProfileList(receiverId).mapCatching { data ->
            data.map { it.toDomain() }
        }

    override suspend fun getMessageStatus(): Result<MessageStatus> {
        return messageDataSource.getMessageStatus().mapCatching { messageStatusDto ->
            messageStatusDto.toMessageStatus()
        }
    }

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
