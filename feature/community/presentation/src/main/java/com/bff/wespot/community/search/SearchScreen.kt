package com.bff.wespot.community.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.bff.wespot.community.component.Item
import com.bff.wespot.community.search.state.SearchAction
import com.bff.wespot.community.search.state.SearchUiState
import com.bff.wespot.community.uimodel.PostItemUiModel
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.input.WsTextField
import com.bff.wespot.designsystem.component.input.WsTextFieldType

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun SearchScreen(
    navigateUp: () -> Unit,
    action: (SearchAction) -> Unit,
    state: SearchUiState,
) {
    val pagingData = state.searches.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            WSTopBar(
                title = "",
                canNavigateBack = true,
                navigateUp = navigateUp,
            )
        },
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .padding(start = 20.dp, end = 20.dp, top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(bottom = 86.dp),
        ) {
            stickyHeader {
                WsTextField(
                    value = state.keyword,
                    onValueChange = {
                        action(SearchAction.HandleSearchChange(it))
                    },
                    placeholder = "글 제목 내용을 검색해 주세요",
                    textFieldType = WsTextFieldType.Search,
                )
            }

            items(
                key = pagingData.itemKey(),
                count = pagingData.itemCount,
            ) {
                when (val post = pagingData[it]) {
                    is PostItemUiModel -> {
                        val updatedContent = post.content.copy(
                            footerSection = post.content.footerSection.copy(
                                reactions = post.content.footerSection.reactions.map { reaction ->
                                    when (reaction) {
                                        is PostItemUiModel.PostContentUiModel.FooterSectionUiModel.ReactionUiModel.LikeUiModel -> {
                                            val isLikedLocally = state.likedPosts.contains(post.id)
                                            val actualSelected = if (isLikedLocally) {
                                                !reaction.selected
                                            } else {
                                                reaction.selected
                                            }

                                            val serverSelected = reaction.selected
                                            val currentCount = reaction.count.text.toIntOrNull() ?: 0
                                            val adjustedCount = when {
                                                isLikedLocally && serverSelected -> currentCount - 1
                                                isLikedLocally && !serverSelected -> currentCount + 1
                                                else -> currentCount
                                            }

                                            reaction.copy(
                                                selected = actualSelected,
                                                count = reaction.count.copy(text = adjustedCount.toString()),
                                            )
                                        }

                                        else -> reaction
                                    }
                                },
                                scrap = post.content.footerSection.scrap.copy(
                                    selected = if (state.scrappedPosts.contains(post.id)) {
                                        !post.content.footerSection.scrap.selected
                                    } else {
                                        post.content.footerSection.scrap.selected
                                    },
                                ),
                            ),
                        )

                        updatedContent.Item(
                            navigateToPost = {
                                action(SearchAction.NavigateToDetail(post.id))
                            },
                            reactionClick = { reaction ->
                                when (reaction) {
                                    is PostItemUiModel.PostContentUiModel.FooterSectionUiModel.ReactionUiModel.ChatUiModel -> {
                                        // Navigate to PostDetail comment section
                                        action(SearchAction.NavigateToDetailComments(post.id))
                                    }

                                    else -> {
                                        // Handle other reactions (like)
                                        action(
                                            SearchAction.OnReactionClick(
                                                id = post.id,
                                                reaction = reaction,
                                            ),
                                        )
                                    }
                                }
                            },
                            navigateToCategory = { categoryId, categoryText ->
                                action(SearchAction.NavigateToCategory(categoryId, categoryText))
                            },
                            scrapClick = {
                                action(
                                    SearchAction.OnScrapClick(
                                        id = post.id,
                                        isCurrentlyScrapped = updatedContent.footerSection.scrap.selected,
                                    ),
                                )
                            },
                        )
                    }

                    else -> {
                        // 지원할 필요 없음
                    }
                }
            }
        }
    }
}
