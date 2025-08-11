package com.bff.wespot.community.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.bff.wespot.community.component.FilterChip
import com.bff.wespot.community.component.Item
import com.bff.wespot.community.presentation.R
import com.bff.wespot.community.state.CommunityAction
import com.bff.wespot.community.state.CommunitySideEffect
import com.bff.wespot.community.uimodel.BannerItemUiModel
import com.bff.wespot.community.uimodel.HotPostItemUiModel
import com.bff.wespot.community.uimodel.PostItemUiModel
import com.bff.wespot.community.uimodel.VoteItemUiModel
import com.bff.wespot.community.viewmodel.CommunityHomeViewModel
import com.bff.wespot.community.write.screen.CategoryBottomSheet
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.navigation.Navigator
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
internal fun CommunityHomeScreen(
    viewModel: CommunityHomeViewModel = hiltViewModel(),
    navigator: Navigator,
) {
    val uiState by viewModel.collectAsState()
    val onAction = viewModel::onAction
    val context = LocalContext.current
    val paging = uiState.posts.collectAsLazyPagingItems()
    val lazyColumnState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            FilterChip(
                filterChips = uiState.filterChips,
                selectedId = uiState.selectedChipId,
                onChipClicked = { id: String, target: String ->
                    onAction(CommunityAction.OnFilterChipClicked(id, target))
                },
                onMoreClicked = {
                    onAction(CommunityAction.OnMoreClicked)
                },
                onSameChipClicked = {
                    coroutineScope.launch {
                        lazyColumnState.animateScrollToItem(0)
                    }
                },
            )
        },
        floatingActionButton = {
            CommunityFABButton(
                onFABClicked = {
                    onAction(CommunityAction.OnWritePostClicked)
                },
            )
        },
        modifier = Modifier.padding(horizontal = 20.dp),
    ) {
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = {
                onAction(CommunityAction.OnRefresh)
            },
        ) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
            ) {
                LazyColumn(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxSize(),
                    state = lazyColumnState,
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    contentPadding = PaddingValues(bottom = 86.dp),
                ) {
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
                                                is PostItemUiModel.PostContentUiModel.FooterSectionUiModel.ReactionUiModel.LikeUiModel -> {
                                                    val isLiked =
                                                        uiState.likedPosts.contains(post.id)
                                                    val currentCount =
                                                        reaction.count.text.toIntOrNull() ?: 0
                                                    val wasLikedBefore = reaction.selected

                                                    val newCount = when {
                                                        isLiked && !wasLikedBefore -> currentCount + 1
                                                        !isLiked && wasLikedBefore -> currentCount - 1
                                                        else -> currentCount
                                                    }

                                                    reaction.copy(
                                                        selected = isLiked,
                                                        count = reaction.count.copy(text = newCount.toString()),
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
                                        val intent = navigator.navigateToPostDetailActivity(
                                            context = context,
                                            postId = post.id,
                                        )

                                        context.startActivity(intent)
                                    },
                                    reactionClick = {
                                        onAction(
                                            CommunityAction.OnReactionClick(
                                                id = post.id,
                                                reaction = it,
                                            ),
                                        )
                                    },
                                    navigateToCategory = { categoryId, categoryText ->
                                        val intent = navigator.navigateToCategoryDetail(
                                            context = context,
                                            categoryId = categoryId,
                                            categoryText = categoryText,
                                        )
                                        context.startActivity(intent)
                                    },
                                    scrapClick = {
                                        onAction(
                                            CommunityAction.OnScrapClick(post.id),
                                        )
                                    },
                                )
                            }

                            is BannerItemUiModel -> {
                                post.content.Item()
                            }

                            is HotPostItemUiModel -> {
                                post.content.Item()
                            }

                            is VoteItemUiModel -> {
                                post.content.Item()
                            }

                            else -> {
                                // TODO: 오류 컨텐츠 추가
                            }
                        }
                    }
                }

                if (paging.loadState.refresh is LoadState.Loading && paging.itemCount == 0) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = WeSpotThemeManager.colors.primaryColor,
                    )
                }
            }
        }
    }

    if (uiState.showCategoryBottomSheet) {
        CategoryBottomSheet(
            chips = uiState.categories,
            onChipClicked = {},
            closeSheet = {
                onAction(CommunityAction.CloseCategorySheet)
            },
            selectedChip = uiState.selectedChip,
        )
    }

    viewModel.collectSideEffect {
        when (it) {
            is CommunitySideEffect.NavigateToWriteActivity -> {
                context.startActivity(navigator.navigateToWriteActivity(context))
            }
        }
    }

    LaunchedEffect(Unit) {
        onAction(CommunityAction.OnCommunityEnter)
    }
}

@Composable
internal fun CommunityFABButton(
    onFABClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        shape = CircleShape,
        onClick = onFABClicked,
        containerColor = WeSpotThemeManager.colors.primaryColor,
        contentColor = WeSpotThemeManager.colors.backgroundColor,
        modifier = modifier.size(50.dp),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.post_article),
            contentDescription = "",
            modifier = Modifier.size(25.dp),
        )
    }
}
