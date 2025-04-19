package com.bff.wespot.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bff.wespot.data.local.database.dao.MessageDao
import com.bff.wespot.data.local.model.message.MessageEntity

@Database(
    entities = [MessageEntity::class],
    version = 1
)
abstract class WeSpotDatabase: RoomDatabase() {
    abstract fun messageDao(): MessageDao
}
