package com.bff.wespot.server.driven.type

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import kotlin.math.cos
import kotlin.math.sin

data class GradationUiModel(
    val startColor: com.bff.wespot.model.serverDriven.type.ColorType,
    val endColor: com.bff.wespot.model.serverDriven.type.ColorType,
    val angle: Int,
)

@Composable
fun GradationUiModel.toBrush(): Brush {
    val startColorCompose = startColor.color()
    val endColorCompose = endColor.color()

    // Convert angle to radians
    val angleRadians = Math.toRadians(angle.toDouble())

    // Calculate start and end points based on angle
    // Angle 0 = left to right, 90 = top to bottom, etc.
    val startX = (1 - cos(angleRadians)).toFloat() * 0.5f
    val startY = (1 + sin(angleRadians)).toFloat() * 0.5f
    val endX = (1 + cos(angleRadians)).toFloat() * 0.5f
    val endY = (1 - sin(angleRadians)).toFloat() * 0.5f

    return Brush.linearGradient(
        colors = listOf(startColorCompose, endColorCompose),
        start = Offset(startX, startY),
        end = Offset(endX, endY),
    )
}
