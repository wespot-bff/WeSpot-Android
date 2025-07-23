package com.bff.wespot.main.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.bff.wespot.navigation.AppNavGraphs
import com.ramcosta.composedestinations.spec.NavGraphSpec

internal enum class BottomBarDestinations(
    val screen: NavGraphSpec,
    @DrawableRes val icon: Int,
    @DrawableRes val emptyIcon: Int,
    @StringRes val title: Int,
) {
    Community(
        AppNavGraphs.community,
        com.bff.wespot.designsystem.R.drawable.community_tab,
        com.bff.wespot.designsystem.R.drawable.community_empty,
        com.bff.wespot.community.presentation.R.string.community,
    ),
    Vote(
        AppNavGraphs.vote,
        com.bff.wespot.designsystem.R.drawable.vote_tab,
        com.bff.wespot.designsystem.R.drawable.vote_empty,
        com.bff.wespot.vote.R.string.secret_vote,
    ),
    Message(
        AppNavGraphs.message,
        com.bff.wespot.designsystem.R.drawable.message_tab,
        com.bff.wespot.designsystem.R.drawable.message_empty,
        com.bff.wespot.message.R.string.message,
    ),
    Entire(
        AppNavGraphs.entire,
        com.bff.wespot.designsystem.R.drawable.entire_tab,
        com.bff.wespot.designsystem.R.drawable.entire_empty,
        com.bff.wespot.designsystem.R.string.entire,
    ),
}
