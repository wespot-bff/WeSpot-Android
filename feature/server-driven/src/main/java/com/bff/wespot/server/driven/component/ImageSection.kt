package com.bff.wespot.server.driven.component

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bff.wespot.ui.util.autoTilt

@Composable
internal fun ImageSection(
    imageUrl: String,
    width: Int,
    height: Int,
    enableTilt: Boolean = true,
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = Modifier
            .size(width = width.dp, height = height.dp)
            .autoTilt(enableTilt),
    )
}
