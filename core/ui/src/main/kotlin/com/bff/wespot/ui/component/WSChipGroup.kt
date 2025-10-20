package com.bff.wespot.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.ui.R
import com.bff.wespot.ui.util.clickableSingle

@Composable
fun WSChipGroup(
    type: WSChipGroupType,
    selectedItemIndex: Int = 0,
    onSelectedChanged: (Int) -> Unit = {},
) {
    LazyRow(
        modifier = Modifier.padding(vertical = 12.dp, horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        when (type) {
            is WSChipGroupType.Normal -> {
                items(type.items.size) { index: Int ->
                    FilterChip(
                        shape = WeSpotThemeManager.shapes.extraLarge,
                        selected = index == selectedItemIndex,
                        onClick = {
                            onSelectedChanged(index)
                        },
                        label = {
                            Text(
                                text = type.items[index],
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

            is WSChipGroupType.LeadingIcon -> {
                items(type.items.size) { index: Int ->
                    WSIconChip(
                        item = type.items[index],
                        selected = selectedItemIndex == index,
                        onClick = {
                            onSelectedChanged(index)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun WSIconChip(
    item: WSChipGroupType.WSIconChipItem,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .clip(WeSpotThemeManager.shapes.extraLarge)
            .clickableSingle { onClick() }
            .background(
                if (selected) {
                    WeSpotThemeManager.colors.secondaryBtnColor
                } else {
                    WeSpotThemeManager.colors.backgroundColor
                },
            ).then(
                if (!selected) {
                    Modifier.border(
                        width = 1.dp,
                        color = WeSpotThemeManager.colors.disableIcnColor,
                        shape = WeSpotThemeManager.shapes.extraLarge,
                    )
                } else {
                    Modifier
                },
            ).padding(horizontal = 12.dp, vertical = 5.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = "${item.label} Chip",
                tint = if (selected) {
                    WeSpotThemeManager.colors.txtTitleColor
                } else {
                    WeSpotThemeManager.colors.disableIcnColor
                },
            )

            Text(
                text = item.label,
                style = StaticTypeScale.Default.body9,
                color = if (selected) {
                    WeSpotThemeManager.colors.txtTitleColor
                } else {
                    WeSpotThemeManager.colors.disableIcnColor
                },
            )
        }
    }
}

sealed interface WSChipGroupType {
    data class WSIconChipItem(
        val icon: ImageVector,
        val label: String,
    )

    data class Normal(
        val items: List<String>,
    ) : WSChipGroupType

    data class LeadingIcon(
        val items: List<WSIconChipItem>,
    ) : WSChipGroupType
}

@Preview
@Composable
private fun Preview() {
    WeSpotTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            WSChipGroup(
                WSChipGroupType.Normal(listOf("받은 쪽지", "보낸 쪽지")),
                selectedItemIndex = 1,
                onSelectedChanged = { },
            )

            WSChipGroup(
                WSChipGroupType.LeadingIcon(
                    listOf(
                        WSChipGroupType.WSIconChipItem(
                            ImageVector.vectorResource(id = R.drawable.exclude),
                            "전체",
                        ),
                        WSChipGroupType.WSIconChipItem(
                            ImageVector.vectorResource(id = R.drawable.exclude),
                            "전체",
                        ),
                    ),
                ),
            )
        }
    }
}
