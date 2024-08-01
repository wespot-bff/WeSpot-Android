package com.bff.wespot.vote.screen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bff.wespot.common.util.toDateString
import com.bff.wespot.designsystem.component.banner.WSBanner
import com.bff.wespot.designsystem.component.banner.WSBannerType
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.indicator.WSHomeTabRow
import com.bff.wespot.designsystem.theme.Gray100
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.model.user.response.ProfileCharacter
import com.bff.wespot.model.vote.response.Result
import com.bff.wespot.model.vote.response.VoteUser
import com.bff.wespot.ui.DotIndicators
import com.bff.wespot.ui.WSCarousel
import com.bff.wespot.util.OnLifecycleEvent
import com.bff.wespot.vote.R
import com.bff.wespot.vote.state.home.VoteAction
import com.bff.wespot.vote.state.home.VoteUiState
import com.bff.wespot.vote.ui.VoteCard
import com.bff.wespot.vote.viewmodel.VoteHomeViewModel
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState
import java.time.LocalDate

interface VoteNavigator {
    fun navigateUp()
    fun navigateToVotingScreen()
    fun navigateToVoteResultScreen(args: VoteResultScreenArgs)
    fun navigateToVoteStorageScreen()
}

@Destination
@Composable
internal fun VoteHomeScreen(
    viewModel: VoteHomeViewModel = hiltViewModel(),
    voteNavigator: VoteNavigator,
) {
    val state by viewModel.collectAsState()
    val action = viewModel::onAction

    Scaffold(
        topBar = {
            VoteTopBar(
                selectedTabIndex = state.selectedTabIndex,
                onTabSelected = { action(VoteAction.OnTabChanged(it)) },
            )
        },
    ) {
        Crossfade(targetState = state.selectedTabIndex, label = "") { index ->
            Box(modifier = Modifier.padding(it)) {
                when (index) {
                    HOME_SCREEN -> VoteHomeContent(
                        viewModel = viewModel,
                        voteNavigator = voteNavigator,
                    )

                    RESULT_SCREEN -> CardResultContent(
                        state = state,
                        action = viewModel::onAction,
                        navigator = voteNavigator,
                    )
                }
            }
        }
    }

    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                viewModel.onAction(VoteAction.StartDate)
            }

            Lifecycle.Event.ON_STOP -> {
                viewModel.onAction(VoteAction.EndDate)
            }

            else -> {}
        }
    }
}

@Composable
private fun ClockTime(viewModel: VoteHomeViewModel) {
    val currentDate by viewModel.currentDate.collectAsStateWithLifecycle()

    Text(
        text = currentDate,
        style = StaticTypeScale.Default.body6,
        color = WeSpotThemeManager.colors.disableBtnTxtColor,
        modifier = Modifier.padding(horizontal = 28.dp),
    )
}

@Composable
private fun VoteTopBar(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
) {
    val tabList = persistentListOf(
        stringResource(R.string.vote_home_screen),
        stringResource(R.string.vote_result_screen),
    )

    WSHomeTabRow(
        selectedTabIndex = selectedTabIndex,
        tabList = tabList,
        onTabSelected = onTabSelected,
    )
}

@Composable
private fun VoteHomeContent(
    viewModel: VoteHomeViewModel,
    voteNavigator: VoteNavigator,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        WSBanner(
            icon = painterResource(id = com.bff.wespot.ui.R.drawable.send),
            title = stringResource(id = R.string.invite_friend),
            subTitle = stringResource(id = R.string.invite_friend_description),
            bannerType = WSBannerType.Primary,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(390.dp)
                .padding(horizontal = 20.dp),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.vote),
                    contentDescription = stringResource(
                        id = R.string.vote_description,
                    ),
                    modifier = Modifier.size(310.dp),
                )
            }
            Card(
                modifier = Modifier
                    .clip(WeSpotThemeManager.shapes.medium)
                    .zIndex(0f),
                colors = CardDefaults.cardColors(
                    containerColor = WeSpotThemeManager.colors.cardBackgroundColor,
                    contentColor = Gray100,
                ),
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                ClockTime(viewModel = viewModel)
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(1f)
                        .padding(bottom = 24.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(id = R.string.vote_ongoing),
                        style = StaticTypeScale.Default.body1,
                        modifier = Modifier.padding(horizontal = 28.dp),
                    )

                    WSButton(
                        onClick = {
                            voteNavigator.navigateToVotingScreen()
                        },
                        text = stringResource(id = R.string.voting),
                    ) {
                        it.invoke()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CardResultContent(
    state: VoteUiState,
    action: (VoteAction) -> Unit,
    navigator: VoteNavigator,
) {
    val pagerState = rememberPagerState { state.voteResults.size }

    Image(
        painter = painterResource(id = R.drawable.vote_background),
        contentDescription = "",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds,
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp),
    ) {
        if (state.voteResults.isNotEmpty() && !state.isLoading) {
            WSCarousel(
                pagerState = pagerState,
                pageSpacing = 4.dp,
                contentPadding = PaddingValues(horizontal = 46.dp),
            ) {
                if (state.voteResults[it].results.isEmpty()) {
                    VoteCard(
                        result = Result(
                            user = VoteUser(
                                id = -1,
                                name = stringResource(R.string.analyzing),
                                introduction = stringResource(R.string.need_more_vote),
                                profile = ProfileCharacter(),
                            ),
                            voteCount = 0,
                        ),
                        pagerState = pagerState,
                        question = state.voteResults[it].voteOption.content,
                        page = it,
                        onClick = {},
                    )
                } else {
                    VoteCard(
                        result = state.voteResults[it].results.first(),
                        question = state.voteResults[it].voteOption.content,
                        pagerState = pagerState,
                        page = it,
                        onClick = {
                            navigator.navigateToVoteResultScreen(
                                VoteResultScreenArgs(false),
                            )
                        },
                    )
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Column {
            DotIndicators(pagerState = pagerState)
            Spacer(modifier = Modifier.height(10.dp))

            WSButton(onClick = {
                navigator.navigateToVoteStorageScreen()
            }, text = stringResource(R.string.check_my_vote)) {
                it.invoke()
            }
        }
    }

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

    LaunchedEffect(Unit) {
        action(VoteAction.GetFirst(LocalDate.now().toDateString()))
    }
}

private const val HOME_SCREEN = 0
private const val RESULT_SCREEN = 1
