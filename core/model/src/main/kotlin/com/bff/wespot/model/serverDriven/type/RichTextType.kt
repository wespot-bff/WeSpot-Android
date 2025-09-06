package com.bff.wespot.model.serverDriven.type

data class RichTextType(
    val text: String,
    val color: ColorType,
    val typography: String,
    val maxLine: Int = 0,
)
