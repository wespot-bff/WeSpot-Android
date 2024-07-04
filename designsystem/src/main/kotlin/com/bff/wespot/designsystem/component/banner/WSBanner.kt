package com.bff.wespot.designsystem.component.banner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.bff.wespot.common.StringSet
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.designsystem.util.OrientationPreviews

@Composable
fun WSBanner(
    icon: ImageVector,
    title: String,
    subTitle: String,
    bannerType: WSBannerType = WSBannerType.Primary
) {
    WSBanner(
        icon = rememberVectorPainter(image = icon),
        title = title,
        subTitle = subTitle,
        bannerType = bannerType
    )
}

@Composable
fun WSBanner(
    icon: ImageBitmap,
    title: String,
    subTitle: String,
    bannerType: WSBannerType = WSBannerType.Primary
) {
    WSBanner(
        icon = BitmapPainter(icon),
        title = title,
        subTitle = subTitle,
        bannerType = bannerType
    )
}


@Composable
fun WSBanner(
    icon: Painter,
    title: String,
    subTitle: String,
    bannerType: WSBannerType = WSBannerType.Primary
) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .wrapContentHeight()
            .fillMaxWidth()
            .clip(WeSpotThemeManager.shapes.medium)
            .background(bannerType.backgroundColor())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = icon,
                contentDescription = StringSet.BANNER_ICON,
                modifier = Modifier
                    .padding(start = 20.dp, top = 20.dp, bottom = 20.dp, end = 12.dp),
            )

            Column(
                modifier = Modifier.padding(vertical = 18.dp)
            ) {
                Text(
                    text = title,
                    color = bannerType.titleColor(),
                    style = bannerType.titleTextStyle()
                )

                Text(
                    text = subTitle,
                    color = bannerType.subTitleColor(),
                    style = bannerType.subTitleTextStyle()
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                    contentDescription = StringSet.RIGHT_ARROW,
                    tint = WeSpotThemeManager.colors.abledIconColor,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

    }
}

sealed interface WSBannerType {

    @Composable
    fun backgroundColor(): Color

    @Composable
    fun titleColor(): Color

    @Composable
    fun subTitleColor(): Color

    @Composable
    fun titleTextStyle(): TextStyle

    @Composable
    fun subTitleTextStyle(): TextStyle

    data object Primary : WSBannerType {
        @Composable
        override fun backgroundColor() = WeSpotThemeManager.colors.cardBackgroundColor

        @Composable
        override fun titleColor() = WeSpotThemeManager.colors.txtTitleColor

        @Composable
        override fun subTitleColor(): Color = WeSpotThemeManager.colors.primaryColor

        @Composable
        override fun titleTextStyle(): TextStyle = StaticTypeScale.Default.body4

        @Composable
        override fun subTitleTextStyle(): TextStyle = StaticTypeScale.Default.body7
    }
}

@OrientationPreviews
@Composable
private fun WSBannerPreview() {
    WeSpotTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            WSBanner(
                icon = Icons.AutoMirrored.Default.Send,
                title = StringSet.MESSAGE_ARRIVED,
                subTitle = StringSet.OPEN_INBOX_FOR_MESSAGE,
                bannerType = WSBannerType.Primary
            )
        }
    }
}