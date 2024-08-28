package com.bff.wespot.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.designsystem.util.OrientationPreviews
import com.bff.wespot.util.hexToColor

@Composable
fun WSListItem(
    title: String,
    subTitle: String,
    selected: Boolean,
    backgroundColor: String = "",
    imageContent: @Composable () -> Unit,
    onClick: () -> Unit = { },
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 8.dp)
            .clip(WeSpotThemeManager.shapes.medium)
            .border(
                width = 1.dp,
                color = if (selected) {
                    WeSpotThemeManager.colors.primaryColor
                } else {
                    WeSpotThemeManager.colors.cardBackgroundColor
                },
                shape = WeSpotThemeManager.shapes.medium,
            )
            .background(WeSpotThemeManager.colors.cardBackgroundColor)
            .clickable { onClick.invoke() },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        if (backgroundColor.isNotEmpty()) {
                            hexToColor(backgroundColor)
                        } else {
                            WeSpotThemeManager.colors.cardBackgroundColor
                        },
                    ),
                contentAlignment = Alignment.Center,
            ) {
                imageContent()
            }

            Column(
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp, start = 16.dp, end = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = title,
                    style = StaticTypeScale.Default.body2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = subTitle,
                    style = StaticTypeScale.Default.body6,
                    color = WeSpotThemeManager.colors.txtSubColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 14.dp, top = 14.dp),
            contentAlignment = Alignment.TopEnd,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.exclude),
                contentDescription = "",
                tint = if (selected) {
                    WeSpotThemeManager.colors.primaryColor
                } else {
                    WeSpotThemeManager.colors.disableIcnColor
                },
            )
        }
    }
}

@OrientationPreviews
@Composable
private fun SchoolListItemPreview() {
    WeSpotTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column {
                repeat(10) {
                    WSListItem(
                        title = "김재연",
                        subTitle = "낙동고등학교 2학년 3반",
                        selected = false,
                        backgroundColor = "#FF5733",
                        imageContent = {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data("https://avatars.githubusercontent.com/u/89840550?v=4")
                                    .crossfade(true)
                                    .build(),
                                contentDescription = stringResource(R.string.user_character_image),
                            )
                        },
                        onClick = {},
                    )
                }
            }
        }
    }
}
