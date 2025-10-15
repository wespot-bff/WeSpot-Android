package com.bff.wespot.community.all.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.bff.wespot.community.all.CommunityAllViewModel
import com.bff.wespot.community.all.state.CommunityAllAction
import com.bff.wespot.community.all.state.MenuType
import com.bff.wespot.community.component.Item
import com.bff.wespot.community.uimodel.PostItemUiModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun AllPostsScreen(
    menuType: MenuType,
    viewModel: CommunityAllViewModel,
) {
    val uiState by viewModel.collectAsState()
    val pagingItems = uiState.paging.collectAsLazyPagingItems()
    val action = viewModel::onAction

    LaunchedEffect(menuType) {
        viewModel.onAction(CommunityAllAction.LoadPostsByMenuType(menuType.name.lowercase()))
    }
    LazyColumn(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 12.dp),
    ) {
        items(
            count = pagingItems.itemCount,
            key = pagingItems.itemKey(),
        ) { index ->
            pagingItems[index]?.let { item ->
                when (item) {
                    is PostItemUiModel -> {
                        val updatedContent = item.content.copy(
                            footerSection = item.content.footerSection.copy(
                                reactions = item.content.footerSection.reactions.map { reaction ->
                                    when (reaction) {
                                        is PostItemUiModel.PostContentUiModel.FooterSectionUiModel.ReactionUiModel.LikeUiModel -> {
                                            reaction.copy(selected = uiState.likedPosts.contains(item.id))
                                        }
                                        else -> reaction
                                    }
                                },
                                scrap = item.content.footerSection.scrap.copy(
                                    selected = uiState.scrappedPosts.contains(item.id),
                                ),
                            ),
                        )

                        updatedContent.Item(
                            navigateToPost = {
                                action(CommunityAllAction.NavigateToDetail(item.id))
                            },
                            reactionClick = {
                                action(
                                    CommunityAllAction.OnReactionClick(
                                        id = item.id,
                                        reaction = it,
                                    ),
                                )
                            },
                            scrapClick = {
                                action(
                                    CommunityAllAction.OnScrapClick(item.id),
                                )
                            },
                        )
                    }

                    else -> {
                        // Handle other content types if needed
                    }
                }
            }
        }
    }
}
