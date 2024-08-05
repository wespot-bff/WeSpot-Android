package com.bff.wespot.vote.state.home

sealed class VoteAction {
    data object StartDate : VoteAction()
    data object EndDate : VoteAction()
    data class GetFirst(val date: String) : VoteAction()
    data class OnTabChanged(val index: Int) : VoteAction()
    data object GetSettingDialogOption : VoteAction()
    data class ChangeSettingDialog(val showDialog: Boolean) : VoteAction()
}
