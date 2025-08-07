package com.bff.wespot.community.categorydetail

import androidx.lifecycle.SavedStateHandle
import com.bff.wespot.community.categorydetail.state.CategoryDetailParams
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object CategoryDetailModule {
    @Provides
    fun provideCategoryParams(savedStateHandle: SavedStateHandle): CategoryDetailParams {
        val categoryId = savedStateHandle.get<String>("categoryId") ?: ""
        return CategoryDetailParams(categoryId = categoryId)
    }
}
