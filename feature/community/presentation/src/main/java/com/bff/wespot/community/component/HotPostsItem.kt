package com.bff.wespot.community.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bff.wespot.community.uimodel.HotPostItemUiModel
import com.bff.wespot.community.uimodel.HotPostItemUiModel.HotPostContentUiModel
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.White
import com.bff.wespot.model.serverDriven.type.ColorType
import com.bff.wespot.model.serverDriven.type.GradationType
import com.bff.wespot.model.serverDriven.type.IconType
import com.bff.wespot.model.serverDriven.type.ImageType
import com.bff.wespot.model.serverDriven.type.RichTextType
import com.bff.wespot.server.driven.type.Icon
import com.bff.wespot.server.driven.type.Text
import com.bff.wespot.server.driven.type.toBrush

@Composable
internal fun HotPostContentUiModel.Item(
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            title.icon.Icon(modifier = Modifier.size(24.dp))
            title.text.Text(StaticTypeScale.Default.body2)
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(posts) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(it.gradation.toBrush(), RoundedCornerShape(16.dp)),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        it.headerSection.Item()
                        Spacer(modifier = Modifier.height(8.dp))
                        it.infoSection.Item()
                        Spacer(modifier = Modifier.height(12.dp))
                        it.createdAt.Text(StaticTypeScale.Default.body11)
                    }
                }
            }
        }
    }
}

@Composable
private fun HotPostContentUiModel.PostUiModel.HeaderSectionUiModel.Item() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(24.dp)
                .background(White, CircleShape),
        ) {
            AsyncImage(
                model = profileImage.url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }

        nickname.Text(StaticTypeScale.Default.badge)
    }
}

@Composable
private fun HotPostContentUiModel.PostUiModel.InfoSectionUiModel.Item() {
    Column {
        title?.Text(
            textStyle = StaticTypeScale.Default.body5,
            maxLines = 1,
        )
        description.Text(
            textStyle = StaticTypeScale.Default.body7,
            maxLines = if (title?.text != null) {
                1
            } else {
                2
            },
        )
    }
}

private object HotPostItemPreviewData {
    val sampleHotPostItem = HotPostItemUiModel(
        id = "hotpost_1",
        content = HotPostContentUiModel(
            title = HotPostContentUiModel.TitleUiModel(
                icon = IconType(
                    url = "https://cdn-icons-png.flaticon.com/128/3524/3524335.png",
                    color = ColorType.Token("primary300"),
                ),
                text = RichTextType(
                    text = "🔥 인기 게시물",
                    color = ColorType.Token("white"),
                    typography = "body2",
                ),
            ),
            posts = listOf(
                HotPostContentUiModel.PostUiModel(
                    headerSection = HotPostContentUiModel.PostUiModel.HeaderSectionUiModel(
                        profileImage = ImageType(
                            url = "https://via.placeholder.com/24",
                            width = 24,
                            height = 24,
                        ),
                        nickname = RichTextType(
                            text = "김개발자",
                            color = ColorType.Token("white"),
                            typography = "badge",
                        ),
                    ),
                    infoSection = HotPostContentUiModel.PostUiModel.InfoSectionUiModel(
                        title = RichTextType(
                            text = "React 18 새로운 기능들",
                            color = ColorType.Token("white"),
                            typography = "body5",
                        ),
                        description = RichTextType(
                            text = "React 18에서 추가된 Concurrent Features에 대해 알아봅시다",
                            color = ColorType.Token("white"),
                            typography = "body7",
                        ),
                    ),
                    createdAt = RichTextType(
                        text = "1시간 전",
                        color = ColorType.Token("gray200"),
                        typography = "body11",
                    ),
                    gradation = GradationType(
                        startColor = ColorType.Token("primary200"),
                        endColor = ColorType.Token("primary500"),
                        angle = 45,
                    ),
                ),
                HotPostContentUiModel.PostUiModel(
                    headerSection = HotPostContentUiModel.PostUiModel.HeaderSectionUiModel(
                        profileImage = ImageType(
                            url = "https://via.placeholder.com/24",
                            width = 24,
                            height = 24,
                        ),
                        nickname = RichTextType(
                            text = "박학생",
                            color = ColorType.Token("white"),
                            typography = "badge",
                        ),
                    ),
                    infoSection = HotPostContentUiModel.PostUiModel.InfoSectionUiModel(
                        title = RichTextType(
                            text = "알고리즘 스터디 모집",
                            color = ColorType.Token("white"),
                            typography = "body5",
                        ),
                        description = RichTextType(
                            text = "같이 코딩테스트 준비하실 분들 구합니다!",
                            color = ColorType.Token("white"),
                            typography = "body7",
                        ),
                    ),
                    createdAt = RichTextType(
                        text = "3시간 전",
                        color = ColorType.Token("gray200"),
                        typography = "body11",
                    ),
                    gradation = GradationType(
                        startColor = ColorType.Token("gray400"),
                        endColor = ColorType.Token("gray700"),
                        angle = 135,
                    ),
                ),
                HotPostContentUiModel.PostUiModel(
                    headerSection = HotPostContentUiModel.PostUiModel.HeaderSectionUiModel(
                        profileImage = ImageType(
                            url = "https://via.placeholder.com/24",
                            width = 24,
                            height = 24,
                        ),
                        nickname = RichTextType(
                            text = "이디자이너",
                            color = ColorType.Token("white"),
                            typography = "badge",
                        ),
                    ),
                    infoSection = HotPostContentUiModel.PostUiModel.InfoSectionUiModel(
                        title = RichTextType(
                            text = "UI/UX 트렌드 2024",
                            color = ColorType.Token("white"),
                            typography = "body5",
                        ),
                        description = RichTextType(
                            text = "올해 주목해야 할 디자인 트렌드들을 정리해봤어요",
                            color = ColorType.Token("white"),
                            typography = "body7",
                        ),
                    ),
                    createdAt = RichTextType(
                        text = "5시간 전",
                        color = ColorType.Token("gray200"),
                        typography = "body11",
                    ),
                    gradation = GradationType(
                        startColor = ColorType.Token("primary100"),
                        endColor = ColorType.Token("primary300"),
                        angle = 90,
                    ),
                ),
            ),
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun HotPostItemPreview() {
    WeSpotTheme {
        Surface {
            HotPostItemPreviewData.sampleHotPostItem.content.Item()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HotPostItemWithPaddingPreview() {
    WeSpotTheme {
        Surface {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                HotPostItemPreviewData.sampleHotPostItem.content.Item()
            }
        }
    }
}
