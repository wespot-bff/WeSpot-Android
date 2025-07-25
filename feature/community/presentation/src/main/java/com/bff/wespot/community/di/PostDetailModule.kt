package com.bff.wespot.community.di

import androidx.lifecycle.SavedStateHandle
import com.bff.wespot.community.detail.state.PostDetailParams
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object PostDetailModule {
    @Provides
    fun providePostDetailParams(
        savedStateHandle: SavedStateHandle,
    ): PostDetailParams {
        val postId = savedStateHandle.get<String>("postId") ?: ""

        return PostDetailParams(
            postId = postId,
        )
    }
}
