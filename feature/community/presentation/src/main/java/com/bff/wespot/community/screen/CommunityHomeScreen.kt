package com.bff.wespot.community.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.navigation.Navigator
import com.ramcosta.composedestinations.annotation.Destination
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination
@Composable
internal fun CommunityHomeScreen(
    viewModel: CommunityHomeViewModel = hiltViewModel(),
    navigator: Navigator,
) {
    val uiState by viewModel.collectAsState()
    val onAction = viewModel::onAction
    val context = LocalContext.current

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
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .padding(top = 24.dp)
                .fillMaxSize(),
        ) {
            items(
                items = uiState.posts,
                key = { uiModel ->
                    uiModel.id
                },
            ) { post ->
                when (post) {
                    is PostItemUiModel -> {
                        post.content.Item()
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
    }

    viewModel.collectSideEffect {
        when (it) {
            is CommunitySideEffect.NavigateToWriteActivity -> {
                context.startActivity(navigator.navigateToWriteActivity(context))
            }
        }
    }
}

@Composable
private fun CommunityFABButton(
    onFABClicked: () -> Unit,
) {
    FloatingActionButton(
        shape = CircleShape,
        onClick = onFABClicked,
        containerColor = WeSpotThemeManager.colors.primaryColor,
        contentColor = WeSpotThemeManager.colors.backgroundColor,
        modifier = Modifier.size(50.dp),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.post_article),
            contentDescription = "",
            modifier = Modifier.size(25.dp),
        )
    }
}
