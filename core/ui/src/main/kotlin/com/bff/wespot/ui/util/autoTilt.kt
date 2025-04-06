package com.bff.wespot.ui.util

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.debugInspectorInfo

fun Modifier.autoTilt(enableTilt: Boolean): Modifier = composed(
    factory = {
        if (!enableTilt) return@composed this
        val rotation = remember { Animatable(0f) }

        LaunchedEffect(Unit) {
            repeat(5) {
                rotation.animateTo(
                    targetValue = 5f,
                    animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing),
                )
                rotation.animateTo(
                    targetValue = -5f,
                    animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing),
                )
            }
            rotation.animateTo(0f) // Reset to the original position
        }
        Modifier.graphicsLayer {
            rotationZ = rotation.value
        }
    },
    inspectorInfo = debugInspectorInfo {
        name = "autoTilt"
    },
)
