package com.bff.wespot.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager

@Composable
fun WSHomeChipGroup(
    items: List<String>,
    selectedItemIndex: Int = 0,
    onSelectedChanged: (Int) -> Unit = {},
) {
    LazyRow(
        modifier = Modifier.padding(vertical = 12.dp, horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(items.size) { index: Int ->
            FilterChip(
                shape = WeSpotThemeManager.shapes.extraLarge,
                selected = index == selectedItemIndex,
                onClick = {
                    onSelectedChanged(index)
                },
                label = {
                    Text(
                        text = items[index],
                        style = StaticTypeScale.Default.body6,
                    )
                },
                border = if (index != selectedItemIndex) {
                    BorderStroke(
                        width = 1.dp,
                        color = WeSpotThemeManager.colors.disableIcnColor,
                    )
                } else {
                    null
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = WeSpotThemeManager.colors.backgroundColor,
                    labelColor = WeSpotThemeManager.colors.disableIcnColor,
                    selectedContainerColor = WeSpotThemeManager.colors.secondaryBtnColor,
                    selectedLabelColor = Color(0xFFF7F7F8),
                ),
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    WeSpotTheme {
        Column {
            WSHomeChipGroup(
                items = listOf("받은 쪽지", "보낸 쪽지"),
                selectedItemIndex = 1,
                onSelectedChanged = { },
            )
        }
    }
}
