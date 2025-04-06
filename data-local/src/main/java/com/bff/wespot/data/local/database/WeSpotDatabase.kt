package com.bff.wespot.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bff.wespot.data.local.common.serializer.UserConverter
import com.bff.wespot.data.local.database.dao.ReceivedMessageDao
import com.bff.wespot.data.local.model.message.ReceivedMessageEntity

@Database(
    entities = [ReceivedMessageEntity::class],
    version = 1
)
@TypeConverters(UserConverter::class)
abstract class WeSpotDatabase: RoomDatabase() {
    abstract fun receivedMessageDao(): ReceivedMessageDao
}
