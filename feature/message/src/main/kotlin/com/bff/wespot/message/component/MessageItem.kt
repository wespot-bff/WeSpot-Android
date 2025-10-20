package com.bff.wespot.message.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.bff.wespot.designsystem.theme.Gray300
import com.bff.wespot.designsystem.theme.Gray400
import com.bff.wespot.designsystem.theme.Gray600
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
    itemType: MessageItemType,
    message: Message,
    itemClick: () -> Unit,
    optionButtonClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.0.dp))
            .fillMaxWidth()
            .background(WeSpotThemeManager.colors.cardBackgroundColor)
            .clickableSingle { itemClick() },
    ) {
        if (message.isExistsUnreadMessage) {
            RedDot(
                modifier = Modifier
                    .zIndex(1f)
                    .padding(top = 14.dp, start = 14.dp),
                size = 8.dp,
            )
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            MessageUserItem(
                imageUrl = message.senderProfile.iconUrl,
                type = stringResource(R.string.me),
                name = message.senderProfile.name,
                isBookmarked = false,
            )

            Icon(
                modifier = Modifier
                    .padding(start = 8.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.message_room_arrow),
                tint = WeSpotThemeManager.colors.primaryColor,
                contentDescription = "Message Room Arrow",
            )

            /** 에버의 경우 이름만 노출한다. */
            MessageUserItem(
                imageUrl = message.receiverProfile.iconUrl,
                type = if (message.receiverProfile.isAnonymous) {
                    stringResource(R.string.anonymous)
                } else {
                    stringResource(R.string.real_name)
                },
                name = message.receiverProfile.name,
                isBookmarked = message.isBookmarked,
            )
        }

        Box(
            modifier = Modifier
                .padding(top = 16.dp, end = 16.dp)
                .align(Alignment.TopEnd),
        ) {
            when (itemType) {
                MessageItemType.Normal -> {
                    Icon(
                        modifier = Modifier
                            .clickableSingle { optionButtonClick() },
                        imageVector = ImageVector.vectorResource(id = R.drawable.option),
                        tint = Gray300,
                        contentDescription = stringResource(id = R.string.option_button),
                    )
                }
                is MessageItemType.Blocked -> {
                    BlockStateChip(
                        isBlocked = itemType.isBlocked,
                        onClick = optionButtonClick,
                    )
                }
                /** 에버 쪽지의 경우, 삭제를 막기 위해 옵션 버튼을 노출하지 않는다. */
                MessageItemType.Ever -> { }
            }
        }

        message.latestChatTime?.let {
            Text(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 16.dp, end = 16.dp),
                text = it.toStringWithDotSeparator(),
                style = StaticTypeScale.Default.body9,
                color = WeSpotThemeManager.colors.txtSubColor,
            )
        }
    }
}

@Composable
private fun MessageUserItem(
    imageUrl: String,
    type: String,
    name: String,
    isBookmarked: Boolean,
) {
    Row(
        modifier = Modifier.height(45.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProfileCircleImage(
            size = 34.dp,
            imageUrl = imageUrl,
            contentDescription = "Message Profile Image",
        )

        Column(
            modifier = Modifier.padding(start = 14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Box(
                modifier = Modifier
                    .clip(WeSpotThemeManager.shapes.extraLarge)
                    .background(WeSpotThemeManager.colors.bottomSheetColor)
                    .padding(horizontal = 8.dp, vertical = 1.dp),
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (isBookmarked) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.bookmark),
                            tint = WeSpotThemeManager.colors.primaryColor,
                            contentDescription = "Bookmarked User",
                        )
                    }

                    Text(
                        text = type,
                        color = WeSpotThemeManager.colors.txtTitleColor,
                        style = StaticTypeScale.Default.body9,
                    )
                }
            }

            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 78.dp),
                text = name,
                color = WeSpotThemeManager.colors.txtTitleColor,
                style = StaticTypeScale.Default.body6,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun BlockStateChip(
    isBlocked: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .clip(WeSpotThemeManager.shapes.extraLarge)
            .clickable { onClick() }
            .let {
                if (isBlocked) {
                    it.background(WeSpotThemeManager.colors.secondaryBtnColor)
                } else {
                    it.background(Gray600)
                }
            },
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            text = stringResource(
                id = if (isBlocked) {
                    R.string.unblock
                } else {
                    R.string.unblock_done
                },
            ),
            style = StaticTypeScale.Default.body9,
            color = if (isBlocked) {
                Color(0xFFF7F7F8)
            } else {
                Gray400
            },
        )
    }
}

sealed interface MessageItemType {
    data object Normal : MessageItemType

    data class Blocked(
        val isBlocked: Boolean,
    ) : MessageItemType

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
                itemType = MessageItemType.Normal,
                message = Message(),
                itemClick = { },
                optionButtonClick = { },
            )
        }
    }
}
