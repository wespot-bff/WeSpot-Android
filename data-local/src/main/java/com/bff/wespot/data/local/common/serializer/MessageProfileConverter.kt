package com.bff.wespot.data.local.common.serializer

import androidx.room.TypeConverter
import com.bff.wespot.model.message.response.MessageProfile
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class MessageProfileConverter {
    @TypeConverter
    fun fromMessageProfile(profile: MessageProfile): String {
        return Json.encodeToString(profile)
    }

    @TypeConverter
    fun toMessageProfile(json: String): MessageProfile {
        return Json.decodeFromString(json)
    }
}
