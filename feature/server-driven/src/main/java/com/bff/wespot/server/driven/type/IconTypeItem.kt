package com.bff.wespot.server.driven.type

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import coil.compose.AsyncImage
import com.bff.wespot.model.serverDriven.type.IconType

@Composable
fun IconType.Icon(
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = url,
        colorFilter = ColorFilter.tint(color.color()),
        modifier = modifier,
        contentDescription = null,
    )
}
