package com.bff.wespot.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bff.wespot.data.local.common.serializer.MessageProfileConverter
import com.bff.wespot.data.local.database.dao.MessageDao
import com.bff.wespot.data.local.model.message.MessageEntity

@Database(
    entities = [MessageEntity::class],
    version = 1
)
@TypeConverters(MessageProfileConverter::class)
abstract class WeSpotDatabase: RoomDatabase() {
    abstract fun messageDao(): MessageDao
}
