package com.bff.wespot.community.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
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
                                            val isLiked = state.likedPosts.contains(post.id)
                                            val currentCount = reaction.count.text.toIntOrNull() ?: 0
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
                                    selected = state.scrappedPosts.contains(post.id),
                                ),
                            ),
                        )

                        updatedContent.Item(
                            navigateToPost = {
                                action(SearchAction.NavigateToDetail(post.id))
                            },
                            reactionClick = {
                                action(
                                    SearchAction.OnReactionClick(
                                        id = post.id,
                                        reaction = it,
                                    ),
                                )
                            },
                            navigateToCategory = { categoryId, categoryText ->
                                action(SearchAction.NavigateToCategory(categoryId, categoryText))
                            },
                            scrapClick = {
                                action(
                                    SearchAction.OnScrapClick(post.id),
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
