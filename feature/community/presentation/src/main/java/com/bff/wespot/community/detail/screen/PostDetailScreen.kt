package com.bff.wespot.community.detail.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import coil.compose.rememberAsyncImagePainter
import com.bff.wespot.community.presentation.R
import com.bff.wespot.community.uimodel.PostCommentUiModel
import com.bff.wespot.community.uimodel.PostDetailUiModel
import com.bff.wespot.community.uimodel.PostDetailUiModel.PostDetailContentUiModel
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.theme.Gray200
import com.bff.wespot.designsystem.theme.Gray300
import com.bff.wespot.designsystem.theme.Gray400
import com.bff.wespot.designsystem.theme.Gray600
import com.bff.wespot.designsystem.theme.Gray700
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PostDetailScreen(
    uiModel: PostDetailContentUiModel,
    comments: List<PostCommentUiModel>,
    onBackClick: () -> Unit = { },
    onCategoryClick: (String) -> Unit = { },
    onProfileClick: (String) -> Unit = { },
    onNotificationClick: () -> Unit = { },
    onReactionClick: (String) -> Unit = { },
    onScrapClick: () -> Unit = { },
) {
    Scaffold(
        topBar = {
            WSTopBar(
                titleContent = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        uiModel.category.text.Text(StaticTypeScale.Default.body6)

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(
                                    WeSpotThemeManager.colors.cardBackgroundColor,
                                    CircleShape,
                                ),
                        ) {
                            uiModel.category.icon.Icon()
                        }
                    }
                },
                title = "",
                canNavigateBack = true,
                navigateUp = {
                    onBackClick.invoke()
                },
                action = {
                    Icon(
                        painter = rememberAsyncImagePainter(R.drawable.horizontal_3dot),
                        contentDescription = null,
                        modifier = Modifier.padding(end = 16.dp),
                    )
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
            Column {
                HorizontalDivider(color = WeSpotThemeManager.colors.bottomSheetColor)

                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                ) {
                    uiModel.category.Item(onCategoryClick)

                    Spacer(modifier = Modifier.height(16.dp))

                    uiModel.headerSection.Item(onProfileClick, onNotificationClick)

                    Spacer(modifier = Modifier.height(16.dp))

                    uiModel.infoSection.Item()

                    uiModel.contentSection?.let {
                        Spacer(modifier = Modifier.height(24.dp))
                        it.Item()
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    uiModel.footerSection.Item(onReactionClick, onScrapClick)

                    Spacer(modifier = Modifier.height(16.dp))
                }

                HorizontalDivider(
                    thickness = 8.dp,
                    color = Gray700,
                )

                comments.Item()
            }
        }
    }
}

@Composable
private fun PostDetailContentUiModel.CategoryUiModel.Item(
    onCategoryClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier.clickableSingle { onCategoryClick(target) },
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        text.Text(StaticTypeScale.Default.badge)
        icon.Icon(modifier = Modifier.size(16.dp))
    }
}

@Composable
private fun PostDetailContentUiModel.HeaderSectionUiModel.Item(
    onProfileClick: (String) -> Unit,
    onNotificationClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(White, CircleShape)
                    .clickableSingle { onProfileClick(profileImage) },
            ) {
                AsyncImage(
                    model = profileImage,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                nickname.Text(StaticTypeScale.Default.body3)
                createdAt.Text(StaticTypeScale.Default.body8)
            }

            Row(
                modifier = Modifier.clickableSingle { onNotificationClick() },
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                button.icon.Icon(modifier = Modifier.size(16.dp))
                button.text.Text(StaticTypeScale.Default.body7)
            }
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(WeSpotThemeManager.colors.cardBackgroundColor, RoundedCornerShape(8.dp)),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                button.icon.Icon(
                    modifier = Modifier.size(15.dp),
                )

                button.text.Text(StaticTypeScale.Default.body9)
            }
        }
    }
}

@Composable
private fun PostDetailContentUiModel.InfoSectionUiModel.Item() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        title.Text(
            textStyle = StaticTypeScale.Default.header3,
            maxLines = 1,
        )

        description.Text(
            textStyle = StaticTypeScale.Default.body6,
            maxLines = if (maxLine == 0) Int.MAX_VALUE else maxLine,
        )
    }
}

