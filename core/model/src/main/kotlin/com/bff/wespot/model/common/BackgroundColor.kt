package com.bff.wespot.model.common

data class BackgroundColor(
    val id: Int,
    val color: String,
    val name: String,
)

data class BackgroundColorList(
    val backgrounds: List<BackgroundColor>,
)
