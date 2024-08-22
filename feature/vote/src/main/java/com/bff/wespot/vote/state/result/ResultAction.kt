package com.bff.wespot.vote.state.result

sealed class ResultAction {
    data class LoadVoteResults(val date: String) : ResultAction()
    data object GetOnBoarding : ResultAction()
    data object SetVoteOnBoarding : ResultAction()
    data object GetKakaoContent : ResultAction()
}