@Composable
private fun PostDetailContentUiModel.ContentSectionUiModel.Item() {
    when (this) {
        is PostDetailContentUiModel.ContentSectionUiModel.ImagesContentUiModel -> {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(content) { image ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .width(image.width.dp)
                            .height(image.height.dp)
                            .background(White, RoundedCornerShape(12.dp)),
                    ) {
                        AsyncImage(
                            model = image.url,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .width(image.width.dp)
                                .height(image.height.dp),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PostDetailContentUiModel.FooterSectionUiModel.Item(
    onReactionClick: (String) -> Unit,
    onScrapClick: () -> Unit,
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            reactions.forEach { reaction ->
                Row(
                    modifier = Modifier.clickableSingle {
                        onReactionClick(
                            when (reaction) {
                                is PostDetailContentUiModel.FooterSectionUiModel.ReactionUiModel.ChatUiModel -> "chat"
                                is PostDetailContentUiModel.FooterSectionUiModel.ReactionUiModel.LikeUiModel -> "like"
                            },
                        )
                    },
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    reaction.icon.Icon(modifier = Modifier.size(18.dp))
                    reaction.count.Text(StaticTypeScale.Default.body6)
                }
            }
        }

        Row(
            modifier = Modifier.clickableSingle { onScrapClick() },
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            scrap.icon.Icon(modifier = Modifier.size(18.dp))
            scrap.count.Text(StaticTypeScale.Default.body6)
        }
    }
}

@Composable
private fun List<PostCommentUiModel>.Item() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        items(this@Item) {
            it.Item()
        }
    }
}

@Composable
private fun PostCommentUiModel.Item() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isMe) {
            Alignment.End
        } else {
            Alignment.Start
        },
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (!isMe) {
                Box(
                    modifier = Modifier
                        .background(Gray300, CircleShape),
                ) {
                    AsyncImage(
                        model = profileImage,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape),
                        contentDescription = null,
                    )
                }
            }

            Column(
                horizontalAlignment = if (isMe) {
                    Alignment.End
                } else {
                    Alignment.Start
                },
            ) {
                Text(
                    text = nickname,
                    color = Gray200,
                    style = StaticTypeScale.Default.body8,
                )

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Gray600, RoundedCornerShape(16.dp)),
                    ) {
                        Text(
                            text = message,
                            style = StaticTypeScale.Default.body6,
                            color = White,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = createdAt,
                        style = StaticTypeScale.Default.body9,
                        color = Gray400,
                    )

                    Icon(
                        painter = rememberAsyncImagePainter(R.drawable.like),
                        contentDescription = null,
                        tint = Gray400,
                        modifier = Modifier.size(12.dp),
                    )

                    Text(
                        text = likeCount.toString(),
                        style = StaticTypeScale.Default.body9,
                        color = Gray400,
                    )

                    Text(
                        text = "•",
                        style = StaticTypeScale.Default.body9,
                        color = Gray400,
                    )

                    if (isMe) {
                        Text(
                            text = stringResource(R.string.postdetail_delete),
                            style = StaticTypeScale.Default.body9,
                            color = Gray400,
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.postdetail_report),
                            style = StaticTypeScale.Default.body9,
                            color = Gray400,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CommentsPreview() {
    WeSpotTheme {
        val sampleComment = listOf(
            PostCommentUiModel(
                isMe = true,
                nickname = "김개발자",
                profileImage = "https://via.placeholder.com/32",
                message = "정말 유용한 정보네요! React 18의 Concurrent Features에 대해 잘 이해하게 되었습니다.",
                createdAt = "2024년 1월 15일 15:00",
                likeCount = 5,
            ),
            PostCommentUiModel(
                isMe = false,
                nickname = "김개발자",
                profileImage = "https://via.placeholder.com/32",
                message = "정말 유용한 정보네요! React 18의 Concurrent Features에 대해 잘 이해하게 되었습니다.",
                createdAt = "2024년 1월 15일 15:00",
                likeCount = 5,
            ),
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(WeSpotThemeManager.colors.backgroundColor)
                .padding(16.dp),
        ) {
            sampleComment.Item()
        }
    }
}

private object PostDetailPreviewData {
    val samplePostDetail = PostDetailUiModel(
        id = "post_detail_1",
        content = PostDetailContentUiModel(
            category = PostDetailContentUiModel.CategoryUiModel(
                text = RichTextType(
                    text = "개발",
                    color = ColorType.Token("primary300"),
                    typography = "badge",
                ),
                target = "development",
                icon = IconType(
                    url = "https://cdn-icons-png.flaticon.com/128/3524/3524335.png",
                    color = ColorType.Token("primary300"),
                ),
            ),
            headerSection = PostDetailContentUiModel.HeaderSectionUiModel(
                profileImage = "https://via.placeholder.com/48",
                nickname = RichTextType(
                    text = "김개발자",
                    color = ColorType.Token("white"),
                    typography = "body3",
                ),
                createdAt = RichTextType(
                    text = "2024년 1월 15일 14:30",
                    color = ColorType.Token("gray400"),
                    typography = "body8",
                ),
                button = PostDetailContentUiModel.HeaderSectionUiModel.ButtonUiModel(
                    type = "notification",
                    icon = IconType(
                        url = "https://cdn-icons-png.flaticon.com/128/3524/3524335.png",
                        color = ColorType.Token("gray400"),
                    ),
                    text = RichTextType(
                        text = "알림받기",
                        color = ColorType.Token("gray400"),
                        typography = "body7",
                    ),
                ),
            ),
            infoSection = PostDetailContentUiModel.InfoSectionUiModel(
                title = RichTextType(
                    text = "React 18의 새로운 Concurrent Features 완벽 가이드",
                    color = ColorType.Token("white"),
                    typography = "header3",
                ),
                description = RichTextType(
                    text = "React 18에서 도입된 Concurrent Features는 사용자 경험을 크게 개선할 수 있는 강력한 기능들입니다. " +
                        "이번 포스트에서는 Suspense, useTransition, useDeferredValue 등의 새로운 기능들을 실제 예제와 함께 자세히 살펴보겠습니다. " +
                        "각 기능의 사용법부터 실무에서의 활용 방안까지 포괄적으로 다루어보겠습니다.",
                    color = ColorType.Token("white"),
                    typography = "body6",
                ),
                maxLine = 0,
            ),
            contentSection = PostDetailContentUiModel.ContentSectionUiModel.ImagesContentUiModel(
                content = listOf(
                    ImageType(
                        url = "https://via.placeholder.com/320x200",
                        width = 320,
                        height = 200,
                    ),
                    ImageType(
                        url = "https://via.placeholder.com/320x200",
                        width = 320,
                        height = 200,
                    ),
                ),
            ),
            footerSection = PostDetailContentUiModel.FooterSectionUiModel(
                reactions = listOf(
                    PostDetailContentUiModel.FooterSectionUiModel.ReactionUiModel.LikeUiModel(
                        icon = IconType(
                            url = "https://cdn-icons-png.flaticon.com/128/15407/15407695.png",
                            color = ColorType.Token("gray300"),
                        ),
                        count = RichTextType(
                            text = "24",
                            color = ColorType.Token("gray300"),
                            typography = "body6",
                        ),
                        selected = false,
                    ),
                    PostDetailContentUiModel.FooterSectionUiModel.ReactionUiModel.ChatUiModel(
                        icon = IconType(
                            url = "https://cdn-icons-png.flaticon.com/128/646/646094.png",
                            color = ColorType.Token("gray300"),
                        ),
                        count = RichTextType(
                            text = "8",
                            color = ColorType.Token("gray300"),
                            typography = "body6",
                        ),
                        selected = false,
                    ),
                ),
                scrap = PostDetailContentUiModel.FooterSectionUiModel.ScrapUiModel(
                    icon = IconType(
                        url = "https://cdn-icons-png.flaticon.com/512/3031/3031121.png",
                        color = ColorType.Token("gray300"),
                    ),
                    count = RichTextType(
                        text = "12",
                        color = ColorType.Token("gray300"),
                        typography = "body6",
                    ),
                ),
            ),
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun PostDetailScreenPreview() {
    WeSpotTheme {
        PostDetailScreen(
            uiModel = PostDetailPreviewData.samplePostDetail.content,
            comments = emptyList(),
        )
    }
}
