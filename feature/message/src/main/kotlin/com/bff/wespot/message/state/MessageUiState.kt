package com.bff.wespot.message.state

import com.bff.wespot.model.message.response.MessageStatus
import com.bff.wespot.model.user.response.Profile

data class MessageUiState(
    val messageStatus: MessageStatus = MessageStatus(),
    val profile: Profile = Profile(),
    val isLoading: Boolean = false,
    val homeTitle: String = DEFAULT_HOME_TITLE,
) {
    companion object {
        const val DEFAULT_HOME_TITLE = "%1\$s님의 소중한 마음을\n모두 전달해 드렸어요"
    }
}
