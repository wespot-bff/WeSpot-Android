package com.bff.wespot.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.bff.wespot.designsystem.component.indicator.WSToast
import com.bff.wespot.designsystem.component.indicator.WSToastType

@Composable
fun TopToast(
    message: String,
    toastType: WSToastType,
    showToast: Boolean,
    closeToast: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp)
            .zIndex(99f),
        contentAlignment = Alignment.TopCenter,
    ) {
        WSToast(
            text = message,
            showToast = showToast,
            toastType = toastType,
            closeToast = closeToast,
        )
    }
}
