package com.bff.wespot.data.local.model.message

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bff.wespot.common.util.toISOLocalDateTime
import com.bff.wespot.model.message.response.ReceivedMessage
import com.bff.wespot.model.user.response.User

@Entity(tableName = "received_message")
data class ReceivedMessageEntity(
    @PrimaryKey(autoGenerate = true) val number: Int = 0,
    val id: Int,
    val sender: User,
    @ColumnInfo("sender_name") val senderName: String,
    val receiver: User,
    val content: String,
    @ColumnInfo("received_at") val receivedAt: String,
    @ColumnInfo("is_read") val isRead: Boolean,
    @ColumnInfo("is_anonymous") val isAnonymous: Boolean,
    @ColumnInfo("read_at") val readAt: String,
    @ColumnInfo("last_cursor_id") val lastCursorId: Int?,
) {
    fun toDomain() = ReceivedMessage(
        id = id,
        senderName = senderName,
        receiver = receiver,
        content = content,
        sender = sender,
        receivedAt = receivedAt.toISOLocalDateTime(),
        isRead = isRead,
        isAnonymous = isAnonymous,
        readAt = readAt.toISOLocalDateTime(),
    )
}
