package com.bff.wespot.designsystem.component.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.R
import com.bff.wespot.designsystem.theme.Gray100
import com.bff.wespot.designsystem.theme.Gray300
import com.bff.wespot.designsystem.theme.Positive
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.designsystem.util.OrientationPreviews

@Composable
fun WSMessageItem(
    userInfo: String,
    date: String,
    wsMessageItemType: WSMessageItemType,
    itemClick: () -> Unit,
    optionButtonClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .clip(WeSpotThemeManager.shapes.medium)
            .size(width = 154.dp, height = 200.dp)
            .background(WeSpotThemeManager.colors.cardBackgroundColor)
            .clickable { itemClick() },
    ) {
        Column {
            WSLetterItemOptionButton(optionButtonClick)

            WSLetterItemContent(
                userInfo = userInfo,
                date = date,
                wsMessageItemType = wsMessageItemType,
            )
        }
    }
}

@Composable
private fun WSLetterItemOptionButton(
    optionButtonClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, end = 8.dp),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Icon(
            modifier = Modifier
                .clickable { optionButtonClick() },
            imageVector = ImageVector.vectorResource(id = R.drawable.option),
            contentDescription = stringResource(id = R.string.option_button),
        )
    }
}

@Composable
private fun WSLetterItemContent(
    userInfo: String,
    date: String,
    wsMessageItemType: WSMessageItemType,
) {
    Column(
        modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp),
    ) {
        Image(
            modifier = Modifier.size(width = 130.dp, height = 100.dp),
            bitmap = wsMessageItemType.letterImage(),
            contentDescription = stringResource(R.string.letter_image),
        )

        Column(
            modifier = Modifier.padding(top = 2.dp),
        ) {
            Text(
                text = wsMessageItemType.letterStatusText(),
                color = Gray100,
                style = StaticTypeScale.Default.body9,
            )

            Text(
                text = userInfo,
                color = Gray100,
                style = StaticTypeScale.Default.body9,
            )
        }

        Row(
            modifier = Modifier.padding(top = 16.dp),
        ) {
            Text(
                text = date,
                color = Gray300,
                style = StaticTypeScale.Default.body11,
            )

            wsMessageItemType.openStatusText()?.let {
                Text(
                    modifier = Modifier.weight(1f),
                    text = it,
                    color = Positive,
                    style = StaticTypeScale.Default.body11,
                    textAlign = TextAlign.End,
                )
            }
        }
    }
}

sealed interface WSMessageItemType {
    @Composable
    fun letterStatusText(): String

    @Composable
    fun openStatusText(): String?

    @Composable
    fun letterImage(): ImageBitmap

    data object UnreadReceivedMessage : WSMessageItemType {
        @Composable
        override fun letterStatusText(): String = stringResource(id = R.string.letter_receiver)

        @Composable
        override fun letterImage(): ImageBitmap = ImageBitmap.imageResource(id = R.drawable.closed_letter)

        @Composable
        override fun openStatusText(): String? = null
    }

    data object UnreadSentMessage : WSMessageItemType {
        @Composable
        override fun letterStatusText(): String = stringResource(id = R.string.letter_sender)

        @Composable
        override fun letterImage(): ImageBitmap =
            ImageBitmap.imageResource(id = R.drawable.closed_letter)

        @Composable
        override fun openStatusText(): String? = null
    }

    data object ReadReceivedMessage : WSMessageItemType {
        @Composable
        override fun letterStatusText(): String = stringResource(id = R.string.letter_receiver)

        @Composable
        override fun letterImage(): ImageBitmap =
            ImageBitmap.imageResource(id = R.drawable.opened_letter)

        @Composable
        override fun openStatusText(): String? = null
    }

    data object UnReadSentMessage : WSMessageItemType {
        @Composable
        override fun letterStatusText(): String = stringResource(id = R.string.letter_sender)

        @Composable
        override fun letterImage(): ImageBitmap =
            ImageBitmap.imageResource(id = R.drawable.opened_letter)

        @Composable
        override fun openStatusText(): String = stringResource(id = R.string.letter_opened)
    }
}

@OrientationPreviews
@Composable
private fun WSBannerPreview() {
    WeSpotTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                WSMessageItem(
                    userInfo = "역삼중 1학년 6반 김도현",
                    date = "2024.07.07",
                    wsMessageItemType = WSMessageItemType.UnReadSentMessage,
                    itemClick = { },
                    optionButtonClick = { },
                )

                WSMessageItem(
                    userInfo = "역삼중 1학년 6반 김도현",
                    date = "2024.07.07",
                    wsMessageItemType = WSMessageItemType.ReadReceivedMessage,
                    optionButtonClick = { },
                    itemClick = { },
                )

                WSMessageItem(
                    userInfo = "역삼중 1학년 6반 김도현",
                    date = "2024.07.07",
                    wsMessageItemType = WSMessageItemType.UnreadReceivedMessage,
                    optionButtonClick = { },
                    itemClick = { },
                )

                WSMessageItem(
                    userInfo = "역삼중 1학년 6반 김도현",
                    date = "2024.07.07",
                    wsMessageItemType = WSMessageItemType.UnreadSentMessage,
                    optionButtonClick = { },
                    itemClick = { },
                )
            }
        }
    }
}
