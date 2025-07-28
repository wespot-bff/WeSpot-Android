package com.bff.wespot.model.community

data class PostInfo(
    val categoryId: String,
    val title: String,
    val description: String,
    val imagesRequest: List<String>,
)
