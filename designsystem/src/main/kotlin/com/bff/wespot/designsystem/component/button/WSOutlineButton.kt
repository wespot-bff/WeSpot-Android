package com.bff.wespot.designsystem.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.R
import com.bff.wespot.designsystem.theme.Primary400
import com.bff.wespot.designsystem.theme.Primary500
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.util.OrientationPreviews

@Composable
fun WSOutlineButton(
    onClick: () -> Unit,
    text: String = "",
    enabled: Boolean = true,
    buttonType: WSOutlineButtonType = WSOutlineButtonType.Primary,
    paddingValues: PaddingValues = PaddingValues(vertical = 12.dp, horizontal = 20.dp),
    content: @Composable RowScope.(text: @Composable () -> Unit) -> Unit,
) {
    WSButton(
        text = text,
        onClick = onClick,
        buttonType = buttonType.buttonType(),
        borderStroke = BorderStroke(1.dp, buttonType.borderColor()),
        pressedBorderStroke = BorderStroke(1.dp, buttonType.borderPressedColor()),
        paddingValues = paddingValues,
        enabled = enabled,
        background = buttonType.backgroundColor(),
    ) {
        content(it)
    }
}

sealed interface WSOutlineButtonType {
    fun buttonType(): WSButtonType

    @Composable
    fun borderColor(): Color

    @Composable
    fun borderPressedColor(): Color

    @Composable
    fun backgroundColor(): Color?

    data object Primary : WSOutlineButtonType {
        override fun buttonType() = WSButtonType.Tertiary

        @Composable
        override fun borderColor() = Primary400

        @Composable
        override fun borderPressedColor() = Color(0xFFADB08D)

        @Composable
        override fun backgroundColor() = null
    }

    data object Highlight : WSOutlineButtonType {
        override fun buttonType() = WSButtonType.Tertiary

        @Composable
        override fun borderColor() = Primary400

        @Composable
        override fun borderPressedColor() = Color(0xFFADB08D)

        @Composable
        override fun backgroundColor() = Primary500
    }

    data object None : WSOutlineButtonType {
        override fun buttonType() = WSButtonType.Tertiary

        @Composable
        override fun borderColor() = Color.Transparent

        @Composable
        override fun borderPressedColor() = Color.Transparent

        @Composable
        override fun backgroundColor() = null
    }
}

@OrientationPreviews
@Composable
private fun WSOutlineButtonPreview() {
    WeSpotTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column {
                WSOutlineButton(
                    onClick = {},
                    text = stringResource(id = R.string.with_friend),
                    buttonType = WSOutlineButtonType.Primary,
                ) {
                    it()
                }
            }
        }
    }
}
