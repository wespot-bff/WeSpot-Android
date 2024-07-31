package com.bff.wespot.vote.screen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.bff.wespot.common.util.timeDifference
import com.bff.wespot.common.util.toDateString
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.model.vote.response.ReceivedVoteResult
import com.bff.wespot.model.vote.response.SentVoteResult
import com.bff.wespot.model.vote.response.StorageVoteResult
import com.bff.wespot.ui.RedDot
import com.bff.wespot.ui.WSHomeChipGroup
import com.bff.wespot.vote.R
import com.bff.wespot.vote.state.storage.StorageAction
import com.bff.wespot.vote.state.storage.StorageSideEffect
import com.bff.wespot.vote.state.storage.StorageUiState
import com.bff.wespot.vote.ui.EmptyResultScreen
import com.bff.wespot.vote.viewmodel.VoteStorageViewModel
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

interface VoteStorageNavigator {
    fun navigateUp()
    fun navigateToIndividualVote(args: IndividualVoteArgs)
}

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun VoteStorageScreen(
    navigator: VoteStorageNavigator,
    viewModel: VoteStorageViewModel = hiltViewModel(),
) {
    val state by viewModel.collectAsState()
    val action = viewModel::onAction

    var selectedTab by remember {
        mutableStateOf(0)
    }

    viewModel.collectSideEffect {
        when (it) {
            is StorageSideEffect.NavigateToIndividualVote -> {
                navigator.navigateToIndividualVote(
                    IndividualVoteArgs(
                        it.optionId,
                        it.date,
                        it.isReceived
                    )
                )
            }
        }
    }

    Scaffold(
        topBar = {
            WSTopBar(
                title = "",
                canNavigateBack = true,
                navigateUp = {
                    navigator.navigateUp()
                },
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                WSHomeChipGroup(
                    items = persistentListOf(
                        stringResource(id = R.string.received_vote), stringResource(
                            id = R.string.sent_vote
                        )
                    ),
                    selectedItemIndex = selectedTab,
                    onSelectedChanged = { index ->
                        selectedTab = index
                    },
                )
            }

            Crossfade(
                targetState = selectedTab,
                label = "Screen",
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                when (it) {
                    0 -> {
                        ReceivedVoteScreen(state = state, action = action)
                    }

                    1 -> {
                        SentVoteScreen(state = state, action = action)
                    }
                }
            }
        }
    }

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun ReceivedVoteScreen(state: StorageUiState, action: (StorageAction) -> Unit) {
    if (state.receivedVotes.isEmpty()) {
        EmptyResultScreen()
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(state.receivedVotes, key = {
                it.date
            }) {
                VoteDateList(votes = it.receivedVoteResults, date = it.date, action = action)
            }
        }
    }

    LaunchedEffect(Unit) {
        action(StorageAction.GetReceivedVotes)
    }
}

@Composable
private fun SentVoteScreen(state: StorageUiState, action: (StorageAction) -> Unit) {
    if (state.sentVotes.isEmpty()) {
        EmptyResultScreen()
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(state.sentVotes, key = {
                it.date
            }) {
                VoteDateList(votes = it.sentVoteResults, date = it.date, action = action)
            }
        }
    }

    LaunchedEffect(Unit) {
        action(StorageAction.GetSentVotes)
    }
}

@Composable
private fun VoteDateList(
    votes: List<StorageVoteResult>,
    date: String,
    action: (StorageAction) -> Unit
) {
    val context = LocalContext.current
    val timeDiff = date.timeDifference()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            timeDiff.toDateString(date),
            style = StaticTypeScale.Default.body3,
            modifier = Modifier.padding(start = 4.dp, top = 12.dp),
        )

        votes.forEach { data ->
            when (data) {
                is SentVoteResult -> {
                    VoteItem(
                        new = false,
                        title = data.voteOption.content,
                        subTitle = context.getString(
                            R.string.storage_subtitle,
                            data.voteCount.toString(),
                        ),
                        isToday = timeDiff == 0L,
                    ) {
                        action(StorageAction.ToIndividualVote(data.voteOption.id, date, false))
                    }
                }

                is ReceivedVoteResult -> {
                    VoteItem(
                        new = data.isNew,
                        title = data.voteOption.content,
                        subTitle = context.getString(
                            R.string.storage_subtitle,
                            data.voteCount.toString(),
                        ),
                        isToday = timeDiff == 0L,
                    ) {
                        action(StorageAction.ToIndividualVote(data.voteOption.id, date, true))
                    }
                }
            }
        }
    }
}

@Composable
private fun VoteItem(
    new: Boolean,
    title: String,
    subTitle: String,
    isToday: Boolean,
    onClicked: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(WeSpotThemeManager.shapes.medium)
            .clickable {
                onClicked()
            },
        colors = CardDefaults.cardColors(
            containerColor = WeSpotThemeManager.colors.cardBackgroundColor,
            contentColor = WeSpotThemeManager.colors.txtTitleColor,
        ),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (new) {
                RedDot(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                        .zIndex(1f),
                )
            }

            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 18.dp)
                    .fillMaxWidth()
                    .zIndex(0f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    if (isToday) {
                        Image(
                            painter = painterResource(id = R.drawable.vote_badge),
                            contentDescription = stringResource(R.string.vote_badge),
                            modifier = Modifier.size(40.dp),
                        )
                    }

                    Column {
                        Text(text = title, style = StaticTypeScale.Default.body4)
                        Text(
                            text = subTitle,
                            style = StaticTypeScale.Default.body7,
                            color = WeSpotThemeManager.colors.txtSubColor,
                        )
                    }
                }

                Icon(
                    painter = painterResource(id = com.bff.wespot.designsystem.R.drawable.right_arrow),
                    contentDescription = stringResource(id = com.bff.wespot.designsystem.R.string.right_arrow),
                )
            }
        }
    }
}
