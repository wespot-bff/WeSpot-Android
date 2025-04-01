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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bff.wespot.designsystem.theme.WeSpotThemeManager

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
                .error(com.bff.wespot.designsystem.R.drawable.default_profile)
                .fallback(com.bff.wespot.designsystem.R.drawable.default_profile)
                .placeholder(com.bff.wespot.designsystem.R.drawable.default_profile)
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
        )
    }
}
