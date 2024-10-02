package com.bff.wespot.vote.screen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bff.wespot.common.util.toDateString
import com.bff.wespot.designsystem.component.banner.WSBanner
import com.bff.wespot.designsystem.component.banner.WSBannerType
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.indicator.WSHomeTabRow
import com.bff.wespot.designsystem.component.modal.WSDialog
import com.bff.wespot.designsystem.theme.Gray100
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.model.common.KakaoContent
import com.bff.wespot.model.common.RestrictionArg
import com.bff.wespot.model.user.response.ProfileCharacter
import com.bff.wespot.model.vote.response.Result
import com.bff.wespot.model.vote.response.VoteUser
import com.bff.wespot.navigation.Navigator
import com.bff.wespot.ui.component.DotIndicators
import com.bff.wespot.ui.component.LoadingAnimation
import com.bff.wespot.ui.component.NetworkDialog
import com.bff.wespot.ui.component.WSCarousel
import com.bff.wespot.ui.network.NetworkState
import com.bff.wespot.ui.util.OnLifecycleEvent
import com.bff.wespot.ui.util.handleSideEffect
import com.bff.wespot.vote.R
import com.bff.wespot.vote.state.home.VoteAction
import com.bff.wespot.vote.state.home.VoteUiState
import com.bff.wespot.vote.ui.VoteCard
import com.bff.wespot.vote.viewmodel.VoteHomeViewModel
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.compose.collectAsState
import java.time.LocalDate

interface VoteNavigator {
    fun navigateUp()
    fun navigateToVotingScreen()
    fun navigateToVoteResultScreen(args: VoteResultScreenArgs)
    fun navigateToVoteStorageScreen()
    fun navigateToCharacterScreen()
}

@Destination
@Composable
internal fun VoteHomeScreen(
    viewModel: VoteHomeViewModel = hiltViewModel(),
    voteNavigator: VoteNavigator,
    navigator: Navigator,
    restricted: RestrictionArg,
) {
    val state by viewModel.collectAsState()
    val networkState by viewModel.networkState.collectAsStateWithLifecycle()
    val action = viewModel::onAction

    handleSideEffect(viewModel.sideEffect)

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
                        navigator = navigator,
                        restricted = restricted,
                    )

                    RESULT_SCREEN -> CardResultContent(
                        state = state,
                        action = viewModel::onAction,
                        voteNavigator = voteNavigator,
                        navigator = navigator,
                        networkState = networkState,
                    )
                }
            }
        }
    }

    if (state.showSettingDialog) {
        WSDialog(
            title = stringResource(R.string.show_profile_setting),
            subTitle = stringResource(R.string.write_introduction),
            okButtonText = stringResource(R.string.sure),
            cancelButtonText = stringResource(R.string.next_time),
            okButtonClick = {
                voteNavigator.navigateToCharacterScreen()
                action(VoteAction.ChangeSettingDialog(false))
            },
            cancelButtonClick = {
                action(VoteAction.ChangeSettingDialog(false))
            },
            onDismissRequest = {},
        )
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

    LaunchedEffect(Unit) {
        delay(EDIT_POPUP_TIME)
        action(VoteAction.GetSettingDialogOption)
        if (state.kakaoContent == KakaoContent.EMPTY) {
            action(VoteAction.GetKakaoContent)
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
    navigator: Navigator,
    restricted: RestrictionArg,
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current

    val message = context.getString(com.bff.wespot.designsystem.R.string.invite_message)

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        val context = LocalContext.current

        WSBanner(
            icon = painterResource(id = com.bff.wespot.ui.R.drawable.send),
            title = stringResource(id = R.string.invite_friend),
            subTitle = stringResource(id = R.string.invite_friend_description),
            bannerType = WSBannerType.Primary,
            onBannerClick = {
                navigator.navigateToSharing(context, message + state.playStoreLink)
            },
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(horizontal = 20.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f),
                contentAlignment = Alignment.Center,
            ) {
                val composition by rememberLottieComposition(
                    spec = LottieCompositionSpec.RawRes(
                        R.raw.vote_home,
                    ),
                )

                Image(
                    painter = painterResource(id = R.drawable.vote_gradient),
                    contentDescription = stringResource(
                        id = R.string.vote_description,
                    ),
                    modifier = Modifier
                        .matchParentSize()
                        .aspectRatio(1f)
                        .zIndex(2f),
                )

                LottieAnimation(
                    composition = composition,
                    iterations = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
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
                        enabled = !restricted.restricted,
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
    voteNavigator: VoteNavigator,
    navigator: Navigator,
    networkState: NetworkState,
) {
    val pagerState = rememberPagerState { state.voteResults.size }
    val context = LocalContext.current

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
                        navigateToShare = {
                            if (state.kakaoContent != KakaoContent.EMPTY) {
                                navigator.navigateToKakao(
                                    context = context,
                                    title = state.kakaoContent.title,
                                    description = state.kakaoContent.description,
                                    imageUrl = state.kakaoContent.imageUrl,
                                    buttonText = state.kakaoContent.buttonText,
                                    url = state.kakaoContent.url,
                                )
                            }
                        },
                    )
                } else {
                    VoteCard(
                        result = state.voteResults[it].results.first(),
                        question = state.voteResults[it].voteOption.content,
                        pagerState = pagerState,
                        page = it,
                        onClick = {
                            voteNavigator.navigateToVoteResultScreen(
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
                voteNavigator.navigateToVoteStorageScreen()
            }, text = stringResource(R.string.check_my_vote)) {
                it.invoke()
            }
        }
    }

    if (state.isLoading) {
        LoadingAnimation()
    }

    NetworkDialog(context = context, networkState = networkState)

    LaunchedEffect(Unit) {
        action(VoteAction.GetFirst(LocalDate.now().toDateString()))
    }
}

private const val HOME_SCREEN = 0
private const val RESULT_SCREEN = 1
private const val EDIT_POPUP_TIME = 5_000L
