package com.bff.wespot.ui

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.theme.WeSpotThemeManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WSBottomSheet(
    closeSheet: () -> Unit,
    sheetState: SheetState = rememberStandardBottomSheetState(),
    content: @Composable () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = closeSheet,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),
        containerColor = WeSpotThemeManager.colors.bottomSheetColor,
        dragHandle = null,
        modifier = Modifier.navigationBarsPadding(),
    ) {
        content.invoke()
    }
}
