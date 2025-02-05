package com.bff.wespot.server.driven.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.theme.Gray600
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager

@Composable
fun ChipSection(
    text: String,
    paddingValues: PaddingValues = PaddingValues(0.dp),
) {
    FilterChip(
        modifier = Modifier.padding(paddingValues),
        shape = WeSpotThemeManager.shapes.extraLarge,
        label = {
            Text(
                text = text,
                style = StaticTypeScale.Default.body5,
            )
        },
        selected = true,
        border = BorderStroke(1.dp, WeSpotThemeManager.colors.primaryColor),
        onClick = { },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Gray600,
            selectedLabelColor = WeSpotThemeManager.colors.abledTxtColor,
        ),
    )
}
