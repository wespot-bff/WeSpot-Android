package com.bff.wespot.designsystem.component.button

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.R
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.designsystem.util.OrientationPreviews


@Composable
fun WSTextButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    buttonType: WSTextButtonType = WSTextButtonType.Primary,
) {
    Box(
        modifier =
        Modifier
            .wrapContentSize()
            .padding(8.dp),
    ) {
        TextButton(onClick = onClick, enabled = enabled) {
            Text(text = text, color = buttonType.textColor(), style = buttonType.fontStyle())
        }
    }
}

sealed interface WSTextButtonType {
    @Composable
    fun textColor(): Color

    @Composable
    fun fontStyle(): TextStyle

    data object Primary : WSTextButtonType {
        @Composable
        override fun textColor() = WeSpotThemeManager.colors.abledTxtColor

        @Composable
        override fun fontStyle() = StaticTypeScale.Default.body4
    }

    data object Underline : WSTextButtonType {
        @Composable
        override fun textColor() = WeSpotThemeManager.colors.abledTxtColor

        @Composable
        override fun fontStyle() =
            StaticTypeScale.Default.body4.copy(textDecoration = TextDecoration.Underline)
    }
}

@OrientationPreviews
@Composable
private fun WSTextButtonPreview() {
    WeSpotTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                WSTextButton(
                    text = stringResource(id = R.string.any_friend_not_registered),
                    onClick = {},
                    buttonType = WSTextButtonType.Primary
                )
                WSTextButton(
                    text = stringResource(id = R.string.any_friend_not_registered),
                    onClick = {},
                    buttonType = WSTextButtonType.Underline
                )
            }
        }
    }
}
