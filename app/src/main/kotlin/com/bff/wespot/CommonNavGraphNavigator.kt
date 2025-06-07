package com.bff.wespot

import androidx.navigation.NavController
import com.bff.wespot.entire.screen.EntireNavigator
import com.bff.wespot.entire.screen.destinations.AccountSettingScreenDestination
import com.bff.wespot.entire.screen.destinations.EntireScreenDestination
import com.bff.wespot.entire.screen.destinations.NotificationSettingScreenDestination
import com.bff.wespot.entire.screen.destinations.ProfileEditScreenDestination
import com.bff.wespot.entire.screen.destinations.RevokeConfirmScreenDestination
import com.bff.wespot.entire.screen.destinations.RevokeScreenDestination
import com.bff.wespot.entire.screen.destinations.SettingScreenDestination
import com.bff.wespot.entire.screen.edit.ProfileEditNavigator
import com.bff.wespot.entire.screen.setting.AccountSettingNavigator
import com.bff.wespot.entire.screen.setting.NotificationSettingNavigator
import com.bff.wespot.entire.screen.setting.RevokeConfirmNavigator
import com.bff.wespot.entire.screen.setting.RevokeNavigator
import com.bff.wespot.entire.screen.setting.SettingNavigator
import com.bff.wespot.message.screen.MessageNavigator
import com.bff.wespot.message.screen.destinations.BlockedMessageScreenDestination
import com.bff.wespot.message.screen.destinations.MessageNotificationSettingScreenDestination
import com.bff.wespot.message.screen.destinations.MessageScreenDestination
import com.bff.wespot.message.screen.destinations.MessageSendScreenDestination
import com.bff.wespot.message.screen.destinations.MessageUsageSettingScreenDestination
import com.bff.wespot.message.screen.destinations.MessageWriteScreenDestination
import com.bff.wespot.message.screen.destinations.ReceiverSelectionScreenDestination
import com.bff.wespot.message.screen.send.MessageSendNavigator
import com.bff.wespot.message.screen.send.MessageWriteNavigator
import com.bff.wespot.message.screen.send.ReceiverSelectionNavigator
import com.bff.wespot.message.screen.setting.BlockedMessageNavigator
import com.bff.wespot.message.screen.setting.MessageNotificationSettingNavigator
import com.bff.wespot.message.screen.setting.MessageSettingNavigator
import com.bff.wespot.message.screen.setting.MessageUsageSettingNavigator
import com.bff.wespot.vote.screen.IndividualVoteArgs
import com.bff.wespot.vote.screen.IndividualVoteNavigator
import com.bff.wespot.vote.screen.VoteNavigator
import com.bff.wespot.vote.screen.VoteResultNavigator
import com.bff.wespot.vote.screen.VoteResultScreenArgs
import com.bff.wespot.vote.screen.VoteStorageNavigator
import com.bff.wespot.vote.screen.VotingNavigator
import com.bff.wespot.vote.screen.destinations.IndividualVoteScreenDestination
import com.bff.wespot.vote.screen.destinations.VoteResultScreenDestination
import com.bff.wespot.vote.screen.destinations.VoteStorageScreenDestination
import com.bff.wespot.vote.screen.destinations.VotingScreenDestination
import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.NavGraphSpec

class CommonNavGraphNavigator(
    private val navGraph: NavGraphSpec,
    private val navController: NavController,
) : VoteNavigator,
    MessageNavigator,
    ReceiverSelectionNavigator,
    MessageWriteNavigator,
    MessageSendNavigator,
    EntireNavigator,
    SettingNavigator,
    NotificationSettingNavigator,
    AccountSettingNavigator,
    RevokeNavigator,
    RevokeConfirmNavigator,
    VotingNavigator,
    VoteResultNavigator,
    VoteStorageNavigator,
    IndividualVoteNavigator,
    ProfileEditNavigator,
    MessageSettingNavigator,
    BlockedMessageNavigator,
    MessageNotificationSettingNavigator,
    MessageUsageSettingNavigator {
    override fun navigateUp() {
        navController.navigateUp()
    }

    override fun navigateToVotingScreen() {
        navController.navigate(VotingScreenDestination within navGraph)
    }

    override fun navigateMessageWriteScreen() {
        navController.navigate(MessageWriteScreenDestination within navGraph)
    }

    override fun navigateReceiverSelectionScreen() {
        navController.navigate(ReceiverSelectionScreenDestination within navGraph)
    }

    override fun popUpToMessageScreen() {
        navController.popBackStack(
            route = (MessageScreenDestination() within navGraph).route,
            inclusive = false,
        )
    }

    override fun navigateMessageSendScreen() {
        navController.navigate(MessageSendScreenDestination within navGraph)
    }

    override fun navigateToVoteHome() {
        navController.popBackStack(navGraph.startRoute.route, inclusive = false, saveState = true)
    }

    override fun navigateToVoteResultScreen(args: VoteResultScreenArgs) {
        navController.navigate(VoteResultScreenDestination(args) within navGraph)
    }

    override fun navigateToSetting() {
        navController.navigate(SettingScreenDestination within navGraph)
    }

    override fun navigateToNotificationSetting() {
        navController.navigate(NotificationSettingScreenDestination within navGraph)
    }

    override fun navigateToAccountSetting() {
        navController.navigate(AccountSettingScreenDestination within navGraph)
    }

    override fun navigateToRevokeScreen() {
        navController.navigate(RevokeScreenDestination within navGraph)
    }

    override fun navigateToRevokeConfirmScreen() {
        navController.navigate(RevokeConfirmScreenDestination within navGraph)
    }

    override fun navigateToVoteStorageScreen() {
        navController.navigate(VoteStorageScreenDestination within navGraph)
    }

    override fun navigateToIndividualVote(args: IndividualVoteArgs) {
        navController.navigate(IndividualVoteScreenDestination(args) within navGraph)
    }

    override fun navigateToProfileEditScreen() {
        navController.navigate(ProfileEditScreenDestination within navGraph)
    }

    override fun navigateToEntireScreen() {
        navController.navigate(EntireScreenDestination within navGraph)
    }

    override fun navigateToBlockedMessage() {
        navController.navigate(BlockedMessageScreenDestination within navGraph)
    }

    override fun navigateToMessageNotificationSetting() {
        navController.navigate(MessageNotificationSettingScreenDestination within navGraph)
    }

    override fun navigateToMessageUsageSetting() {
        navController.navigate(MessageUsageSettingScreenDestination within navGraph)
    }
}
