package com.bff.wespot.data.repository.message

import com.bff.wespot.data.remote.model.message.request.MessageUsageDto
import com.bff.wespot.data.remote.source.message.MessageSettingDataSource
import com.bff.wespot.domain.repository.message.MessageSettingRepository
import com.bff.wespot.model.message.response.Message
import javax.inject.Inject

class MessageSettingRepositoryImpl @Inject constructor(
    private val dataSource: MessageSettingDataSource,
) : MessageSettingRepository {
    override suspend fun getBlockedMessageList(): Result<List<Message>> =
        dataSource.getBlockedMessageList().mapCatching { messageList ->
            messageList.map { response ->
                response.toMessage()
            }
        }

    override suspend fun updateMessageBlockStatus(messageId: Int): Result<Unit> =
        dataSource.updateMessageBlockStatus(messageId)

    override suspend fun updateMessageUsageStatus(enabled: Boolean): Result<Unit> =
        dataSource.updateMessageUsageStatus(MessageUsageDto(enabled))
}
