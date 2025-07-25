package com.bff.wespot.community.di

import android.content.Context
import com.bff.wespot.community.detail.state.PostDetailParams
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
object PostDetailModule {
    @Provides
    fun providePostDetailParams(
        @ActivityContext context: Context,
    ): PostDetailParams {
        val postId = context as android.app.Activity
        return PostDetailParams(
            postId = postId.intent.getStringExtra("postId") ?: "",
        )
    }
}
