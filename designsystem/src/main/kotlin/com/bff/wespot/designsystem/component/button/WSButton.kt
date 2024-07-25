package com.bff.wespot.designsystem.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.R
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.designsystem.util.OrientationPreviews

@Composable
fun WSButton(
    onClick: () -> Unit,
    text: String = "",
    buttonType: WSButtonType = WSButtonType.Primary,
    paddingValues: PaddingValues = PaddingValues(vertical = 12.dp, horizontal = 20.dp),
    enabled: Boolean = true,
    borderStroke: BorderStroke? = null,
    background: Color? = null,
    pressedBorderStroke: BorderStroke? = null,
    content: @Composable RowScope.(text: @Composable () -> Unit) -> Unit,
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    val isPressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(paddingValues),
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                contentColor = buttonType.textColor(),
                containerColor = if (isPressed) {
                    buttonType.pressColor()
                } else {
                    background ?: buttonType.background()
                },
                disabledContentColor = WeSpotThemeManager.colors.disableBtnTxtColor.copy(alpha = 0.8f),
                disabledContainerColor = WeSpotThemeManager.colors.disableBtnColor,
            ),
            interactionSource = interactionSource,
            shape = WeSpotThemeManager.shapes.small,
            enabled = enabled,
            border = if (isPressed) {
                pressedBorderStroke
            } else {
                borderStroke
            },
            contentPadding = PaddingValues(0.dp),
        ) {
            content {
                Text(
                    text = text,
                    style = buttonType.fontStyle(),
                    modifier = Modifier.padding(vertical = 14.dp),
                )
            }
        }
    }
}

sealed interface WSButtonType {
    @Composable
    fun background(): Color

    @Composable
    fun pressColor(): Color

    @Composable
    fun textColor(): Color

    @Composable
    fun fontStyle(): TextStyle

    data object Primary : WSButtonType {
        @Composable
        override fun background() = WeSpotThemeManager.colors.primaryBtnColor

        @Composable
        override fun pressColor() = Color(0xFFC0C66B)

        @Composable
        override fun textColor() = WeSpotThemeManager.colors.backgroundColor

        @Composable
        override fun fontStyle() = StaticTypeScale.Default.body3
    }

    data object Secondary : WSButtonType {
        @Composable
        override fun background() = WeSpotThemeManager.colors.secondaryBtnColor

        @Composable
        override fun pressColor() = Color(0xFF48494C)

        @Composable
        override fun textColor() = WeSpotThemeManager.colors.abledTxtColor

        @Composable
        override fun fontStyle() = StaticTypeScale.Default.body3
    }

    data object Tertiary : WSButtonType {
        @Composable
        override fun background() = WeSpotThemeManager.colors.tertiaryBtnColor

        @Composable
        override fun pressColor() = Color(0xFF27282B)

        @Composable
        override fun textColor() = WeSpotThemeManager.colors.abledTxtColor

        @Composable
        override fun fontStyle() = StaticTypeScale.Default.body3
    }
}

@OrientationPreviews
@Composable
private fun WSButtonPreview() {
    WeSpotTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                WSButton(
                    text = stringResource(id = R.string.register_classmate),
                    buttonType = WSButtonType.Primary,
                    onClick = {},
                ) {
                    it()
                }

                WSButton(
                    text = stringResource(id = R.string.register_classmate),
                    buttonType = WSButtonType.Secondary,
                    onClick = {},
                ) {
                    it()
                }

                WSButton(
                    text = stringResource(id = R.string.register_classmate),
                    buttonType = WSButtonType.Tertiary,
                    onClick = {},
                ) {
                    it()
                }
            }
        }
    }
}

@OrientationPreviews
@Composable
private fun WSButtonDisablePreview() {
    WeSpotTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                WSButton(
                    text = stringResource(id = R.string.register_classmate),
                    buttonType = WSButtonType.Primary,
                    onClick = {},
                    enabled = false,
                ) {
                    it()
                }

                WSButton(
                    text = stringResource(id = R.string.register_classmate),
                    buttonType = WSButtonType.Secondary,
                    onClick = {},
                    enabled = false,
                ) {
                    it()
                }

                WSButton(
                    text = stringResource(id = R.string.register_classmate),
                    buttonType = WSButtonType.Tertiary,
                    onClick = {},
                    enabled = false,
                ) {
                    it()
                }
            }
        }
    }
}

@OrientationPreviews
@Composable
private fun WSButtonIconPreview() {
    WeSpotTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                WSButton(
                    buttonType = WSButtonType.Primary,
                    onClick = {},
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.add),
                        tint = WeSpotThemeManager.colors.backgroundColor,
                        modifier = Modifier.padding(16.dp),
                    )
                }

                WSButton(
                    buttonType = WSButtonType.Secondary,
                    onClick = {},
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.add),
                        tint = WeSpotThemeManager.colors.backgroundColor,
                        modifier = Modifier.padding(16.dp),
                    )
                }

                WSButton(
                    buttonType = WSButtonType.Tertiary,
                    onClick = {},
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.add),
                        tint = WeSpotThemeManager.colors.backgroundColor,
                        modifier = Modifier.padding(16.dp),
                    )
                }

                WSButton(
                    text = stringResource(id = R.string.register_classmate),
                    buttonType = WSButtonType.Primary,
                    onClick = {},
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.add),
                        tint = WeSpotThemeManager.colors.backgroundColor,
                        modifier = Modifier.padding(16.dp),
                    )
                    it()
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.add),
                        tint = WeSpotThemeManager.colors.backgroundColor,
                        modifier = Modifier.padding(vertical = 16.dp),
                    )
                }
            }
        }
    }
}
