package com.bff.wespot.community.write

import androidx.lifecycle.SavedStateHandle
import com.bff.wespot.community.write.state.WritePostParams
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object WritePostModule {
    @Provides
    fun provideWritePostParams(
        savedStateHandle: SavedStateHandle,
    ): WritePostParams {
        val postId = savedStateHandle.get<String>("postId") ?: ""
        val isEditing = savedStateHandle.get<Boolean>("isEditing") ?: false
        val title = savedStateHandle.get<String>("title") ?: ""
        val description = savedStateHandle.get<String>("description") ?: ""
        val category = savedStateHandle.get<String>("category") ?: ""
        val images = savedStateHandle.get<ArrayList<String>>("images")?.toList() ?: emptyList()

        return WritePostParams(
            postId = postId,
            isEditing = isEditing,
            title = title,
            description = description,
            category = category,
            images = images,
        )
    }
}
