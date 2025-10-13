package com.bff.wespot.policy.state

sealed class AnnouncementPolicySideEffect {
    data object NavigateToAuth : AnnouncementPolicySideEffect()
}
