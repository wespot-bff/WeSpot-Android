package com.bff.wespot.model.serverDriven

data class TextListComponent(
    val textList: List<TextList>,
    override val paddings: Paddings,
) : BaseComponent

data class TextList(
    val icon: String,
    val richText: RichText,
)
