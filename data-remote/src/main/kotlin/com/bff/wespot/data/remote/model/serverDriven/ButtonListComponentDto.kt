package com.bff.wespot.data.remote.model.serverDriven

import com.bff.wespot.model.serverDriven.BaseComponent
import com.bff.wespot.model.serverDriven.ButtonItemComponent
import com.bff.wespot.model.serverDriven.ButtonListComponent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("buttonListComponent")
data class ButtonListComponentDto(
    val buttonList: List<ButtonItemComponentDto>
): BaseComponentDto {
    override fun toDomain(): BaseComponent {
        return ButtonListComponent(
            buttonList = buttonList.map {
                it.toDomain()
            }
        )
    }
}

@Serializable
data class ButtonItemComponentDto(
    val text: String,
    val textColor: Long,
    val buttonColor: Long,
    val pressColor: Long,
    val onClickAction: ClickActionDto,
) {
    fun toDomain(): ButtonItemComponent = ButtonItemComponent(
        text = text,
        textColor = textColor,
        buttonColor = buttonColor,
        pressColor = pressColor,
        onClickAction = onClickAction.toDomain(),
    )
}
