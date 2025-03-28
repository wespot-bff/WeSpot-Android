package com.bff.wespot.model.serverDriven

import com.bff.wespot.model.serverDriven.click.ClickAction

data class ButtonsComponent(
    val buttons: List<ButtonComponent>,
    override val paddings: Paddings,
) : BaseComponent

data class ButtonComponent(
    val richText: RichText,
    val buttonColor: String,
    val pressColor: String,
    val onClickAction: ClickAction,
    val paddings: Paddings,
)
