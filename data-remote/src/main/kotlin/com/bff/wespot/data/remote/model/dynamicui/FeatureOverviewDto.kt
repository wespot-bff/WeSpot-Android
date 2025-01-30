package com.bff.wespot.data.remote.model.dynamicui

import com.bff.wespot.data.remote.model.dynamicui.component.DynamicUiComponentDto
import com.bff.wespot.data.remote.model.dynamicui.component.toDynamicUiComponent
import com.bff.wespot.model.dynamicui.FeatureOverview
import com.bff.wespot.model.dynamicui.component.ButtonComponent
import com.bff.wespot.model.dynamicui.component.ImageComponent
import com.bff.wespot.model.dynamicui.component.TextComponent
import kotlinx.serialization.Serializable

@Serializable
data class FeatureOverviewDto(
    val id: Int,
    val name: String,
    val data: List<DynamicUiComponentDto>,
) {
    fun toFeatureOverview(): FeatureOverview = FeatureOverview(
        headerText = data[0].toDynamicUiComponent() as? TextComponent ?: TextComponent(),
        overview = data[1].toDynamicUiComponent() as? ImageComponent ?: ImageComponent(),
        dismissButton = data[2].toDynamicUiComponent() as? ButtonComponent ?: ButtonComponent(),
        navigateButton = data[3].toDynamicUiComponent() as? ButtonComponent ?: ButtonComponent(),
    )
}
