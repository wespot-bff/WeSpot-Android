package com.bff.wespot.vote.state.profile

sealed class ProfileAction {
    data class UpdateIntroduction(val introduction: String) : ProfileAction()
    data object StartIntroduction : ProfileAction()
    data object EditProfile : ProfileAction()
}
