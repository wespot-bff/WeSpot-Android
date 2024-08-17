package com.bff.wespot.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.theme.Gray900

@Composable
fun ListBottomGradient(
    height: Int = 70,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Gray900.copy(alpha = 0f),
                        Gray900,
                    ),
                ),
            ),
    )
}
