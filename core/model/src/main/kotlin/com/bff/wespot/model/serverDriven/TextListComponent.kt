package com.bff.wespot.model.serverDriven

data class TextListComponent(
    val textList: List<TextList>,
) : BaseComponent

data class TextList(
    val icon: String,
    val text: String,
)
