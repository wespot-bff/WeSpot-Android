package com.bff.wespot.community.write.state

data class WritePostParams(
    val isEditing: Boolean = false,
    val postId: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val images: List<String> = emptyList(),
)
