package com.bff.wespot.community.screen

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.rememberAsyncImagePainter
import com.bff.wespot.community.component.FilterChip
import com.bff.wespot.community.component.Item
import com.bff.wespot.community.presentation.R
import com.bff.wespot.community.state.CommunityAction
import com.bff.wespot.community.state.CommunitySideEffect
import com.bff.wespot.community.state.CommunityUiState
import com.bff.wespot.community.uimodel.BannerItemUiModel
import com.bff.wespot.community.uimodel.BaseCommunityContentUiModel
import com.bff.wespot.community.uimodel.HotPostItemUiModel
import com.bff.wespot.community.uimodel.PostItemUiModel
import com.bff.wespot.community.uimodel.VoteItemUiModel
import com.bff.wespot.community.viewmodel.CommunityHomeViewModel
import com.bff.wespot.designsystem.theme.Gray500
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.navigation.Navigator
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import kotlin.reflect.KFunction1

interface CommunityNavigator {
    fun navigateToVote()
}

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
internal fun CommunityHomeScreen(
    viewModel: CommunityHomeViewModel = hiltViewModel(),
    navigator: Navigator,
    communityNavigator: CommunityNavigator,
) {
    val uiState by viewModel.collectAsState()
    val onAction = viewModel::onAction
    val context = LocalContext.current
    val paging = uiState.posts.collectAsLazyPagingItems()
    val lazyColumnState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val postDetailLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val shouldRefresh = result.data?.getBooleanExtra("refresh", false) ?: false
            if (shouldRefresh) {
                onAction(CommunityAction.OnRefresh)
            }
        }
    }

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
                when (paging.loadState.refresh) {
                    is LoadState.Loading -> {
                        if (paging.itemCount == 0) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center),
                                color = WeSpotThemeManager.colors.primaryColor,
                            )
                        }
                    }

                    is LoadState.Error -> {
                        CommunityErrorScreen(
                            onRetryClick = { onAction(CommunityAction.OnRefresh) },
                            modifier = Modifier.fillMaxSize(),
                        )
                    }

                    else -> {
                        CommunityFeed(
                            lazyColumnState = lazyColumnState,
                            paging = paging,
                            uiState = uiState,
                            navigator = navigator,
                            postDetailLauncher = postDetailLauncher,
                            onAction = onAction,
                            communityNavigator = communityNavigator,
                        )
                    }
                }
            }
        }
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
private fun CommunityFeed(
    lazyColumnState: LazyListState,
    paging: LazyPagingItems<BaseCommunityContentUiModel>,
    uiState: CommunityUiState,
    navigator: Navigator,
    postDetailLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    onAction: KFunction1<CommunityAction, Job>,
    communityNavigator: CommunityNavigator,
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .padding(top = 24.dp)
            .padding(horizontal = 20.dp)
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
                                            reaction.count.text.toIntOrNull()
                                                ?: 0
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

                            postDetailLauncher.launch(intent)
                        },
                        reactionClick = { reaction ->
                            when (reaction) {
                                is PostItemUiModel.PostContentUiModel.FooterSectionUiModel.ReactionUiModel.ChatUiModel -> {
                                    val intent =
                                        navigator.navigateToPostDetailActivity(
                                            context = context,
                                            postId = post.id,
                                            scrollToComments = true,
                                        )
                                    postDetailLauncher.launch(intent)
                                }

                                else -> {
                                    onAction(
                                        CommunityAction.OnReactionClick(
                                            id = post.id,
                                            reaction = reaction,
                                        ),
                                    )
                                }
                            }
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
                    post.content.Item(
                        navigateToPost = {
                            val intent = navigator.navigateToPostDetailActivity(
                                context = context,
                                postId = it,
                            )

                            postDetailLauncher.launch(intent)
                        },
                    )
                }

                is VoteItemUiModel -> {
                    post.content.Item(
                        onClick = {
                            communityNavigator.navigateToVote()
                        },
                    )
                }

                else -> {
                    // TODO: 오류 컨텐츠 추가
                }
            }
        }
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

@Composable
internal fun CommunityErrorScreen(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = R.drawable.connection_error),
                contentDescription = stringResource(R.string.community_error_description),
                modifier = Modifier.size(80.dp),
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.community_error_title),
                style = StaticTypeScale.Default.body3,
                color = WeSpotThemeManager.colors.txtTitleColor,
            )

            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(R.string.community_error_message),
                style = StaticTypeScale.Default.body6,
                color = WeSpotThemeManager.colors.disableBtnColor,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onRetryClick,
                shape = RoundedCornerShape(10.dp),
                colors = buttonColors(
                    containerColor = Gray500,
                ),
                contentPadding = PaddingValues(vertical = 14.dp, horizontal = 38.dp),
            ) {
                Text(
                    text = stringResource(R.string.retry),
                    style = StaticTypeScale.Default.body3,
                    color = WeSpotThemeManager.colors.txtTitleColor,
                )
            }
        }
    }
}
