package com.bff.wespot

import androidx.navigation.NavController
import com.bff.wespot.message.screen.MessageNavigator
import com.danggeun.entire.screen.EntireNavigator
import com.danggeun.vote.screen.VoteNavigator
import com.danggeun.vote.screen.VotingNavigator
import com.danggeun.vote.screen.destinations.VotingScreenDestination
import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.NavGraphSpec

class CommonNavGraphNavigator(
    private val navGraph: NavGraphSpec,
    private val navController: NavController,
) : VoteNavigator,
    MessageNavigator,
    EntireNavigator,
    VotingNavigator {
    override fun navigateUp() {
        navController.navigateUp()
    }

    override fun navigateToVotingScreen() {
        navController.navigate(VotingScreenDestination within navGraph)
    }
}