package com.bff.wespot.model.dynamicui

import com.bff.wespot.model.dynamicui.component.ButtonComponent
import com.bff.wespot.model.dynamicui.component.ImageComponent
import com.bff.wespot.model.dynamicui.component.TextComponent

data class FeatureOverview(
    val headerText: TextComponent = TextComponent("새로운 기능"),
    val overview: ImageComponent = ImageComponent(),
    val dismissButton: ButtonComponent = ButtonComponent(text = "다음에 하기"),
    val navigateButton: ButtonComponent = ButtonComponent(text = "설정하기"),
)
