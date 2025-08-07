package com.bff.wespot.community.component

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.rememberAsyncImagePainter
import com.bff.wespot.community.uimodel.BannerItemUiModel.BannerContentUiModel
import com.bff.wespot.designsystem.component.banner.WSBanner
import com.bff.wespot.designsystem.component.banner.WSBannerType

@Composable
internal fun BannerContentUiModel.Item(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        WSBanner(
            icon = rememberAsyncImagePainter(model = icon.url),
            title = title.text,
            subTitle = description.text,
            bannerType = WSBannerType.Primary,
        )
    }
}
