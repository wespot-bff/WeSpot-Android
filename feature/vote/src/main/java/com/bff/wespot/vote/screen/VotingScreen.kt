package com.bff.wespot.vote.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bff.wespot.analytic.AnalyticsEvent
import com.bff.wespot.analytic.AnalyticsEvent.Param
import com.bff.wespot.analytic.AnalyticsHelper
import com.bff.wespot.analytic.LocalAnalyticsHelper
import com.bff.wespot.analytic.TrackScreenViewEvent
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.button.WSOutlineButton
import com.bff.wespot.designsystem.component.button.WSOutlineButtonType
import com.bff.wespot.designsystem.component.button.WSTextButton
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.indicator.WSToastType
import com.bff.wespot.designsystem.component.modal.WSDialog
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.navigation.Navigator
import com.bff.wespot.ui.LoadingAnimation
import com.bff.wespot.ui.NetworkDialog
import com.bff.wespot.ui.ReportBottomSheet
import com.bff.wespot.ui.TopToast
import com.bff.wespot.util.hexToColor
import com.bff.wespot.vote.R
import com.bff.wespot.vote.state.voting.VotingAction
import com.bff.wespot.vote.state.voting.VotingSideEffect
import com.bff.wespot.vote.state.voting.VotingUiState
import com.bff.wespot.vote.viewmodel.VotingViewModel
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

interface VotingNavigator {
    fun navigateUp()
    fun navigateToVotingScreen()
    fun navigateToResultScreen(args: VoteResultScreenArgs)
}

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun VotingScreen(
    votingNavigator: VotingNavigator,
    viewModel: VotingViewModel,
    navigator: Navigator,
) {
    val state by viewModel.collectAsState()
    val action = viewModel::onAction
    val analyticsHelper = LocalAnalyticsHelper.current
    val networkState by viewModel.networkState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    analyticsHelper.logEvent(
        AnalyticsEvent(
            type = "vote_screen_view",
            extras = listOf(
                Param("screen_name", "vote_screen"),
            ),
        ),
    )

    var submitButton by remember { mutableStateOf(false) }

    var toast by remember {
        mutableStateOf(false)
    }
    var toastMessage by remember {
        mutableStateOf("")
    }

    var showReportSheet by remember {
        mutableStateOf(false)
    }

    var showReportDialog by remember {
        mutableStateOf(false)
    }

    val showGuideScreen = state.voteItems.isEmpty()

    viewModel.collectSideEffect {
        when (it) {
            VotingSideEffect.NavigateToResult -> {
                votingNavigator.navigateToResultScreen(
                    VoteResultScreenArgs(
                        isVoting = true,
                    ),
                )
            }

            is VotingSideEffect.ShowToast -> {
                toast = true
                toastMessage = it.message
            }
        }
    }

    Scaffold(
        topBar = {
            if (showGuideScreen) {
                WSTopBar(
                    title = "",
                    canNavigateBack = true,
                    navigateUp = votingNavigator::navigateUp,
                )
            } else {
                WSTopBar(
                    title = if (state.pageNumber == state.totalPage || state.loading.not()) {
                        "${state.pageNumber}/${state.totalPage}"
                    } else {
                        ""
                    },
                    action = {
                        WSTextButton(text = stringResource(id = R.string.report), onClick = {
                            showReportSheet = true
                        })
                    },
                    canNavigateBack = true,
                    navigateUp = {
                        votingNavigator.navigateUp()
                        action(VotingAction.GoBackVote)
                        submitButton = false
                    },
                )
            }
        },
    ) {
        if (state.loading && showGuideScreen) {
            return@Scaffold
        } else if (showGuideScreen) {
            VotingGuideScreen(it, navigator, state, analyticsHelper)
        } else {
            VotingProgressScreen(
                state = state,
                paddingValues = it,
                action = action,
                votingNavigator = votingNavigator,
                submitButton = submitButton,
                changeSubmitButton = { submitButton = it },
            )
        }
    }

    TopToast(
        message = toastMessage,
        toastType = WSToastType.Success,
        showToast = toast,
    ) {
        toast = false
    }

    if (state.loading) {
        LoadingAnimation()
    }

    if (showReportSheet) {
        ReportBottomSheet(
            closeSheet = { showReportSheet = false },
            options = persistentListOf(
                stringResource(R.string.not_my_classmate),
                stringResource(R.string.need_more_option),
            ),
            optionsClickable = persistentListOf(
                {
                    showReportSheet = false
                    showReportDialog = true
                },
                {
                },
            ),
        )
    }

    if (showReportDialog) {
        WSDialog(
            title = stringResource(R.string.not_your_classmate),
            subTitle = stringResource(R.string.wrong_report),
            okButtonText = stringResource(R.string.it_is_not),
            cancelButtonText = stringResource(com.bff.wespot.designsystem.R.string.close),
            okButtonClick = {
                action(VotingAction.SendReport(state.currentVote.voteUser.id))
                showReportDialog = false
            },
            cancelButtonClick = {
                navigator.navigateToWebLink(
                    context = context,
                    webLink = state.addQuestionLink,
                )
                showReportDialog = false
            },
        ) {
            showReportDialog = false
        }
    }

    NetworkDialog(context = context, networkState = networkState)

    LaunchedEffect(Unit) {
        if (state.start) {
            action(VotingAction.StartVoting)
        }
    }

    BackHandler {
        action(VotingAction.GoBackVote)
        votingNavigator.navigateUp()
        submitButton = false
    }
}

