package com.bff.wespot.model.message.response

import java.time.LocalDateTime

/**
 * @property messageRoomId 이전 쪽지를 주고 받은 상대의 경우, Room Id가 함꼐 내려온다.
 */
data class SenderProfile(
    val id: Int = -1,
    val name: String = "",
    val image: String = "",
    val recentlyTalk: LocalDateTime? = null,
    val myTurnToAnswer: Boolean = false,
    val isAnonymous: Boolean = false,
    val messageRoomId: Int = -1,
) {
    fun isNeverTalkBefore() = recentlyTalk == null
}
