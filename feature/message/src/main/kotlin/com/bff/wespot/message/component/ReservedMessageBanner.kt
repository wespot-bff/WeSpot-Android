package com.bff.wespot.message.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.bff.wespot.designsystem.component.banner.WSBanner
import com.bff.wespot.designsystem.component.banner.WSBannerType
import com.bff.wespot.message.R
import com.bff.wespot.model.message.response.MessageStatus

@Composable
internal fun ReservedMessageBanner(
    paddingValues: PaddingValues,
    messageStatus: MessageStatus,
    onBannerClick: () -> Unit,
) {
    AnimatedVisibility(
        modifier = Modifier.padding(paddingValues),
        visible = messageStatus.hasReservedMessages() && messageStatus.countRemainingMessages >= 0,
        enter = slideInVertically { initialOffsetY -> -initialOffsetY },
    ) {
        if (messageStatus.hasRemainingMessages()) {
            WSBanner(
                title = stringResource(
                    R.string.reserved_message_banner_title,
                    messageStatus.getReservedMessageCount(),
                ),
                subTitle = stringResource(
                    R.string.reserved_message_banner_subtitle,
                    messageStatus.countRemainingMessages,
                ),
                image = painterResource(id = R.drawable.reserved_message),
                onBannerClick = { onBannerClick() },
                bannerType = WSBannerType.Primary,
            )
        } else if (messageStatus.hasNoRemainingMessages()) {
            WSBanner(
                title = stringResource(
                    R.string.reserved_message_banner_title_disabled,
                    messageStatus.getReservedMessageCount(),
                ),
                image = painterResource(id = R.drawable.reserved_message),
                onBannerClick = { onBannerClick() },
                bannerType = WSBannerType.Secondary,
            )
        }
    }
}
