package com.bff.wespot.data.remote.source.message

import com.bff.wespot.data.remote.model.message.request.MessageReplyDto
import com.bff.wespot.data.remote.model.message.request.SendMessageDto
import com.bff.wespot.data.remote.model.message.response.BlockedMessageListDto
import com.bff.wespot.data.remote.model.message.response.MessageDetailDto
import com.bff.wespot.data.remote.model.message.response.MessageHomeTitleDto
import com.bff.wespot.data.remote.model.message.response.MessageStatusDto
import com.bff.wespot.data.remote.model.message.response.SenderProfileDto
import com.bff.wespot.network.extensions.safeRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import io.ktor.http.path
import javax.inject.Inject

class MessageDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient,
): MessageDataSource {
    override suspend fun getSenderProfileList(receiverId: Int): Result<List<SenderProfileDto>> =
        httpClient.safeRequest {
            method = HttpMethod.Get
            url {
                path("api/v1/messages/receiver/$receiverId/profiles")
            }
        }

    override suspend fun postMessage(
        sendMessage: SendMessageDto,
    ): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Post
                path("api/v2/messages")
                setBody(sendMessage)
            }
        }

    override suspend fun getMessageStatus(): Result<MessageStatusDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v2/messages/status")
            }
        }

    override suspend fun getMessage(messageId: Int): Result<MessageDetailDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/messages/$messageId")
            }
        }

    override suspend fun getBlockedMessage(cursorId: Int?): Result<BlockedMessageListDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v1/messages/blocked")
                parameter("cursorId", cursorId)
            }
        }

    override suspend fun getMessageHomeTitle(): Result<MessageHomeTitleDto> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Get
                path("api/v2/messages/title")
            }
        }

    override suspend fun replyMessage(roomId: Int, reply: MessageReplyDto): Result<Unit> =
        httpClient.safeRequest {
            url {
                method = HttpMethod.Post
                path("/api/v2/messages/${roomId}/answer")
            }
            setBody(reply)
        }
}
