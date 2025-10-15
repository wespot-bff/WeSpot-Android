package com.bff.wespot.community.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bff.wespot.community.presentation.R
import com.bff.wespot.community.uimodel.PostItemUiModel
import com.bff.wespot.community.uimodel.PostItemUiModel.PostContentUiModel
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.designsystem.theme.White
import com.bff.wespot.model.serverDriven.type.ColorType
import com.bff.wespot.model.serverDriven.type.IconType
import com.bff.wespot.model.serverDriven.type.ImageType
import com.bff.wespot.model.serverDriven.type.RichTextType
import com.bff.wespot.server.driven.type.Icon
import com.bff.wespot.server.driven.type.Text
import com.bff.wespot.ui.util.clickableSingle

@Composable
internal fun PostContentUiModel.Item(
    navigateToPost: () -> Unit,
) {
    Column(
        modifier = Modifier.clickableSingle {
            navigateToPost.invoke()
        },
    ) {
        HorizontalDivider(color = WeSpotThemeManager.colors.bottomSheetColor)

        Spacer(modifier = Modifier.height(24.dp))

        headerSection.Item()

        Spacer(modifier = Modifier.height(12.dp))

        infoSection.Item()

        Spacer(modifier = Modifier.height(16.dp))

        contentSection.Item()

        Spacer(modifier = Modifier.height(16.dp))

        footerSection.Item()

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun PostContentUiModel.HeaderSectionUiModel.Item() {
    Column {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(White, CircleShape),
            ) {
                AsyncImage(
                    model = profileImage,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Row {
                    category.text.Text(StaticTypeScale.Default.badge)

                    category.icon.Icon(
                        modifier = Modifier.size(16.dp),
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    nickname.Text(
                        textStyle = StaticTypeScale.Default.body6,
                    )

                    createdAt.Text(StaticTypeScale.Default.badge)
                }
            }
        }
    }
}

@Composable
private fun PostContentUiModel.InfoSectionUiModel.Item() {
    Column {
        title.Text(
            textStyle = StaticTypeScale.Default.body4,
            maxLines = 1,
        )

        Spacer(modifier = Modifier.height(4.dp))

        description.Text(StaticTypeScale.Default.body6)

        Spacer(modifier = Modifier.height(8.dp))

        seeMore.Text(
            textStyle = StaticTypeScale.Default.body6,
            maxLines = 1,
        )
    }
}

@Composable
private fun PostContentUiModel.ContentSectionUiModel.Item() {
    when (this) {
        is PostContentUiModel.ContentSectionUiModel.ImagesSectionUiModel -> {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(images) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .width(it.width.dp)
                            .height(it.height.dp)
                            .background(White, RoundedCornerShape(8.dp)),
                    ) {
                        AsyncImage(
                            model = it.url,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .width(it.width.dp)
                                .height(it.height.dp),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                        )
                    }
                }
            }
        }

        is PostContentUiModel.ContentSectionUiModel.EmptySectionUiModel -> {}
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PostContentUiModel.FooterSectionUiModel.Item() {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            reactions.forEach {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    it.icon.Icon(modifier = Modifier.size(14.dp))

                    it.count.Text(StaticTypeScale.Default.badge)
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            scrap.icon.Icon(
                modifier = Modifier.size(14.dp),
            )

            Text(
                text = stringResource(R.string.post_scrap),
                style = StaticTypeScale.Default.badge,
                color = WeSpotThemeManager.colors.txtSubColor,
            )
        }
    }
}

