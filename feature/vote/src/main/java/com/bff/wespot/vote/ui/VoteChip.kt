package com.bff.wespot.vote.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.theme.Gray400
import com.bff.wespot.designsystem.theme.Gray500
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager

@Composable
fun VoteChip(
    text: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
) {
    Box(
        modifier = Modifier
            .background(
                if (isSelected) {
                    Gray500
                } else {
                    Color.Transparent
                },
                RoundedCornerShape(80.dp),
            )
            .border(
                if (isSelected) {
                    1.dp
                } else {
                    0.dp
                },
                Gray400,
                RoundedCornerShape(80.dp),
            )
            .clickable { onSelect.invoke() },
    ) {
        Text(
            text = text,
            style = StaticTypeScale.Default.body6,
            color = if (isSelected) {
                WeSpotThemeManager.colors.abledTxtColor
            } else {
                WeSpotThemeManager.colors.disableIcnColor
            },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
        )
    }
}
