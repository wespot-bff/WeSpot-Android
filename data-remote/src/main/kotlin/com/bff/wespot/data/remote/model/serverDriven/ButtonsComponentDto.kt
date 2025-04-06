package com.bff.wespot.data.remote.model.serverDriven

import com.bff.wespot.data.remote.model.serverDriven.click.ClickActionDto
import com.bff.wespot.model.serverDriven.ButtonComponent
import com.bff.wespot.model.serverDriven.ButtonsComponent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("buttonsComponent")
data class ButtonsComponentDto(
    val content: ButtonsComponentContent,
) : BaseComponentDto {
    override fun toDomain(): ButtonsComponent {
        return ButtonsComponent(
            buttons = content.buttons.map {
                it.toDomain()
            },
            paddings = content.paddings.toDomain()
        )
    }
}

@Serializable
data class ButtonsComponentContent(
    val buttons: List<ButtonComponentContent>,
    val paddings: PaddingsDto = PaddingsDto.None
)

@Serializable
data class ButtonComponentContent(
    val richText: RichTextDto,
    val buttonColor: String,
    val pressColor: String,
    val onClickAction: ClickActionDto,
    val paddings: PaddingsDto = PaddingsDto.None
) {
    fun toDomain(): ButtonComponent {
        return ButtonComponent(
            richText = richText.toDomain(),
            buttonColor = buttonColor,
            pressColor = pressColor,
            onClickAction = onClickAction.toDomain(),
            paddings = paddings.toDomain()
        )
    }
}