package com.bff.wespot.data.local.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.bff.wespot.data.local.ProfilePreference
import com.bff.wespot.data.local.common.serializer.ProfileSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProtoDataStoreModule {
    @Provides
    @Singleton
    fun providesProfilePreferencesDataStore(
        @ApplicationContext context: Context,
        coroutineDispatcher: CoroutineDispatcher,
        profileSerializer: ProfileSerializer,
    ): DataStore<ProfilePreference> =
        DataStoreFactory.create(
            serializer = profileSerializer,
            produceFile = { context.dataStoreFile("profile_preferences.pb") },
            scope = CoroutineScope(coroutineDispatcher + SupervisorJob())
        )
}
