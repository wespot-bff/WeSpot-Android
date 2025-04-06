package com.bff.wespot.data.local.common.serializer

import androidx.room.TypeConverter
import com.bff.wespot.model.user.response.User
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class UserConverter {
    @TypeConverter
    fun fromUser(user: User): String {
        return Json.encodeToString(user)
    }

    @TypeConverter
    fun toUser(json: String): User {
        return Json.decodeFromString(json)
    }
}
