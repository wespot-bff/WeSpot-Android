package com.bff.wespot.ui.component

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.theme.Gray300

@Composable
fun Modifier.verticalScrollIndicator(
    scrollState: ScrollState,
    paddingValues: PaddingValues,
    width: Dp,
): Modifier {
    return this.then(
        Modifier
            .padding(paddingValues)
            .drawBehind {
                if (scrollState.maxValue == 0) return@drawBehind

                val scrollFraction =
                    scrollState.value.toFloat() / scrollState.maxValue.toFloat().coerceAtLeast(1f)

                val scrollbarHeight = 40.dp.toPx()
                val scrollbarTop = scrollFraction * (size.height - scrollbarHeight)

                drawRoundRect(
                    cornerRadius = CornerRadius(99f),
                    color = Gray300,
                    topLeft = Offset(x = size.width - width.toPx(), y = scrollbarTop),
                    size = Size(width = width.toPx(), height = scrollbarHeight),
                )
            },
    )
}
