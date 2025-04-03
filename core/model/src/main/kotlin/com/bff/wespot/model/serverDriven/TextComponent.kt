package com.bff.wespot.model.serverDriven

data class TextComponent(
    val richText: RichText,
    override val paddings: Paddings,
) : BaseComponent
