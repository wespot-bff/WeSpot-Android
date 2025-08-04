package com.bff.wespot.community.component

import androidx.compose.runtime.Composable
import coil.compose.rememberAsyncImagePainter
import com.bff.wespot.community.uimodel.BannerItemUiModel.BannerContentUiModel
import com.bff.wespot.designsystem.component.banner.WSBanner
import com.bff.wespot.designsystem.component.banner.WSBannerType

@Composable
internal fun BannerContentUiModel.Item() {
    WSBanner(
        icon = rememberAsyncImagePainter(model = icon.url),
        title = title.text,
        subTitle = description.text,
        bannerType = WSBannerType.Primary,
    )
}
