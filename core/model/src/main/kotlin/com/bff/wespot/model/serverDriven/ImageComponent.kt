package com.bff.wespot.model.serverDriven

data class ImageComponent(
    val url: String,
    val width: Int,
    val height: Int,
    override val paddings: Paddings,
) : BaseComponent
