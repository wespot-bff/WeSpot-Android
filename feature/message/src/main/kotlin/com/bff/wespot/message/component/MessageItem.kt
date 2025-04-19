package com.bff.wespot.message.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.bff.wespot.designsystem.theme.Gray300
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.message.R
import com.bff.wespot.message.common.toStringWithDotSeparator
import com.bff.wespot.model.message.response.Message
import com.bff.wespot.ui.component.ProfileCircleImage
import com.bff.wespot.ui.component.RedDot
import com.bff.wespot.ui.util.clickableSingle

@Composable
internal fun MessageItem(
    messageItemType: MessageItemType,
    message: Message,
    itemClick: () -> Unit,
    optionButtonClick: () -> Unit,
    favoritesButtonClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .clip(WeSpotThemeManager.shapes.medium)
            .size(width = 160.dp, height = 170.dp)
            .background(WeSpotThemeManager.colors.cardBackgroundColor)
            .clickable { itemClick() },
    ) {
        /** 에버 쪽지의 경우, 삭제를 막기 위해 옵션 버튼을 노출하지 않는다. */
        if (messageItemType !is MessageItemType.Ever) {
            Icon(
                modifier = Modifier
                    .padding(top = 4.dp, end = 4.dp)
                    .align(Alignment.TopEnd)
                    .clickableSingle { optionButtonClick() },
                imageVector = ImageVector.vectorResource(id = R.drawable.option),
                tint = Gray300,
                contentDescription = stringResource(id = R.string.option_button),
            )
        }

        if (messageItemType is MessageItemType.Normal) {
            Icon(
                modifier = Modifier
                    .padding(bottom = 8.dp, end = 8.dp)
                    .align(Alignment.BottomEnd)
                    .clickableSingle { favoritesButtonClick() },
                imageVector = ImageVector.vectorResource(
                    if (messageItemType.isFavorites) {
                        R.drawable.favorites
                    } else {
                        R.drawable.unselected_favorites
                    },
                ),
                tint = Gray300,
                contentDescription = stringResource(id = R.string.favorites_button),
            )
        }

        Column(
            modifier = Modifier.padding(top = 18.dp, bottom = 12.dp, start = 14.dp, end = 14.dp),
        ) {
            Box(
                modifier = Modifier.size(50.dp),
            ) {
                if (message.isExistsUnreadMessage.not()) {
                    RedDot(
                        modifier = Modifier
                            .padding(top = 2.dp, end = 2.dp)
                            .align(Alignment.TopEnd)
                            .zIndex(1f),
                        size = 4.dp,
                    )
                }

                when (messageItemType) {
                    MessageItemType.Reported, MessageItemType.Blocked -> {
                        Image(
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.Center),
                            painter = painterResource(id = R.drawable.restrict),
                            contentDescription = stringResource(R.string.restrict_message_icon),
                        )
                    }

                    else -> {
                        ProfileCircleImage(
                            size = 50.dp,
                            imageUrl = message.thumbnail,
                            contentDescription = "Message Receiver Profile Image",
                        )
                    }
                }
            }

            when (messageItemType) {
                MessageItemType.Blocked -> {
                    MessageItemTitle(
                        stringResource(id = R.string.blocked_message_title),
                    )
                }

                MessageItemType.Reported -> {
                    MessageItemTitle(
                        stringResource(id = R.string.reported_message_title),
                    )
                }

                else -> {
                    MessageItemTitle(message.name)
                }
            }

            message.latestChatTime?.let {
                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = it.toStringWithDotSeparator(),
                    style = StaticTypeScale.Default.body9,
                    color = WeSpotThemeManager.colors.txtSubColor,
                )
            }
        }
    }
}

@Composable
private fun MessageItemTitle(
    title: String,
) {
    Text(
        modifier = Modifier.padding(top = 18.dp),
        text = title,
        style = StaticTypeScale.Default.body6,
        color = WeSpotThemeManager.colors.txtTitleColor,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
    )
}

sealed interface MessageItemType {
    data class Normal(val isFavorites: Boolean) : MessageItemType

    data object Favorites : MessageItemType

    data object Blocked : MessageItemType

    data object Reported : MessageItemType

    data object Ever : MessageItemType
}

@Preview
@Composable
private fun PreviewMessageItem() {
    WeSpotTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            MessageItem(
                messageItemType = MessageItemType.Normal(isFavorites = true),
                message = Message(),
                itemClick = { },
                optionButtonClick = { },
                favoritesButtonClick = { },
            )

            MessageItem(
                messageItemType = MessageItemType.Normal(isFavorites = false),
                message = Message(),
                itemClick = { },
                optionButtonClick = { },
                favoritesButtonClick = { },
            )

            MessageItem(
                messageItemType = MessageItemType.Reported,
                message = Message(),
                itemClick = { },
                optionButtonClick = { },
                favoritesButtonClick = { },
            )

            MessageItem(
                messageItemType = MessageItemType.Blocked,
                message = Message(),
                itemClick = { },
                optionButtonClick = { },
                favoritesButtonClick = { },
            )

            MessageItem(
                messageItemType = MessageItemType.Favorites,
                message = Message(),
                itemClick = { },
                optionButtonClick = { },
                favoritesButtonClick = { },
            )
        }
    }
}
