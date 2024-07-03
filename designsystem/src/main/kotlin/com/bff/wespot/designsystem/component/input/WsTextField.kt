package com.bff.wespot.designsystem.component.input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.theme.Primary400
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.designsystem.util.OrientationPreviews

@Composable
fun WsTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isError: Boolean = false,
    singleLine: Boolean = false,
    readOnly: Boolean = false,
    keyBoardOption: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    textFieldType: WsTextFieldType = WsTextFieldType.Normal
) {
    Box(modifier = Modifier.wrapContentSize()) {
        OutlinedTextField(
            modifier = Modifier.heightIn(
                min = textFieldType.minHeight(),
                max = textFieldType.maxHeight()
            ),
            value = value,
            onValueChange = onValueChange,
            isError = isError,
            singleLine = singleLine,
            readOnly = readOnly,
            keyboardOptions = keyBoardOption,
            keyboardActions = keyboardActions,
            trailingIcon = if (textFieldType.trailingIcon() != null) {
                {
                    Icon(
                        imageVector = textFieldType.trailingIcon()!!,
                        contentDescription = "TextField trailing icon"
                    )
                }
            } else {
                null
            },
            leadingIcon = if (textFieldType.leadingIcon() != null) {
                {
                    Icon(
                        imageVector = textFieldType.leadingIcon()!!,
                        contentDescription = "TextField Leading icon"
                    )
                }
            } else {
                null
            },
            placeholder = {
                Text(text = placeholder, style = StaticTypeScale.Default.body4)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = WeSpotThemeManager.colors.cardBackgroundColor,
                unfocusedContainerColor = WeSpotThemeManager.colors.cardBackgroundColor,
                focusedPlaceholderColor = WeSpotThemeManager.colors.disableBtnTxtColor,
                unfocusedPlaceholderColor = WeSpotThemeManager.colors.disableBtnTxtColor,
                focusedBorderColor = Primary400,
                unfocusedBorderColor = WeSpotThemeManager.colors.cardBackgroundColor,
                errorBorderColor = WeSpotThemeManager.colors.dangerColor,
                errorContainerColor = WeSpotThemeManager.colors.cardBackgroundColor,
                errorPlaceholderColor = WeSpotThemeManager.colors.disableBtnTxtColor,
            ),
            shape = WeSpotThemeManager.shapes.small,
            textStyle = StaticTypeScale.Default.body4,
        )
    }
}

sealed interface WsTextFieldType {

    fun trailingIcon(): ImageVector?

    fun leadingIcon(): ImageVector?

    fun minHeight(): Dp = Dp.Unspecified

    fun maxHeight(): Dp = Dp.Unspecified

    data object Normal : WsTextFieldType {

        override fun trailingIcon() = null

        override fun leadingIcon() = null
    }

    data object Search : WsTextFieldType {

        override fun trailingIcon() = null

        override fun leadingIcon() = Icons.Default.Search
    }

    data object Lock : WsTextFieldType {
        override fun trailingIcon() = Icons.Default.Lock

        override fun leadingIcon() = null
    }

    data object Message : WsTextFieldType {
        override fun trailingIcon() = null

        override fun leadingIcon() = null

        override fun minHeight() = 170.dp

        override fun maxHeight() = 228.dp
    }
}

@OrientationPreviews
@Composable
private fun WsTextFieldPreview() {
    val (value, onValueChanged) = remember { mutableStateOf("") }

    WeSpotTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                WsTextField(
                    value = value,
                    onValueChange = onValueChanged,
                    placeholder = "Search",
                    textFieldType = WsTextFieldType.Normal
                )

                WsTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = "Search",
                    textFieldType = WsTextFieldType.Search
                )

                WsTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = "Search",
                    textFieldType = WsTextFieldType.Lock
                )

                WsTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = "Search",
                    isError = true,
                    textFieldType = WsTextFieldType.Normal
                )

            }
        }
    }
}

@OrientationPreviews
@Composable
private fun MessagePreview() {
    val (value, onValueChanged) = remember { mutableStateOf("") }

    WeSpotTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                WsTextField(
                    value = value,
                    onValueChange = onValueChanged,
                    placeholder = "Message",
                    textFieldType = WsTextFieldType.Message
                )
            }
        }
    }
}