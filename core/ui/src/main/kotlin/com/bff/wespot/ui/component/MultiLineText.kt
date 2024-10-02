package com.bff.wespot.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis

@Composable
fun MultiLineText(
    text: String,
    style: TextStyle,
    line: Int,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
) {
    val textMeasurer = rememberTextMeasurer()
    Text(
        text = text,
        style = style,
        modifier = modifier
            .fillMaxWidth()
            .layout { measurable, constraints ->
                val textLayoutResult = textMeasurer.measure("\n", constraints = constraints)
                val layoutHeight = textLayoutResult.size.height

                val placeable = measurable.measure(constraints)

                layout(placeable.width, layoutHeight) {
                    placeable.placeRelative(0, 0)
                }
            },
        overflow = Ellipsis,
        maxLines = line,
        textAlign = textAlign,
    )
}
