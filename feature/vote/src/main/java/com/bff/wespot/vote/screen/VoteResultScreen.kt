package com.bff.wespot.vote.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.bff.wespot.common.util.toDateString
import com.bff.wespot.vote.state.result.ResultAction
import com.bff.wespot.vote.viewmodel.VoteResultViewModel
import com.bff.wespot.vote.viewmodel.VotingViewModel
import com.ramcosta.composedestinations.annotation.Destination
import org.orbitmvi.orbit.compose.collectAsState
import java.time.LocalDate

data class VoteResultScreenArgs(
    val date: String,
    val isVoting: Boolean,
)

@Destination(
    navArgsDelegate = VoteResultScreenArgs::class,
)
@Composable
fun VoteResultScreen(
    viewModel: VoteResultViewModel = hiltViewModel(),
) {
    val state by viewModel.collectAsState()
    val action = viewModel::onAction

    Text(text = state.voteResults.toString())

    LaunchedEffect(Unit) {
        action(ResultAction.LoadVoteResults(LocalDate.now().toDateString()))
    }
}
