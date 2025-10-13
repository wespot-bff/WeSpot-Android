package com.bff.wespot.policy.state

sealed class AnnouncementPolicyAction {
    data object OnScreenEntered : AnnouncementPolicyAction()
    data object OnThreeDotClicked : AnnouncementPolicyAction()
    data object OnBottomSheetDismissed : AnnouncementPolicyAction()
    data object OnRevokeClicked : AnnouncementPolicyAction()
    data object OnConfirmBottomSheetDismissed : AnnouncementPolicyAction()
    data object OnRevokeConfirmed : AnnouncementPolicyAction()
    data object OnConfirmRevokeClicked : AnnouncementPolicyAction()
    data object OnDialogDismissed : AnnouncementPolicyAction()
    data object OnFinalRevokeClicked : AnnouncementPolicyAction()
}
