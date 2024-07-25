package com.bff.wespot.util

import androidx.compose.ui.graphics.Color

fun hexToColor(hex: String): Color {
    if (hex.isEmpty()) {
        return Color.Transparent
    }

    val colorString = hex.removePrefix("#")
    val color = android.graphics.Color.parseColor("#$colorString")
    return Color(color)
}
