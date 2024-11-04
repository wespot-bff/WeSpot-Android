package com.bff.wespot.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.ui.R

@Composable
fun WSSelectionItem(
    title: String,
    selected: Boolean,
    isEditable: Boolean = false,
    onTitleChanged: (String) -> Unit = { },
    onClick: () -> Unit = { },
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(WeSpotThemeManager.shapes.medium)
                .border(
                    width = 1.dp,
                    color = if (selected) {
                        WeSpotThemeManager.colors.primaryColor
                    } else {
                        WeSpotThemeManager.colors.cardBackgroundColor
                    },
                    shape = WeSpotThemeManager.shapes.medium,
                )
                .background(WeSpotThemeManager.colors.cardBackgroundColor)
                .clickable { onClick() },
        ) {
            Row(
                modifier = Modifier.padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .align(Alignment.Top),
                    painter = painterResource(id = R.drawable.ic_check),
                    contentDescription = "check_icon",
                    tint = if (selected) {
                        WeSpotThemeManager.colors.primaryColor
                    } else {
                        WeSpotThemeManager.colors.disableIcnColor
                    },
                )

                if (isEditable) {
                    SelectionItemTextField(
                        title = title,
                        selected = selected,
                        onTitleChanged = onTitleChanged,
                    )
                } else {
                    Text(
                        text = title,
                        style = StaticTypeScale.Default.body4,
                        color = WeSpotThemeManager.colors.txtTitleColor,
                        maxLines = 1,
                    )
                }
            }
        }

        if (isEditable) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                LetterCountIndicator(
                    currentCount = if (selected) title.length else 0,
                    maxCount = 100,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectionItemTextField(
    title: String,
    selected: Boolean,
    onTitleChanged: (String) -> Unit = { },
) {
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }
    val colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = WeSpotThemeManager.colors.cardBackgroundColor,
        unfocusedContainerColor = WeSpotThemeManager.colors.cardBackgroundColor,
        focusedBorderColor = WeSpotThemeManager.colors.cardBackgroundColor,
        unfocusedBorderColor = WeSpotThemeManager.colors.cardBackgroundColor,
        cursorColor = WeSpotThemeManager.colors.txtTitleColor,
    )

    BasicTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 18.dp)
            .focusRequester(focusRequester),
        value = title,
        onValueChange = onTitleChanged,
        enabled = selected,
        textStyle = StaticTypeScale.Default.body4.copy(
            color = WeSpotThemeManager.colors.txtTitleColor,
        ),
        cursorBrush = SolidColor(WeSpotThemeManager.colors.txtTitleColor),
        decorationBox = { innerTextField ->
            OutlinedTextFieldDefaults.DecorationBox(
                value = title,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = false,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                contentPadding = PaddingValues(0.dp),
                colors = colors,
                placeholder = {
                    if (selected.not()) {
                        Text(
                            text = "직접 입력",
                            style = StaticTypeScale.Default.body4,
                            color = WeSpotThemeManager.colors.txtTitleColor,
                        )
                    }
                },
            )
        },
    )

    LaunchedEffect(selected) {
        focusRequester.requestFocus()
    }
}
