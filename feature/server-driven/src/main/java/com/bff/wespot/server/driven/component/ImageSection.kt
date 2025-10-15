package com.bff.wespot.server.driven.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.bff.wespot.model.serverDriven.Paddings
import com.bff.wespot.server.driven.util.toPaddingValues
import com.bff.wespot.ui.util.autoTilt

@Composable
internal fun ImageSection(
    imageUrl: String,
    width: Int,
    height: Int,
    paddings: Paddings,
    enableTilt: Boolean = true,
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = Modifier
            .size(width = width.dp, height = height.dp)
            .padding(paddings.toPaddingValues())
            .autoTilt(enableTilt),
    )
}
