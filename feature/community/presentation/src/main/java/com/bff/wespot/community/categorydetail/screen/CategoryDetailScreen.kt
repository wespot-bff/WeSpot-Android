package com.bff.wespot.community.categorydetail.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.view.WindowInsetsControllerCompat
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.bff.wespot.community.categorydetail.state.CategoryDetailAction
import com.bff.wespot.community.categorydetail.state.CategoryDetailUiState
import com.bff.wespot.community.component.Item
import com.bff.wespot.community.uimodel.BannerItemUiModel
import com.bff.wespot.community.uimodel.BaseCommunityContentUiModel
import com.bff.wespot.community.uimodel.HotPostItemUiModel
import com.bff.wespot.community.uimodel.PostItemUiModel
import com.bff.wespot.community.uimodel.PostItemUiModel.PostContentUiModel.FooterSectionUiModel.ReactionUiModel
import com.bff.wespot.community.uimodel.VoteItemUiModel
import com.bff.wespot.community.write.screen.CategoryBottomSheet
import com.bff.wespot.designsystem.theme.Gray600
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.ui.util.clickableSingle

@Composable
internal fun CategoryScreen(
    uiState: CategoryDetailUiState,
    paging: LazyPagingItems<BaseCommunityContentUiModel>,
    onAction: (CategoryDetailAction) -> Unit,
    navigateToPost: (String) -> Unit,
) {
    val lazyColumnState = rememberLazyListState()
    val density = LocalDensity.current
    val view = LocalView.current

    var showCategoryBottomSheet by remember { mutableStateOf(false) }

    val topBarAlpha by remember {
        derivedStateOf {
            val scrollOffset = lazyColumnState.firstVisibleItemScrollOffset
            val firstVisibleItem = lazyColumnState.firstVisibleItemIndex

            if (firstVisibleItem > 0) {
                1f
            } else {
                val imageHeightPx = with(density) { 150.dp.toPx() }
                (scrollOffset / imageHeightPx).coerceIn(0f, 1f)
            }
        }
    }

    val topbarColor = WeSpotThemeManager.colors.backgroundColor.copy(alpha = topBarAlpha)

    DisposableEffect(topBarAlpha) {
        val window = view.context.let { context ->
            (context as androidx.activity.ComponentActivity).window
        }
        val windowInsetsController = WindowInsetsControllerCompat(window, view)

        window.statusBarColor = topbarColor.toArgb()

        onDispose {
            window.statusBarColor = Color.Transparent.toArgb()
            windowInsetsController.isAppearanceLightStatusBars = false
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(WeSpotThemeManager.colors.backgroundColor),
            state = lazyColumnState,
        ) {
            item {
                Column {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.BottomStart,
                    ) {
                        AsyncImage(
                            model = "https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg",
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                        )

                        AsyncImage(
                            model = "https://picsum.photos/80/80",
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .padding(start = 20.dp)
                                .size(50.dp)
                                .offset(y = 25.dp)
                                .clip(CircleShape)
                                .zIndex(1f),
                        )
                    }

                    Row(
                        modifier = Modifier
                            .clickableSingle {
                                showCategoryBottomSheet = true
                            }
                            .padding(start = 20.dp, top = 55.dp, bottom = 32.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = uiState.currentCategory.text.ifEmpty { "카테고리 선택" },
                            style = StaticTypeScale.Default.header2,
                            color = WeSpotThemeManager.colors.txtTitleColor,
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Gray600, CircleShape)
                                .size(20.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                painter = rememberAsyncImagePainter(
                                    com.bff.wespot.designsystem.R.drawable.right_arrow,
                                ),
                                contentDescription = null,
                                modifier = Modifier.size(10.dp),
                                tint = WeSpotThemeManager.colors.txtSubColor,
                            )
                        }
                    }
                }
            }

            items(
                count = paging.itemCount,
                key = paging.itemKey(),
            ) { index ->
                val post = paging[index]

                when (post) {
                    is PostItemUiModel -> {
                        val updatedContent = post.content.copy(
                            footerSection = post.content.footerSection.copy(
                                reactions = post.content.footerSection.reactions.map { reaction ->
                                    when (reaction) {
                                        is ReactionUiModel.LikeUiModel -> {
                                            reaction.copy(
                                                selected = uiState.likedPosts.contains(post.id),
                                            )
                                        }

                                        else -> reaction
                                    }
                                },
                                scrap = post.content.footerSection.scrap.copy(
                                    selected = uiState.scrappedPosts.contains(post.id),
                                ),
                            ),
                        )

                        updatedContent.Item(
                            navigateToPost = {
                                navigateToPost(post.id)
                            },
                            reactionClick = {
                                onAction(
                                    CategoryDetailAction.OnReactionClick(
                                        id = post.id,
                                        reaction = it,
                                    ),
                                )
                            },
                            navigateToCategory = { categoryId, categoryText ->
                                // Do Nothing
                            },
                            scrapClick = {
                                onAction(CategoryDetailAction.OnScrapClick(post.id))
                            },
                            modifier = Modifier.padding(horizontal = 20.dp),
                        )
                    }

                    is BannerItemUiModel -> {
                        post.content.Item(
                            modifier = Modifier.padding(horizontal = 20.dp),
                        )
                    }

                    is HotPostItemUiModel -> {
                        post.content.Item(
                            modifier = Modifier.padding(horizontal = 20.dp),
                        )
                    }

                    is VoteItemUiModel -> {
                        post.content.Item(
                            modifier = Modifier.padding(horizontal = 20.dp),
                        )
                    }

                    else -> {
                        // TODO: Error content
                    }
                }

                if (index != paging.itemCount - 1) {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        // Top bar with dynamic opacity
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .statusBarsPadding()
                .background(
                    topbarColor,
                ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(start = 8.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                IconButton(
                    onClick = { onAction(CategoryDetailAction.OnBackClick) },
                ) {
                    Icon(
                        painter = rememberAsyncImagePainter(
                            com.bff.wespot.designsystem.R.drawable.left_arrow,
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = if (topBarAlpha < 0.5f) Color.White else WeSpotThemeManager.colors.txtTitleColor,
                    )
                }

                IconButton(
                    onClick = { /* TODO: Add search functionality */ },
                ) {
                    Icon(
                        painter = rememberAsyncImagePainter(
                            com.bff.wespot.designsystem.R.drawable.search,
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = if (topBarAlpha < 0.5f) Color.White else WeSpotThemeManager.colors.txtTitleColor,
                    )
                }
            }
        }
    }

    if (showCategoryBottomSheet) {
        CategoryBottomSheet(
            chips = uiState.categories,
            selectedChip = uiState.currentCategory,
            onChipClicked = { categoryItem ->
                onAction(CategoryDetailAction.OnCategoryChange(categoryItem.id))
                showCategoryBottomSheet = false
            },
            closeSheet = {
                showCategoryBottomSheet = false
            },
        )
    }

    LaunchedEffect(Unit) {
        onAction(CategoryDetailAction.LoadData)
    }
}
