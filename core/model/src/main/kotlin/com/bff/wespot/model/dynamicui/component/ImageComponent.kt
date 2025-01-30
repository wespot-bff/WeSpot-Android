package com.bff.wespot.model.dynamicui.component

data class ImageComponent(
    val url: String = "",
    val width: Int = -1,
    val height: Int = -1,
) : DynamicUiComponent {
    fun isFillMaxWidth() = this.width == 0
    fun isFillMaxHeight() = this.height == 0
}
