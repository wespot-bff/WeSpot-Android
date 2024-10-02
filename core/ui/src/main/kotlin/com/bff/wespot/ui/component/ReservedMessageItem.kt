package com.bff.wespot.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bff.wespot.designsystem.theme.Gray400
import com.bff.wespot.designsystem.theme.Gray600
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.ui.util.hexToColor

@Composable
fun ReservedMessageItem(
    title: String,
    subTitle: String,
    backgroundColor: String,
    iconUrl: String,
    chipText: String,
    chipEnabled: Boolean = true,
    chipDisabledText: String = "",
    onClick: () -> Unit,
) {
    Column {
        Row(
            modifier = Modifier.padding(start = 18.dp, end = 18.dp, top = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        runCatching {
                            hexToColor(backgroundColor)
                        }.getOrDefault(WeSpotThemeManager.colors.cardBackgroundColor),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(iconUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(com.bff.wespot.ui.R.string.user_character_image),
                )
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .weight(1f),
            ) {
                Text(
                    text = title,
                    style = StaticTypeScale.Default.body6,
                    color = WeSpotThemeManager.colors.txtSubColor,
                )

                Text(
                    text = subTitle,
                    style = StaticTypeScale.Default.body6,
                    color = WeSpotThemeManager.colors.txtTitleColor,
                )
            }

            Box(
                modifier = Modifier
                    .clip(WeSpotThemeManager.shapes.extraLarge)
                    .clickable { onClick() }
                    .let {
                        if (chipEnabled) {
                            it.background(WeSpotThemeManager.colors.secondaryBtnColor)
                        } else {
                            it.background(Gray600)
                        }
                    },
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    text = if (chipEnabled) chipText else chipDisabledText,
                    style = StaticTypeScale.Default.body9,
                    color = if (chipEnabled) {
                        Color(0xFFF7F7F8)
                    } else {
                        Gray400
                    },
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(top = 24.dp, start = 24.dp, end = 24.dp),
            thickness = 1.dp,
            color = WeSpotThemeManager.colors.cardBackgroundColor,
        )
    }
}
