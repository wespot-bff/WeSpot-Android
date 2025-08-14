package com.bff.wespot.community.component

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bff.wespot.community.presentation.R
import com.bff.wespot.community.uimodel.PostItemUiModel
import com.bff.wespot.community.uimodel.PostItemUiModel.PostContentUiModel
import com.bff.wespot.designsystem.theme.Gray300
import com.bff.wespot.designsystem.theme.Primary300
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.designsystem.theme.White
import com.bff.wespot.model.serverDriven.type.ColorType
import com.bff.wespot.model.serverDriven.type.IconType
import com.bff.wespot.model.serverDriven.type.RichTextType
import com.bff.wespot.server.driven.type.Icon
import com.bff.wespot.server.driven.type.Text
import com.bff.wespot.server.driven.type.toTextStyle
import com.bff.wespot.ui.util.clickableSingle

@Composable
internal fun PostContentUiModel.Item(
    navigateToPost: (navigateToComment: Boolean) -> Unit,
    reactionClick: (PostContentUiModel.FooterSectionUiModel.ReactionUiModel) -> Unit,
    navigateToCategory: (category: String, categoryText: String) -> Unit,
    scrapClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.clickableSingle {
            navigateToPost.invoke(false)
        },
    ) {
        headerSection.Item(navigateToCategory = navigateToCategory)

        Spacer(modifier = Modifier.height(12.dp))

        infoSection.Item()

        Spacer(modifier = Modifier.height(16.dp))

        var hasContent by remember {
            mutableStateOf(contentSection != PostContentUiModel.ContentSectionUiModel.EmptySectionUiModel)
        }

        contentSection.Item(
            onContentVisibilityChanged = { isVisible ->
                hasContent = isVisible
            },
        )

        if (hasContent) {
            Spacer(modifier = Modifier.height(16.dp))
        }

        footerSection.Item(
            scrapClick = scrapClick,
            reactionClick = reactionClick,
        )

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider(color = WeSpotThemeManager.colors.bottomSheetColor)
    }
}

@Composable
private fun PostContentUiModel.HeaderSectionUiModel.Item(
    navigateToCategory: (target: String, categoryText: String) -> Unit,
) {
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
                category?.let { category ->
                    Row(
                        modifier = Modifier.clickableSingle {
                            navigateToCategory.invoke(category.target, category.text.text)
                        },
                    ) {
                        category.text.Text(StaticTypeScale.Default.badge)

                        category.icon.Icon(
                            modifier = Modifier.size(16.dp),
                        )
                    }
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
    var isExpanded by remember {
        mutableStateOf(false)
    }

    val textMeasurer = rememberTextMeasurer()
    val lineCount = textMeasurer.measure(
        text = description.text,
        style = description.typography.toTextStyle() ?: StaticTypeScale.Default.body6,
    ).lineCount

    Column(
        modifier = Modifier.animateContentSize(),
    ) {
        title?.Text(
            textStyle = StaticTypeScale.Default.body4,
            maxLines = 1,
        )

        Spacer(modifier = Modifier.height(4.dp))

        description.Text(
            textStyle = StaticTypeScale.Default.body6,
            maxLines = if (isExpanded) Int.MAX_VALUE else 5,
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (lineCount > 5 && !isExpanded) {
            seeMore.Text(
                textStyle = StaticTypeScale.Default.body6,
                maxLines = 1,
                modifier = Modifier.clickableSingle {
                    isExpanded = !isExpanded
                },
            )
        }
    }
}

@Composable
private fun PostContentUiModel.ContentSectionUiModel.Item(
    onContentVisibilityChanged: (Boolean) -> Unit = {},
) {
    when (this) {
        is PostContentUiModel.ContentSectionUiModel.ImagesSectionUiModel -> {
            val validImages = remember(images) { mutableStateOf(images.toMutableList()) }

            if (validImages.value.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(validImages.value) { imageUrl ->
                        var isImageVisible by remember { mutableStateOf(true) }

                        if (isImageVisible) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(White, RoundedCornerShape(8.dp)),
                            ) {
                                AsyncImage(
                                    model = imageUrl,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .widthIn(min = 200.dp, max = 226.dp)
                                        .heightIn(min = 226.dp, max = 266.dp),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    onError = {
                                        isImageVisible = false
                                        validImages.value.remove(imageUrl)
                                        if (validImages.value.isEmpty()) {
                                            onContentVisibilityChanged(false)
                                        }
                                    },
                                )
                            }
                        }
                    }
                }
            } else {
                onContentVisibilityChanged(false)
            }
        }

        is PostContentUiModel.ContentSectionUiModel.SingleImageUiModel -> {
            var isImageVisible by remember { mutableStateOf(true) }

            if (isImageVisible) {
                AsyncImage(
                    model = image,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .fillMaxWidth()
                        .heightIn(min = 155.dp, max = 718.dp),
                    contentScale = ContentScale.Crop,
                    onError = {
                        isImageVisible = false
                        onContentVisibilityChanged(false)
                    },
                )
            } else {
                onContentVisibilityChanged(false)
            }
        }

        is PostContentUiModel.ContentSectionUiModel.EmptySectionUiModel -> {
            onContentVisibilityChanged(false)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PostContentUiModel.FooterSectionUiModel.Item(
    reactionClick: (PostContentUiModel.FooterSectionUiModel.ReactionUiModel) -> Unit,
    scrapClick: () -> Unit,
) {
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
                    modifier = Modifier.clickableSingle {
                        reactionClick.invoke(it)
                    },
                ) {
                    AsyncImage(
                        model = it.icon.url,
                        modifier = Modifier.size(14.dp),
                        colorFilter = ColorFilter.tint(
                            color = if (it.selected) {
                                Primary300
                            } else {
                                Gray300
                            },
                        ),
                        contentDescription = null,
                    )

                    if (it.count.text.toIntOrNull() != 0) {
                        it.count.Text(StaticTypeScale.Default.badge)
                    }
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickableSingle {
                scrapClick.invoke()
            },
        ) {
            AsyncImage(
                model = scrap.icon.url,
                modifier = Modifier.size(14.dp),
                colorFilter = ColorFilter.tint(
                    color = if (scrap.selected) {
                        Primary300
                    } else {
                        Gray300
                    },
                ),
                contentDescription = null,
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
                    "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png",
                    "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png",
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
            PostItemPreviewData.samplePostItem.content.Item(
                {},
                {},
                { _, _ -> },
                {},
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_UNDEFINED)
@Composable
private fun PostItemEmptyPreview() {
    WeSpotTheme {
        Surface {
            PostItemPreviewData.samplePostItemEmpty.content.Item(
                {},
                {},
                { _, _ -> },
                {},
            )
        }
    }
}
