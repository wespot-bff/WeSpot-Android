package com.bff.wespot.data.local.model.message

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bff.wespot.common.util.toISOLocalDateTime
import com.bff.wespot.model.message.response.Message
import com.bff.wespot.model.message.response.MessageProfile

@Entity(tableName = "message")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val number: Int = 0,
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "sender_profile") val senderProfile: MessageProfile,
    @ColumnInfo(name = "receiver_profile") val receiverProfile: MessageProfile,
    @ColumnInfo(name = "is_exists_unread_message") val isExistsUnreadMessage: Boolean,
    @ColumnInfo(name = "latest_chat_time") val latestChatTime: String,
    @ColumnInfo(name = "is_anonymous") val isAnonymous: Boolean,
    @ColumnInfo(name = "is_me_message_room_owner") val isMeMessageRoomOwner: Boolean,
    @ColumnInfo(name = "is_bookmarked") val isBookmarked: Boolean,
    @ColumnInfo(name = "is_blocked") val isBlocked: Boolean,
    @ColumnInfo(name = "is_ever") val isEver: Boolean,
    @ColumnInfo("last_cursor_id") val lastCursorId: Int?,
) {
    fun toDomain() = Message(
        id = id,
        senderProfile = senderProfile,
        receiverProfile = receiverProfile,
        isExistsUnreadMessage = isExistsUnreadMessage,
        isMeMessageRoomOwner = isMeMessageRoomOwner,
        latestChatTime = latestChatTime.toISOLocalDateTime(),
        isBookmarked = isBookmarked,
        isBlocked = isBlocked,
        isEver = isEver,
    )
}
