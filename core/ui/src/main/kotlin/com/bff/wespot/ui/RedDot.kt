package com.bff.wespot.ui

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.designsystem.util.OrientationPreviews

@Composable
fun RedDot(modifier: Modifier = Modifier) {
    val color = WeSpotThemeManager.colors.dangerColor

    Canvas(modifier = modifier) {
        drawCircle(color)
    }
}

@OrientationPreviews
@Composable
private fun RedDotPreview() {
    WeSpotTheme {
        RedDot()
    }
}