package com.bff.wespot.data.remote.model.dynamicui.component

import com.bff.wespot.model.dynamicui.component.ButtonComponent
import com.bff.wespot.model.dynamicui.component.DynamicUiComponent
import com.bff.wespot.model.dynamicui.component.ImageComponent
import com.bff.wespot.model.dynamicui.component.TextComponent
import kotlinx.serialization.Serializable

@Serializable
sealed interface DynamicUiComponentDto

fun DynamicUiComponentDto.toDynamicUiComponent(): DynamicUiComponent {
    return when (this) {
        is TextComponentDto -> TextComponent(text)
        is ImageComponentDto -> ImageComponent(url, width, height)
        is ButtonComponentDto -> ButtonComponent(text, link)
    }
}