private object PostItemPreviewData {
    val samplePostItem = PostItemUiModel(
        id = "sample_post_1",
        content = PostContentUiModel(
            headerSection = PostContentUiModel.HeaderSectionUiModel(
                profileImage = "https://via.placeholder.com/36",
                nickname = RichTextType(
                    text = "김위스팟",
                    color = ColorType.Token("white"),
                    typography = "body6",
                ),
                createdAt = RichTextType(
                    text = "2시간 전",
                    color = ColorType.Token("Gray300"),
                    typography = "badge",
                ),
                category = PostContentUiModel.HeaderSectionUiModel.CategoryUiModel(
                    text = RichTextType(
                        text = "질문",
                        color = ColorType.Token("Gray300"),
                        typography = "badge",
                    ),
                    target = "question",
                    icon = IconType(
                        url = "https://via.placeholder.com/16",
                        color = ColorType.Token("Gray300"),
                    ),
                ),
            ),
            infoSection = PostContentUiModel.InfoSectionUiModel(
                title = RichTextType(
                    text = "위스팟 앱 사용법이 궁금해요!",
                    color = ColorType.Token("white"),
                    typography = "body4",
                ),
                description = RichTextType(
                    text = "안녕하세요! 위스팟을 처음 사용해보는데 기본적인 사용법이 궁금합니다. 특히 커뮤니티 기능을 어떻게 활용하면 좋을까요?",
                    color = ColorType.Token("white"),
                    typography = "body6",
                ),
                seeMore = RichTextType(
                    text = "더보기",
                    color = ColorType.Token("primary300"),
                    typography = "body6",
                ),
                maxLine = 2,
            ),
            contentSection = PostContentUiModel.ContentSectionUiModel.ImagesSectionUiModel(
                images = listOf(
                    ImageType(
                        url = "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png",
                        width = 300,
                        height = 150,
                    ),
                    ImageType(
                        url = "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png",
                        width = 300,
                        height = 150,
                    ),
                ),
            ),
            footerSection = PostContentUiModel.FooterSectionUiModel(
                reactions = listOf(
                    PostContentUiModel.FooterSectionUiModel.ReactionUiModel.LikeUiModel(
                        icon = IconType(
                            url = "https://cdn-icons-png.flaticon.com/128/15407/15407695.png",
                            color = ColorType.Token("Gray300"),
                        ),
                        count = RichTextType(
                            text = "12",
                            color = ColorType.Token("gray300"),
                            typography = "badge",
                        ),
                        selected = false,
                    ),
                    PostContentUiModel.FooterSectionUiModel.ReactionUiModel.ChatUiModel(
                        icon = IconType(
                            url = "https://cdn-icons-png.flaticon.com/128/646/646094.png",
                            color = ColorType.Token("Gray300"),
                        ),
                        count = RichTextType(
                            text = "5",
                            color = ColorType.Token("gray300"),
                            typography = "badge",
                        ),
                        selected = false,
                    ),
                ),
                scrap = PostContentUiModel.FooterSectionUiModel.ScrapUiModel(
                    icon = IconType(
                        url = "https://cdn-icons-png.flaticon.com/512/3031/3031121.png",
                        color = ColorType.Token("Gray300"),
                    ),
                    selected = false,
                ),
            ),
        ),
    )

    val samplePostItemEmpty = PostItemUiModel(
        id = "sample_post_2",
        content = PostContentUiModel(
            headerSection = PostContentUiModel.HeaderSectionUiModel(
                profileImage = "https://via.placeholder.com/36",
                nickname = RichTextType(
                    text = "박학생",
                    color = ColorType.Token("white"),
                    typography = "body6",
                ),
                createdAt = RichTextType(
                    text = "1일 전",
                    color = ColorType.Token("gray400"),
                    typography = "badge",
                ),
                category = PostContentUiModel.HeaderSectionUiModel.CategoryUiModel(
                    text = RichTextType(
                        text = "자유",
                        color = ColorType.Token("Gray300"),
                        typography = "badge",
                    ),
                    target = "free",
                    icon = IconType(
                        url = "https://via.placeholder.com/16",
                        color = ColorType.Token("Gray300"),
                    ),
                ),
            ),
            infoSection = PostContentUiModel.InfoSectionUiModel(
                title = RichTextType(
                    text = "오늘 날씨가 정말 좋네요~",
                    color = ColorType.Token("white"),
                    typography = "body4",
                ),
                description = RichTextType(
                    text = "봄이 왔나봐요! 모두들 좋은 하루 보내세요 😊",
                    color = ColorType.Token("white"),
                    typography = "body6",
                ),
                seeMore = RichTextType(
                    text = "",
                    color = ColorType.Token("primary300"),
                    typography = "body6",
                ),
                maxLine = 1,
            ),
            contentSection = PostContentUiModel.ContentSectionUiModel.EmptySectionUiModel,
            footerSection = PostContentUiModel.FooterSectionUiModel(
                reactions = listOf(
                    PostContentUiModel.FooterSectionUiModel.ReactionUiModel.LikeUiModel(
                        icon = IconType(
                            url = "https://cdn-icons-png.flaticon.com/128/15407/15407695.png",
                            color = ColorType.Token("Gray300"),
                        ),
                        count = RichTextType(
                            text = "24",
                            color = ColorType.Token("gray300"),
                            typography = "badge",
                        ),
                        selected = true,
                    ),
                    PostContentUiModel.FooterSectionUiModel.ReactionUiModel.ChatUiModel(
                        icon = IconType(
                            url = "https://cdn-icons-png.flaticon.com/128/646/646094.png",
                            color = ColorType.Token("Gray300"),
                        ),
                        count = RichTextType(
                            text = "8",
                            color = ColorType.Token("Gray300"),
                            typography = "badge",
                        ),
                        selected = false,
                    ),
                ),
                scrap = PostContentUiModel.FooterSectionUiModel.ScrapUiModel(
                    icon = IconType(
                        url = "https://cdn-icons-png.flaticon.com/512/3031/3031121.png",
                        color = ColorType.Token("Gray300"),
                    ),
                    selected = true,
                ),
            ),
        ),
    )
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_UNDEFINED)
@Composable
private fun PostItemPreview() {
    WeSpotTheme {
        Surface {
            PostItemPreviewData.samplePostItem.content.Item({})
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_UNDEFINED)
@Composable
private fun PostItemEmptyPreview() {
    WeSpotTheme {
        Surface {
            PostItemPreviewData.samplePostItemEmpty.content.Item({})
        }
    }
}
