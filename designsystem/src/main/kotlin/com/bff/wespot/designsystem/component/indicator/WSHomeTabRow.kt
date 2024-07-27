package com.bff.wespot.designsystem.component.indicator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.designsystem.util.OrientationPreviews
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun WSHomeTabRow(
    selectedTabIndex: Int,
    tabList: ImmutableList<String>,
    onTabSelected: (Int) -> Unit,
) {
    val paddingModifier = when (selectedTabIndex) {
        0 -> Modifier.padding(start = 20.dp)
        1 -> Modifier.padding(end = 20.dp)
        else -> Modifier
    }

    TabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = WeSpotThemeManager.colors.backgroundColor,
        contentColor = WeSpotThemeManager.colors.disableIcnColor,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                    .then(paddingModifier),
                color = WeSpotThemeManager.colors.abledTxtColor,
            )
        },
    ) {
        tabList.forEachIndexed { index, tab ->
            val selected = selectedTabIndex == index
            Tab(
                selected = selected,
                onClick = { onTabSelected(index) },
                modifier = Modifier
                    .padding(vertical = 11.dp)
                    .then(paddingModifier),
            ) {
                Text(
                    text = tab,
                    style = StaticTypeScale.Default.body3,
                    color = if (selected) {
                        WeSpotThemeManager.colors.abledTxtColor
                    } else {
                        WeSpotThemeManager.colors.disableIcnColor
                    },
                )
            }
        }
    }
}

@OrientationPreviews
@Composable
private fun WSTopBarPreview() {
    WeSpotTheme {
        Surface(modifier = Modifier.fillMaxWidth()) {
            Column {
                WSHomeTabRow(
                    selectedTabIndex = 0,
                    tabList = persistentListOf("쪽지 홈", "내 쪽지함"),
                    onTabSelected = { },
                )
            }
        }
    }
}
