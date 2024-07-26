package com.bff.wespot.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp

@Composable
fun MultiLineText(
    text: String,
    style: TextStyle,
    line: Int,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
) {
    var height by remember { mutableStateOf(Dp.Unspecified) }
    val density = LocalDensity.current

    Text(
        modifier = modifier
            .height(height)
            .fillMaxWidth()
            .wrapContentHeight(),
        text = text,
        style = style,
        onTextLayout = {
            val newHeight = with(density) { it.size.height.toDp() }
            if (it.lineCount < line) {
                height = newHeight * (line / it.lineCount)
            }
        },
        overflow = TextOverflow.Ellipsis,
        maxLines = line,
        textAlign = textAlign,
    )
}