@Composable
private fun VotingProgressScreen(
    state: VotingUiState,
    paddingValues: PaddingValues,
    action: (VotingAction) -> Unit,
    votingNavigator: VotingNavigator,
    submitButton: Boolean,
    changeSubmitButton: (Boolean) -> Unit,
) {
    val heightDp = LocalConfiguration.current.screenHeightDp.dp
    var selected by remember {
        mutableStateOf(-1)
    }

    Column(
        modifier = Modifier
            .padding(paddingValues),
    ) {
        if (state.pageNumber != state.totalPage && state.loading) {
            return@Column
        }
        Text(
            text =
                "${state.currentVote.voteUser.name}${stringResource(id = R.string.vote_question)}",
            style = StaticTypeScale.Default.header1,
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp, bottom = 20.dp, start = 20.dp, end = 20.dp),
            contentAlignment = Alignment.Center,
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(state.currentVote.voteUser.profile.iconUrl)
                    .build(),
                contentDescription = stringResource(R.string.male),
                modifier = Modifier
                    .size(heightDp * 0.15f)
                    .clip(CircleShape)
                    .background(
                        hexToColor(state.currentVote.voteUser.profile.backgroundColor),
                    ),
            )
        }
        LazyColumn {
            items(state.currentVote.voteOption, key = { option ->
                option.id
            }) { voteItem ->
                WSOutlineButton(
                    onClick = {
                        selected = voteItem.id
                        action(VotingAction.GoToNextVote(voteItem.id))
                        if (state.pageNumber != state.totalPage) {
                            votingNavigator.navigateToVotingScreen()
                        } else {
                            changeSubmitButton(true)
                        }
                    },
                    buttonType =
                        if (state.selectedVote[state.pageNumber - 1].voteOptionId == voteItem.id ||
                            selected == voteItem.id
                        ) {
                            WSOutlineButtonType.Highlight
                        } else {
                            WSOutlineButtonType.None
                        },
                    paddingValues = PaddingValues(vertical = 8.dp, horizontal = 20.dp),
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = voteItem.content,
                            style = StaticTypeScale.Default.body3,
                            modifier = Modifier.padding(vertical = 18.dp, horizontal = 14.dp),
                            textAlign = TextAlign.Start,
                        )
                    }
                }
            }
        }
    }

    if (submitButton) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            WSButton(
                onClick = {
                    action(VotingAction.SubmitVoteResult)
                },
                text = stringResource(id = R.string.submit_vote_and_check_result),
                enabled = state.loading.not(),
            ) {
                it.invoke()
            }
        }
    }
}

@Composable
private fun VotingGuideScreen(
    paddingValues: PaddingValues,
    navigator: Navigator,
    state: VotingUiState,
    analyticsHelper: AnalyticsHelper,
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.guide_title),
            style = StaticTypeScale.Default.header1,
            modifier = Modifier.padding(horizontal = 24.dp),
        )
        Image(
            painter = painterResource(id = R.drawable.no_friends),
            contentDescription = stringResource(
                id = R.string.vote,
            ),
            modifier = Modifier
                .fillMaxWidth(),
        )
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        WSButton(onClick = {
            analyticsHelper.logEvent(
                AnalyticsEvent(
                    type = "empty_vote_invite_friend",
                    extras = listOf(
                        Param("screen_name", "vote_empty_screen"),
                        Param("invite_clicked", "true"),
                    ),
                ),
            )

            val message = context.getString(com.bff.wespot.designsystem.R.string.invite_message)
            navigator.navigateToSharing(context, message + state.playStoreLink)
        }, text = stringResource(R.string.invite_friend_vote)) {
            it.invoke()
        }
    }

    TrackScreenViewEvent(screenName = "vote_empty_screen")
}
