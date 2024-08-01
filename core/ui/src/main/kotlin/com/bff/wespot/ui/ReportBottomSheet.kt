package com.bff.wespot.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.theme.StaticTypeScale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportBottomSheet(
    closeSheet: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    ),
    options: List<String>,
    optionsClickable: List<() -> Unit>,
) {
    require(options.size == optionsClickable.size) {
        "options and optionsClickable must have the same size"
    }

    WSBottomSheet(closeSheet = closeSheet, sheetState = sheetState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 28.dp),
        ) {
            options.forEachIndexed { index, option ->
                ReportSection(text = option, onClick = optionsClickable[index])
                if (index != options.size - 1) {
                    HorizontalDivider(
                        color = Color(0xFF4F5157),
                    )
                }
            }
        }
    }
}

@Composable
private fun ReportSection(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick.invoke() }
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = text, style = StaticTypeScale.Default.body4)
    }
}
