package com.bff.wespot.model.user.response

data class NotificationSetting(
    val isEnableVoteNotification: Boolean,
    val isEnableMessageNotification: Boolean,
    val isEnableMarketingNotification: Boolean,
) {
    constructor() : this(false, false, false)
}
