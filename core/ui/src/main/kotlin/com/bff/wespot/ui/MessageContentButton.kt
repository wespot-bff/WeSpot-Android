package com.bff.wespot.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager

@Composable
fun MessageContentButton(
    onClick: () -> Unit,
    text: String = "",
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    val isPressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(vertical = 12.dp, horizontal = 20.dp),
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(
                    min = 170.dp,
                    max = 228.dp,
                ),
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                contentColor = WeSpotThemeManager.colors.abledTxtColor,
                containerColor = if (isPressed) {
                    Color(0xFF27282B)
                } else {
                    WeSpotThemeManager.colors.tertiaryBtnColor
                },
                disabledContentColor = WeSpotThemeManager.colors.disableBtnTxtColor.copy(alpha = 0.8f),
                disabledContainerColor = WeSpotThemeManager.colors.disableBtnColor,
            ),
            interactionSource = interactionSource,
            shape = WeSpotThemeManager.shapes.small,
            contentPadding = PaddingValues(0.dp),
        ) {
            Text(
                text = text,
                style = StaticTypeScale.Default.body4,
                color = WeSpotThemeManager.colors.abledTxtColor,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                textAlign = TextAlign.Start,
            )
        }
    }
}
