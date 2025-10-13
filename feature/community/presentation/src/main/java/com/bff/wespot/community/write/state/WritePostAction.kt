package com.bff.wespot.community.write.state

import com.bff.wespot.model.community.chip.CategoryItem

sealed interface WritePostAction {
    data class OnTitleChanged(
        val title: String,
    ) : WritePostAction
    data class OnDescriptionChanged(
        val description: String,
    ) : WritePostAction
    data class OnImageChanged(
        val images: List<String>,
    ) : WritePostAction
    data class OnCategoryChanged(
        val category: CategoryItem,
    ) : WritePostAction
    data class OnImageDelete(
        val image: String,
    ) : WritePostAction
    data object UploadPost : WritePostAction
    data class ClosePage(
        val force: Boolean = false,
    ) : WritePostAction
    data object CloseDialog : WritePostAction
}
