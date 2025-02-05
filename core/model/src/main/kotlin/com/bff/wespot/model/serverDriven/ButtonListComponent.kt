package com.bff.wespot.model.serverDriven

data class ButtonListComponent(
    val buttonList: List<ButtonItemComponent>,
) : BaseComponent

data class ButtonItemComponent(
    val text: String,
    val textColor: Long,
    val buttonColor: Long,
    val pressColor: Long,
    val onClickAction: ClickAction,
)
