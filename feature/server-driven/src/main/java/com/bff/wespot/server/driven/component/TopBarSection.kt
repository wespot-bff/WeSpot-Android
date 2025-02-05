package com.bff.wespot.server.driven.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.bff.wespot.designsystem.component.header.WSTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TopBarSection(
    title: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
) {
    WSTopBar(
        title = title,
        canNavigateBack = canNavigateBack,
        navigateUp = navigateUp,
    )
}
