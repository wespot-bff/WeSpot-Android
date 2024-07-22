package com.bff.wespot.vote.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.bff.wespot.vote.viewmodel.VotingViewModel
import com.ramcosta.composedestinations.annotation.Destination

data class VoteResultScreenArgs(
    val date: String,
    val isVoting: Boolean,
)

@Destination(
    navArgsDelegate = VoteResultScreenArgs::class,
)
@Composable
fun VoteResultScreen(
    viewModel: VotingViewModel = hiltViewModel(),
) {
}
