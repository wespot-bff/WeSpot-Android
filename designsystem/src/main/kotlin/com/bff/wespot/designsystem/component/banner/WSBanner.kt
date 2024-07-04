package com.bff.wespot.designsystem.component.banner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Accessible
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.bff.wespot.common.StringSet
import com.bff.wespot.designsystem.R
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.designsystem.util.OrientationPreviews

@Composable
fun WSBannerType(
    title: String,
    subTitle: String? = null,
    bannerType: WSBannerType = WSBannerType.Primary
) {
    WSBanner(
        title = title,
        subTitle = subTitle,
        bannerType = bannerType
    )
}

@Composable
fun WSBanner(
    title: String,
    icon: ImageVector,
    subTitle: String? = null,
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
    title: String,
    icon: ImageBitmap,
    subTitle: String? = null,
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
    title: String,
    icon: Painter? = null,
    subTitle: String? = null,
    bannerType: WSBannerType = WSBannerType.Primary
) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .height(80.dp)
            .fillMaxWidth()
            .clip(WeSpotThemeManager.shapes.medium)
            .background(bannerType.backgroundColor())
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let {
                Icon(
                    painter = icon,
                    contentDescription = StringSet.BANNER_ICON,
                    modifier = Modifier
                        .padding(start = 20.dp, top = 20.dp, bottom = 20.dp, end = 12.dp),
                )
            }

            Column(
                modifier = Modifier
                    .padding(vertical = 18.dp)
                    .padding(start = if (icon != null) 0.dp else 20.dp)
            ) {
                Text(
                    text = title,
                    color = bannerType.titleColor(),
                    style = bannerType.titleTextStyle()
                )

                subTitle?.let { subTitle ->
                    Text(
                        text = subTitle,
                        color = bannerType.subTitleColor().copy(alpha = 0.8f),
                        style = bannerType.subTitleTextStyle()
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.right_arrow),
                    contentDescription = StringSet.ARROW_ICON,
                    tint = WeSpotThemeManager.colors.abledIconColor,
                    modifier = Modifier
                        .padding(end = 24.dp)
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
        override fun backgroundColor() = WeSpotThemeManager.colors.modalColor

        @Composable
        override fun titleColor() = WeSpotThemeManager.colors.txtTitleColor

        @Composable
        override fun subTitleColor(): Color = WeSpotThemeManager.colors.primaryColor

        @Composable
        override fun titleTextStyle(): TextStyle = StaticTypeScale.Default.body4

        @Composable
        override fun subTitleTextStyle(): TextStyle = StaticTypeScale.Default.body7
    }

    data object Secondary : WSBannerType {
        @Composable
        override fun backgroundColor() = WeSpotThemeManager.colors.tertiaryBtnColor

        @Composable
        override fun titleColor() = WeSpotThemeManager.colors.abledTxtColor

        @Composable
        override fun subTitleColor(): Color = WeSpotThemeManager.colors.disableBtnTxtColor

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
            Column {
                WSBanner(
                    icon = Icons.AutoMirrored.Default.Send,
                    title = StringSet.MESSAGE_ARRIVED,
                    subTitle = StringSet.OPEN_INBOX_FOR_MESSAGE,
                    bannerType = WSBannerType.Primary
                )

                WSBanner(
                    icon = Icons.AutoMirrored.Default.Accessible,
                    title = StringSet.MESSAGE_ARRIVED,
                )

                WSBanner(
                    icon = Icons.AutoMirrored.Default.Accessible,
                    title = StringSet.MESSAGE_ARRIVED,
                    bannerType = WSBannerType.Secondary
                )

                WSBanner(
                    title = StringSet.MESSAGE_ARRIVED,
                    subTitle = StringSet.OPEN_INBOX_FOR_MESSAGE,
                    bannerType = WSBannerType.Secondary
                )
            }
        }
    }
}