package com.bff.wespot.community.detail.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.bff.wespot.community.detail.state.PostDetailAction
import com.bff.wespot.community.detail.state.PostDetailUiState
import com.bff.wespot.community.presentation.R
import com.bff.wespot.community.uimodel.PostCommentUiModel
import com.bff.wespot.community.uimodel.PostDetailUiModel
import com.bff.wespot.community.uimodel.PostDetailUiModel.PostDetailContentUiModel
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.modal.WSDialog
import com.bff.wespot.designsystem.theme.Gray100
import com.bff.wespot.designsystem.theme.Gray200
import com.bff.wespot.designsystem.theme.Gray300
import com.bff.wespot.designsystem.theme.Gray400
import com.bff.wespot.designsystem.theme.Gray600
import com.bff.wespot.designsystem.theme.Gray700
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
import com.bff.wespot.server.driven.type.color
import com.bff.wespot.ui.component.WSBottomSheet
import com.bff.wespot.ui.util.clickableSingle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PostDetailScreen(
    uiState: PostDetailUiState,
    onAction: (PostDetailAction) -> Unit,
) {
    val uiModel = uiState.detail.content
    val lazyListState = rememberLazyListState()

    Scaffold(
        topBar = {
            WSTopBar(
                titleContent = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.clickableSingle {
                            onAction(
                                PostDetailAction.OnCategoryClick(
                                    uiModel.category.target,
                                    uiModel.category.text.text,
                                ),
                            )
                        },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        uiModel.category.text.Text(StaticTypeScale.Default.body6)

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(
                                    Gray400,
                                    CircleShape,
                                )
                                .size(18.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            uiModel.category.icon.Icon(modifier = Modifier.size(16.dp))
                        }
                    }
                },
                title = "",
                canNavigateBack = true,
                navigateUp = {
                    onAction(PostDetailAction.OnBackClick)
                },
                action = {
                    Icon(
                        painter = rememberAsyncImagePainter(R.drawable.horizontal_3dot),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickableSingle { onAction(PostDetailAction.OnMoreOptionClicked) }
                            .size(40.dp),
                    )
                },
            )
        },
        bottomBar = {
            CommentInputBox(
                value = uiState.commentInput,
                onValueChanged = { onAction(PostDetailAction.OnCommentChange(it)) },
                onSendClick = { onAction(PostDetailAction.OnCommentSend(uiState.commentInput)) },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            state = lazyListState,
        ) {
            item {
                Column {
                    Spacer(modifier = Modifier.height(24.dp))

                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp),
                    ) {
                        uiModel.headerSection.Item(
                            registered = uiState.registered,
                            onAction = onAction,
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        uiModel.infoSection.Item()

                        uiModel.contentSection?.let {
                            Spacer(modifier = Modifier.height(24.dp))
                            it.Item()
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        uiModel.footerSection.Item(
                            onAction,
                            uiState.isLiked,
                            uiState.likeCount,
                            uiState.commentCount,
                            uiState.isScrapped,
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    HorizontalDivider(
                        thickness = 8.dp,
                        color = Gray700,
                    )
                }
            }

            items(uiState.comments) { comment ->
                Spacer(modifier = Modifier.height(24.dp))

                comment.Item(
                    onAction = onAction,
                    isLiked = comment.pushedLike,
                )
                if (comment != uiState.comments.last()) {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }

    LaunchedEffect(uiState.scrollToComments) {
        lazyListState.animateScrollToItem(1)
    }

    if (uiState.showPostOptionsBottomSheet) {
        PostOptionsBottomSheet(
            sheetItems = uiState.getSheetList(),
            onAction = onAction,
        )
    }

    if (uiState.showDeleteDialog) {
        WSDialog(
            title = stringResource(R.string.delete_dialog_title),
            subTitle = stringResource(R.string.delete_dialog_subtitle),
            okButtonText = stringResource(R.string.write_post_warning_ok),
            cancelButtonText = stringResource(R.string.write_post_warning_no),
            okButtonClick = { onAction(PostDetailAction.OnConfirmDelete) },
            cancelButtonClick = { onAction(PostDetailAction.OnDismissDeleteDialog) },
            onDismissRequest = { onAction(PostDetailAction.OnDismissDeleteDialog) },
        )
    }

    if (uiState.showBlockDialog) {
        WSDialog(
            title = stringResource(R.string.block_dialog_title),
            subTitle = stringResource(R.string.block_dialog_subtitle),
            okButtonText = stringResource(R.string.write_post_warning_ok),
            cancelButtonText = stringResource(R.string.write_post_warning_no),
            okButtonClick = { onAction(PostDetailAction.OnConfirmBlock) },
            cancelButtonClick = { onAction(PostDetailAction.OnDismissBlockDialog) },
            onDismissRequest = { onAction(PostDetailAction.OnDismissBlockDialog) },
        )
    }
}

@Composable
private fun PostDetailContentUiModel.HeaderSectionUiModel.Item(
    registered: Boolean,
    onAction: (PostDetailAction) -> Unit,
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
                    .background(White, CircleShape),
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
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(
                    WeSpotThemeManager.colors.cardBackgroundColor,
                    RoundedCornerShape(8.dp),
                ),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .padding(start = 8.dp, end = 14.dp, top = 6.dp, bottom = 6.dp)
                    .clickableSingle {
                        onAction(PostDetailAction.OnNotificationClick)
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (button.icon.url.isNotEmpty()) {
                    AsyncImage(
                        model = button.icon.url,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(
                            if (registered) {
                                Gray300
                            } else {
                                Primary300
                            },
                        ),
                        modifier = Modifier.size(15.dp),
                    )
                }

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
        title?.Text(
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
                items(images) { image ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(White, RoundedCornerShape(12.dp)),
                    ) {
                        AsyncImage(
                            model = image,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .widthIn(min = 200.dp, max = 226.dp)
                                .heightIn(min = 226.dp, max = 266.dp),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                        )
                    }
                }
            }
        }

        is PostDetailContentUiModel.ContentSectionUiModel.SingleImageUiModel -> {
            AsyncImage(
                model = image,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .fillMaxWidth()
                    .heightIn(min = 155.dp, max = 718.dp),
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PostDetailContentUiModel.FooterSectionUiModel.Item(
    onAction: (PostDetailAction) -> Unit,
    isLiked: Boolean = false,
    likeCount: Int = 0,
    commentCount: Int = 0,
    isScrapped: Boolean = false,
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
                val isSelected = when (reaction) {
                    is PostDetailContentUiModel.FooterSectionUiModel.ReactionUiModel.LikeUiModel -> isLiked
                    is PostDetailContentUiModel.FooterSectionUiModel.ReactionUiModel.ChatUiModel -> reaction.selected
                }

                Row(
                    modifier = Modifier.clickableSingle {
                        onAction(PostDetailAction.OnReactionClick(reaction))
                    },
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AsyncImage(
                        model = reaction.icon.url,
                        modifier = Modifier.size(18.dp),
                        colorFilter = ColorFilter.tint(
                            if (isSelected) {
                                Primary300
                            } else {
                                Gray300
                            },
                        ),
                        contentDescription = null,
                    )
                    val displayCount = when (reaction) {
                        is PostDetailContentUiModel.FooterSectionUiModel.ReactionUiModel.LikeUiModel -> likeCount
                        is PostDetailContentUiModel.FooterSectionUiModel.ReactionUiModel.ChatUiModel -> commentCount
                    }
                    if (displayCount != 0) {
                        Text(
                            text = displayCount.toString(),
                            style = StaticTypeScale.Default.body6,
                            color = reaction.count.color.color(),
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.clickableSingle { onAction(PostDetailAction.OnScrapClick) },
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = scrap.icon.url,
                modifier = Modifier.size(18.dp),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    if (isScrapped) {
                        Primary300
                    } else {
                        Gray300
                    },
                ),
            )
            Text(
                text = stringResource(R.string.post_scrap),
                style = StaticTypeScale.Default.body7,
                color = Gray100,
            )
        }
    }
}

@Composable
private fun PostCommentUiModel.Item(
    onAction: (PostDetailAction) -> Unit = {},
    isLiked: Boolean = false,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalAlignment = if (isMe) {
            Alignment.End
        } else {
            Alignment.Start
        },
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.8f),
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
                    horizontalArrangement = if (isMe) {
                        Arrangement.End
                    } else {
                        Arrangement.Start
                    },
                ) {
                    val corner = RoundedCornerShape(
                        topEnd = if (isMe) 0.dp else 16.dp,
                        topStart = if (isMe) 16.dp else 0.dp,
                        bottomEnd = 16.dp,
                        bottomStart = 16.dp,
                    )
                    Box(
                        modifier = Modifier
                            .clip(corner)
                            .background(Gray600, corner),
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
                        tint = if (isLiked) Primary300 else Gray400,
                        modifier = Modifier
                            .size(12.dp)
                            .clickableSingle { onAction(PostDetailAction.OnCommentLike(id)) },
                    )

                    Text(
                        text = likeCount.toString(),
                        style = StaticTypeScale.Default.body9,
                        color = if (isLiked) Primary300 else Gray400,
                        modifier = Modifier.clickableSingle {
                            onAction(
                                PostDetailAction.OnCommentLike(
                                    id,
                                ),
                            )
                        },
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
                            modifier = Modifier.clickableSingle {
                                onAction(PostDetailAction.OnCommentDelete(id))
                            },
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.postdetail_report),
                            style = StaticTypeScale.Default.body9,
                            color = Gray400,
                            modifier = Modifier.clickableSingle {
                                onAction(
                                    PostDetailAction.OnCommentReport(
                                        id,
                                    ),
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CommentInputBox(
    value: String,
    onValueChanged: (String) -> Unit,
    onSendClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 12.dp,
                horizontal = 20.dp,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .height(IntrinsicSize.Min)
                .background(
                    color = WeSpotThemeManager.colors.cardBackgroundColor.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(20.dp),
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChanged,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                textStyle = StaticTypeScale.Default.body3.copy(
                    color = WeSpotThemeManager.colors.txtTitleColor,
                ),
                decorationBox = @Composable { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxHeight(),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = stringResource(R.string.post_detail_comment_placeholder),
                                style = StaticTypeScale.Default.body9,
                                color = Gray400,
                            )
                        }
                        innerTextField()
                    }
                },
            )

            Spacer(modifier = Modifier.width(8.dp))

            Image(
                painter = rememberAsyncImagePainter(R.drawable.send_button),
                contentDescription = "",
                modifier = Modifier
                    .size(26.dp)
                    .clickableSingle { onSendClick() },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PostOptionsBottomSheet(
    sheetItems: List<PostDetailUiState.SheetItem>,
    onAction: (PostDetailAction) -> Unit,
) {
    WSBottomSheet(closeSheet = { onAction(PostDetailAction.OnDismissPostOptions) }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 28.dp),
        ) {
            sheetItems.forEachIndexed { index, item ->
                PostOptionItem(
                    text = item.text,
                    textColor = WeSpotThemeManager.colors.txtTitleColor,
                    onClick = {
                        onAction(PostDetailAction.OnSheetItemClicked(item.type))
                    },
                )

                if (index < sheetItems.lastIndex) {
                    HorizontalDivider(
                        color = Color(0xFF4F5157),
                        modifier = Modifier.padding(vertical = 8.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun PostOptionItem(
    text: String,
    textColor: Color = WeSpotThemeManager.colors.txtTitleColor,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickableSingle { onClick() }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = StaticTypeScale.Default.body3,
            color = textColor,
        )
    }
}

private object PostDetailPreviewData {
    val samplePostDetail = PostDetailUiModel(
        id = "post_detail_1",
        isMyPost = false,
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
                images = listOf(),
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
                    selected = false,
                ),
            ),
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun PostDetailScreenPreview() {
    val sampleComment = listOf(
        PostCommentUiModel(
            id = "comment_1",
            isMe = true,
            nickname = "김개발자",
            profileImage = "https://via.placeholder.com/32",
            message = "정말 유용한 정보네요! React 18의 Concurrent Features에 대해 잘 이해하게 되었습니다.",
            createdAt = "2024년 1월 15일 15:00",
            likeCount = 5,
            pushedLike = false,
            isReported = false,
        ),
        PostCommentUiModel(
            id = "comment_2",
            isMe = false,
            nickname = "김개발자",
            profileImage = "https://via.placeholder.com/32",
            message = "정말 유용한 정보네요! React 18의 Concurrent Features에 대해 잘 이해하게 되었습니다.",
            createdAt = "2024년 1월 15일 15:00",
            likeCount = 5,
            pushedLike = true,
            isReported = true,
        ),
    )
    WeSpotTheme {
        PostDetailScreen(
            uiState = PostDetailUiState(
                comments = sampleComment,
                detail = PostDetailPreviewData.samplePostDetail,
                scrollToComments = false,
            ),
            onAction = {},
        )
    }
}
