package com.bff.wespot.server.driven.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.component.button.WSButton

@Composable
internal fun ButtonSection(
    text: String,
    onClick: () -> Unit,
) {
    WSButton(onClick = onClick, text = text, paddingValues = PaddingValues(0.dp)) {
        it.invoke()
    }
}
