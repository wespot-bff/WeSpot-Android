package com.bff.wespot.server.driven.type

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.bff.wespot.designsystem.theme.Destructive
import com.bff.wespot.designsystem.theme.Gray100
import com.bff.wespot.designsystem.theme.Gray200
import com.bff.wespot.designsystem.theme.Gray300
import com.bff.wespot.designsystem.theme.Gray400
import com.bff.wespot.designsystem.theme.Gray500
import com.bff.wespot.designsystem.theme.Gray600
import com.bff.wespot.designsystem.theme.Gray700
import com.bff.wespot.designsystem.theme.Gray800
import com.bff.wespot.designsystem.theme.Gray900
import com.bff.wespot.designsystem.theme.Positive
import com.bff.wespot.designsystem.theme.Primary100
import com.bff.wespot.designsystem.theme.Primary200
import com.bff.wespot.designsystem.theme.Primary300
import com.bff.wespot.designsystem.theme.Primary400
import com.bff.wespot.designsystem.theme.Primary500
import com.bff.wespot.designsystem.theme.White
import com.bff.wespot.model.serverDriven.type.ColorType
import com.bff.wespot.model.serverDriven.type.GradationType
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ColorType.color(): Color {
    return when (this) {
        is ColorType.Hex -> {
            Color(hexCode.toColorInt())
        }

        is ColorType.Token -> {
            token.toDesignSystemColor()
        }
    }
}

@Composable
fun GradationType.toBrush(): Brush {
    val startColorCompose = startColor.color()
    val endColorCompose = endColor.color()

    val angleRadians = Math.toRadians(angle.toDouble())

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

@Composable
private fun String.toDesignSystemColor(): Color {
    return when (this.lowercase()) {
        "primary100" -> Primary100
        "primary200" -> Primary200
        "primary300" -> Primary300
        "primary400" -> Primary400
        "primary500" -> Primary500

        "white" -> White
        "gray100" -> Gray100
        "gray200" -> Gray200
        "gray300" -> Gray300
        "gray400" -> Gray400
        "gray500" -> Gray500
        "gray600" -> Gray600
        "gray700" -> Gray700
        "gray800" -> Gray800
        "gray900" -> Gray900

        "destructive" -> Destructive
        "positive" -> Positive
        else -> Gray900
    }
}
