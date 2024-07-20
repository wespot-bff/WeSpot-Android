package com.bff.wespot

import androidx.navigation.NavController
import com.bff.wespot.message.MessageNavigator
import com.danggeun.vote.VoteNavigator
import com.ramcosta.composedestinations.spec.NavGraphSpec

class CommonNavGraphNavigator(
    private val navGraph: NavGraphSpec,
    private val navController: NavController,
) : VoteNavigator,
    MessageNavigator {
    override fun navigateUp() {
        navController.navigateUp()
    }
}