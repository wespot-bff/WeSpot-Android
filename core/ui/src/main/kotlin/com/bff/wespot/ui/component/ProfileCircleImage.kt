package com.bff.wespot.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import coil3.request.fallback
import coil3.request.placeholder
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.ui.R

@Composable
fun ProfileCircleImage(
    size: Dp,
    imageUrl: String,
    contentDescription: String,
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(WeSpotThemeManager.colors.cardBackgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        AsyncImage(
            modifier = Modifier.size(size),
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .error(R.drawable.default_profile)
                .fallback(R.drawable.default_profile)
                .placeholder(R.drawable.default_profile)
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
        )
    }
}
