package com.bff.wespot.community.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bff.wespot.community.presentation.R
import com.bff.wespot.community.uimodel.chip.BaseChipUiModel
import com.bff.wespot.community.uimodel.chip.FilterChipUiModel
import com.bff.wespot.designsystem.theme.Gray200
import com.bff.wespot.designsystem.theme.Gray700
import com.bff.wespot.designsystem.theme.Gray900
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.model.serverDriven.type.ColorType
import com.bff.wespot.model.serverDriven.type.IconType
import com.bff.wespot.model.serverDriven.type.RichTextType
import com.bff.wespot.server.driven.type.Icon
import com.bff.wespot.server.driven.type.toTextStyle
import com.bff.wespot.ui.util.clickableSingle

@Composable
internal fun FilterChip(
    selectedId: String,
    filterChips: List<BaseChipUiModel>,
    onChipClicked: (id: String, target: String) -> Unit,
    onMoreClicked: () -> Unit,
    onSameChipClicked: () -> Unit,
) {
    val lazyListState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp),
            state = lazyListState,
        ) {
            items(filterChips) {
                val selected = selectedId == it.id

                when (it) {
                    is FilterChipUiModel -> {
                        Row(
                            modifier = Modifier
                                .background(
                                    color = if (selected) {
                                        Gray200
                                    } else {
                                        Gray700
                                    },
                                    shape = RoundedCornerShape(80.dp),
                                ).clickableSingle {
                                    if (selected) {
                                        onSameChipClicked.invoke()
                                    } else {
                                        onChipClicked(it.id, it.target)
                                    }
                                },
                        ) {
                            it.icon.Icon()

                            Text(
                                text = it.text.text,
                                style = it.text.typography.toTextStyle()
                                    ?: StaticTypeScale.Default.body6,
                                color = if (selected) {
                                    WeSpotThemeManager.colors.backgroundColor
                                } else {
                                    WeSpotThemeManager.colors.txtSubColor
                                },
                                maxLines = 1,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                            )
                        }
                    }
                }
            }
        }

        if (!lazyListState.isScrolledToTheEnd()) {
            Box(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .fillMaxHeight()
                    .align(Alignment.CenterEnd)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Gray900,
                            ),
                            startX = 0f,
                            endX = 100f,
                        ),
                    ).width(70.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            color = WeSpotThemeManager.colors.cardBackgroundColor,
                            shape = CircleShape,
                        ).size(30.dp)
                        .clickableSingle {
                            onMoreClicked.invoke()
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.down_arrow),
                        modifier = Modifier.size(20.dp),
                        contentDescription = null,
                        tint = WeSpotThemeManager.colors.txtSubColor,
                    )
                }
            }
        }
    }
}

private fun LazyListState.isScrolledToTheEnd() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

private object FilterChipPreviewData {
    val sampleFilterChips = listOf(
        FilterChipUiModel(
            id = "all",
            icon = IconType(
                url = "https://cdn-icons-png.flaticon.com/128/3524/3524335.png",
                color = ColorType.Token("gray300"),
            ),
            text = RichTextType(
                text = "전체",
                color = ColorType.Token("gray300"),
                typography = "body6",
            ),
            target = "all",
        ),
        FilterChipUiModel(
            id = "question",
            icon = IconType(
                url = "https://cdn-icons-png.flaticon.com/128/3524/3524335.png",
                color = ColorType.Token("gray300"),
            ),
            text = RichTextType(
                text = "질문",
                color = ColorType.Token("gray300"),
                typography = "body6",
            ),
            target = "question",
        ),
        FilterChipUiModel(
            id = "free",
            icon = IconType(
                url = "https://cdn-icons-png.flaticon.com/128/3524/3524335.png",
                color = ColorType.Token("gray300"),
            ),
            text = RichTextType(
                text = "자유",
                color = ColorType.Token("gray300"),
                typography = "body6",
            ),
            target = "free",
        ),
        FilterChipUiModel(
            id = "study",
            icon = IconType(
                url = "https://cdn-icons-png.flaticon.com/128/3524/3524335.png",
                color = ColorType.Token("gray300"),
            ),
            text = RichTextType(
                text = "스터디",
                color = ColorType.Token("gray300"),
                typography = "body6",
            ),
            target = "study",
        ),
        FilterChipUiModel(
            id = "project",
            icon = IconType(
                url = "https://cdn-icons-png.flaticon.com/128/3524/3524335.png",
                color = ColorType.Token("gray300"),
            ),
            text = RichTextType(
                text = "프로젝트",
                color = ColorType.Token("gray300"),
                typography = "body6",
            ),
            target = "project",
        ),
        FilterChipUiModel(
            id = "event",
            icon = IconType(
                url = "https://cdn-icons-png.flaticon.com/128/3524/3524335.png",
                color = ColorType.Token("gray100"),
            ),
            text = RichTextType(
                text = "이벤트",
                color = ColorType.Token("gray100"),
                typography = "body6",
            ),
            target = "event",
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun FilterChipDefaultPreview() {
    WeSpotTheme {
        Surface {
            FilterChip(
                selectedId = "all",
                filterChips = FilterChipPreviewData.sampleFilterChips,
                onChipClicked = { _, _ -> },
                onMoreClicked = { },
                onSameChipClicked = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterChipSelectedPreview() {
    WeSpotTheme {
        Surface(
            modifier = Modifier.fillMaxWidth(),
        ) {
            FilterChip(
                selectedId = "question",
                filterChips = FilterChipPreviewData.sampleFilterChips,
                onChipClicked = { _, _ -> },
                onMoreClicked = { },
                onSameChipClicked = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterChipWithPaddingPreview() {
    WeSpotTheme {
        Surface {
            FilterChip(
                selectedId = "study",
                filterChips = FilterChipPreviewData.sampleFilterChips,
                onChipClicked = { _, _ -> },
                onMoreClicked = { },
                onSameChipClicked = {},
            )
        }
    }
}
