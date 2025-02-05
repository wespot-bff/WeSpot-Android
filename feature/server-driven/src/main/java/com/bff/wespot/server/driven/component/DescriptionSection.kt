package com.bff.wespot.server.driven.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.theme.StaticTypeScale

@Composable
fun DescriptionSection(
    text: String,
    color: Color,
    paddingValues: PaddingValues = PaddingValues(0.dp),
) {
    Text(
        modifier = Modifier.padding(paddingValues),
        text = text,
        style = StaticTypeScale.Default.body6,
        color = color,
    )
}
