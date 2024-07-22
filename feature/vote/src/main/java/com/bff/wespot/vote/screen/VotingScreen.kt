package com.bff.wespot.vote.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bff.wespot.common.util.toDateString
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.button.WSOutlineButton
import com.bff.wespot.designsystem.component.button.WSOutlineButtonType
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.indicator.WSToast
import com.bff.wespot.designsystem.component.indicator.WSToastType
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.util.hexToColor
import com.bff.wespot.vote.R
import com.bff.wespot.vote.state.voting.VotingAction
import com.bff.wespot.vote.state.voting.VotingSideEffect
import com.bff.wespot.vote.viewmodel.VotingViewModel
import com.ramcosta.composedestinations.annotation.Destination
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.time.LocalDate

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
) {
    val state by viewModel.collectAsState()
    val action = viewModel::onAction

    var submitButton by remember { mutableStateOf(false) }
    var selected by remember {
        mutableStateOf(-1)
    }
    var toast by remember {
        mutableStateOf(false)
    }
    var toastMessage by remember {
        mutableStateOf("")
    }

    viewModel.collectSideEffect {
        when (it) {
            VotingSideEffect.NavigateToResult -> {
                votingNavigator.navigateToResultScreen(
                    VoteResultScreenArgs(
                        date = LocalDate.now().toDateString(),
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
            WSTopBar(
                title = if (state.pageNumber == state.totalPage || state.loading.not()) {
                    "${state.pageNumber}/${state.totalPage}"
                } else {
                    ""
                },
                action = {
                    Text(text = stringResource(id = R.string.report), style = it)
                },
                canNavigateBack = true,
                navigateUp = {
                    votingNavigator.navigateUp()
                    action(VotingAction.GoBackVote)
                    submitButton = false
                },
            )
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it),
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
                    .padding(top = 40.dp, bottom = 24.dp, start = 20.dp, end = 20.dp),
                contentAlignment = Alignment.Center,
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(state.currentVote.voteUser.profile.iconUrl)
                        .build(),
                    contentDescription = "male",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(hexToColor(state.currentVote.voteUser.profile.backgroundColor)),
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
                                submitButton = true
                            }
                        },
                        text = voteItem.content,
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
                        it.invoke()
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        WSToast(text = toastMessage, toastType = WSToastType.Success, showToast = toast) {
            toast = false
        }
    }

    if (state.loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

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
