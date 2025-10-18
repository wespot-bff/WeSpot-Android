package com.bff.wespot.server.driven.type

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.bff.wespot.model.serverDriven.type.RichTextType

@Composable
fun RichTextType.Text(
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
) {
    androidx.compose.material3.Text(
        text = text,
        color = color.color(),
        style = typography.toTextStyle() ?: textStyle,
        maxLines = if (maxLine == 0) maxLines else this.maxLine,
        overflow = overflow,
        softWrap = softWrap,
        onTextLayout = onTextLayout,
        modifier = modifier,
    )
}
