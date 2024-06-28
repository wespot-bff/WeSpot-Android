package com.bff.wespot.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.theme.Primary400
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.designsystem.util.OrientationPreviews

private const val PREVIEW_TEXT = "친구랑 같이 하기"

@Composable
fun WSOutlineButton(
    onClick: () -> Unit,
    text: String = "",
    enabled: Boolean = true,
    buttonType: WSOutlineButtonType = WSOutlineButtonType.Primary,
    content: @Composable RowScope.(text: @Composable () -> Unit) -> Unit,
) {
    WSButton(
        text = text,
        onClick = onClick,
        buttonType = buttonType.buttonType(),
        borderStroke = BorderStroke(1.dp, buttonType.borderColor()),
        enabled = enabled
    ) {
        content(it)
    }
}

sealed interface WSOutlineButtonType {
    fun buttonType(): WSButtonType

    @Composable
    fun borderColor(): Color


    data object Primary : WSOutlineButtonType {
        override fun buttonType() = WSButtonType.Tertiary

        @Composable
        override fun borderColor() = Primary400
    }
}

@OrientationPreviews
@Composable
private fun WSOutlineButtonPreview() {
    WeSpotTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                WSOutlineButton(
                    onClick = {},
                    text = PREVIEW_TEXT,
                    buttonType = WSOutlineButtonType.Primary
                ) {
                    it()
                }
            }
        }
    }
}