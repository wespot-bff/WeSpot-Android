package com.bff.wespot.server.driven.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.designsystem.util.lineHeight
import com.bff.wespot.designsystem.util.textDp
import com.bff.wespot.model.serverDriven.ButtonsComponent
import com.bff.wespot.model.serverDriven.Paddings
import com.bff.wespot.model.serverDriven.RichText
import com.bff.wespot.server.driven.util.toColor
import com.bff.wespot.server.driven.util.toFontWeight
import com.bff.wespot.server.driven.util.toPaddingValues
import com.bff.wespot.server.driven.util.toTextAlign

@Composable
internal fun ButtonsSection(
    buttonsComponent: ButtonsComponent,
    onClick: List<() -> Unit>,
    paddings: Paddings,
) {
    Row(
        modifier = Modifier.padding(paddings.toPaddingValues()),
    ) {
        buttonsComponent.buttons.forEachIndexed { index, item ->
            SingleButton(
                richText = item.richText,
                buttonColor = item.buttonColor,
                pressColor = item.pressColor,
                onClick = onClick[index],
                paddings = item.paddings,
            )
        }
    }
}

@Composable
private fun RowScope.SingleButton(
    richText: RichText,
    buttonColor: String,
    pressColor: String,
    onClick: () -> Unit,
    paddings: Paddings,
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val isPressed by interactionSource.collectIsPressedAsState()

    val containerColor = if (isPressed) {
        pressColor.toColor()
    } else {
        buttonColor.toColor()
    }

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
        ),
        modifier = Modifier
            .weight(1f),
        interactionSource = interactionSource,
        shape = WeSpotThemeManager.shapes.small,
    ) {
        Text(
            text = richText.text,
            fontSize = richText.fontSize.textDp,
            fontWeight = richText.fontWeight.toFontWeight(),
            lineHeight = richText.fontSize.lineHeight,
            textAlign = richText.align.toTextAlign(),
            color = richText.color.toColor(),
            modifier = Modifier
                .padding(paddings.toPaddingValues()),
        )
    }
}
