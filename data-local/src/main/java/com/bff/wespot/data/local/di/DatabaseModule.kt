package com.bff.wespot.data.local.di

import android.content.Context
import androidx.room.Room.databaseBuilder
import com.bff.wespot.data.local.database.WeSpotDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesWeSpotDatabase(
        @ApplicationContext context: Context
    ): WeSpotDatabase {
        return databaseBuilder(
            context = context,
            klass = WeSpotDatabase::class.java,
            name = "message_db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesMessageDao(database: WeSpotDatabase) = database.messageDao()
}
