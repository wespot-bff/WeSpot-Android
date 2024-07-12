package com.bff.wespot.designsystem.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

private fun Int.textDp(density: Density): TextUnit = with(density) {
    this@textDp.dp.toSp()
}

val Int.textDp: TextUnit
    @Composable get() = this.textDp(density = LocalDensity.current)

private fun Float.textDp(density: Density): TextUnit = with(density) {
    this@textDp.dp.toSp()
}

val Float.textDp: TextUnit
    @Composable get() = this.textDp(density = LocalDensity.current)
