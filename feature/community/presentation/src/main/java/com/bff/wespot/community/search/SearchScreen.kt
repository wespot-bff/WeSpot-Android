package com.bff.wespot.community.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.bff.wespot.community.component.Item
import com.bff.wespot.community.presentation.R
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
        ) {
            stickyHeader {
                WsTextField(
                    value = state.keyword,
                    onValueChange = {
                        action(SearchAction.HandleSearchChange(it))
                    },
                    placeholder = stringResource(R.string.search_placeholder),
                    textFieldType = WsTextFieldType.Search,
                )
            }

            items(
                key = pagingData.itemKey(),
                count = pagingData.itemCount,
            ) {
                when (val post = pagingData[it]) {
                    is PostItemUiModel -> {
                        post.content.Item(
                            navigateToPost = {
                                action(SearchAction.NavigateToDetail(post.id))
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
