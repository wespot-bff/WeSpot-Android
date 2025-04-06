package com.bff.wespot.server.driven.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bff.wespot.model.serverDriven.Paddings

internal fun String.toFontWeight(): FontWeight =
    when (this) {
        "Bold" -> FontWeight.Bold
        "SemiBold" -> FontWeight.SemiBold
        else -> FontWeight.Normal
    }

internal fun String.toTextAlign(): TextAlign =
    when (this) {
        "Center" -> TextAlign.Center
        "Start" -> TextAlign.Start
        "End" -> TextAlign.End
        else -> TextAlign.Justify
    }

internal fun String.toColor(): Color {
    return Color(android.graphics.Color.parseColor(this))
}

internal fun Paddings.toPaddingValues(): PaddingValues {
    return PaddingValues(
        start = start.dp,
        top = top.dp,
        end = end.dp,
        bottom = bottom.dp,
    )
}
