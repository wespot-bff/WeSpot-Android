package com.bff.wespot.data.local.model.message

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bff.wespot.common.util.toISOLocalDateTime
import com.bff.wespot.model.message.response.Message

@Entity(tableName = "message")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val number: Int = 0,
    val id: Int,
    val thumbnail: String,
    @ColumnInfo(name = "is_exists_unread_message") val isExistsUnreadMessage: Boolean,
    @ColumnInfo(name = "latest_chat_time") val latestChatTime: String,
    @ColumnInfo(name = "is_anonymous") val isAnonymous: Boolean,
    val name: String,
    @ColumnInfo(name = "school_name") val schoolName: String?,
    val grade: Int?,
    @ColumnInfo(name = "class_number") val classNumber: Int?,
    @ColumnInfo(name = "is_bookmarked") val isBookmarked: Boolean,
    @ColumnInfo(name = "is_reported") val isReported: Boolean,
    @ColumnInfo(name = "is_blocked") val isBlocked: Boolean,
    @ColumnInfo(name = "is_ever") val isEver: Boolean,
    @ColumnInfo("last_cursor_id") val lastCursorId: Int?,
) {
    fun toDomain() = Message(
        id = id,
        thumbnail = thumbnail,
        isExistsUnreadMessage = isExistsUnreadMessage,
        latestChatTime = latestChatTime.toISOLocalDateTime(),
        isAnonymous = isAnonymous,
        name = name,
        schoolName = schoolName,
        grade = grade,
        classNumber = classNumber,
        isBookmarked = isBookmarked,
        isReported = isReported,
        isBlocked = isBlocked,
        isEver = isEver,
    )
}
