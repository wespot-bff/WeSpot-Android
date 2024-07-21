package com.bff.wespot.vote.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.button.WSButtonType
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.util.hexToColor
import com.bff.wespot.vote.R
import com.bff.wespot.vote.state.voting.VotingAction
import com.bff.wespot.vote.viewmodel.VotingViewModel
import com.ramcosta.composedestinations.annotation.Destination
import org.orbitmvi.orbit.compose.collectAsState

interface VotingNavigator {
    fun navigateUp()
    fun navigateToVotingScreen()
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

    Scaffold(
        topBar = {
            WSTopBar(
                title = "${state.pageNumber}/${state.totalPage}",
                action = {
                    Text(text = stringResource(id = R.string.report), style = it)
                },
                canNavigateBack = true,
                navigateUp = votingNavigator::navigateUp,
            )
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it),
        ) {
            Text(
                text =
                "${state.currentVote.voteUser.name}${stringResource(id = R.string.vote_question)}",
                style = StaticTypeScale.Default.header1,
                modifier = Modifier.padding(horizontal = 20.dp)
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
                    WSButton(
                        onClick = { },
                        text = voteItem.content,
                        buttonType = WSButtonType.Tertiary,
                        paddingValues = PaddingValues(vertical = 8.dp)
                    ) { text ->
                        text.invoke()
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            action(VotingAction.StartVoting)
        }
    }
}
