package com.bff.wespot

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ramcosta.composedestinations.spec.NavGraphSpec

internal enum class BottomBarDestinations(
    val screen: NavGraphSpec,
    @DrawableRes val icon: Int,
    @DrawableRes val emptyIcon: Int,
    @StringRes val title: Int,
) {
    Vote(
        AppNavGraphs.vote,
        com.bff.wespot.designsystem.R.drawable.vote_tab,
        com.bff.wespot.designsystem.R.drawable.vote_empty,
        com.bff.wespot.vote.R.string.vote,
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
