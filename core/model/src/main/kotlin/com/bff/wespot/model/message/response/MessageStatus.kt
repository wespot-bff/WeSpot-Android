package com.bff.wespot.model.message.response

/**
 * @property [isSendAllowed] : 현재 쪽지를 보낼 수 있는 상태
 * @property [countRemainingMessages] : 남은 쪽지를 보낼 수 있는 갯수
 * @property [countUnReadMessages] : 어제와 오늘 간 아직 읽지 쪽지 갯수
 * @property [countUnReplayMessages] : 오늘 답장하지 않은 쪽지의 갯수
 */
data class MessageStatus(
    val isSendAllowed: Boolean = false,
    val isReceivedAllowed: Boolean = false,
    val countRemainingMessages: Int = -1,
    val countUnReadMessages: Int = -1,
    val countUnReplayMessages: Int = -1,
) {
    /**
     * 배너 노출 정책
     * [노출] : 오늘과 어제 읽지 않은 쪽지가 있거나 or 오늘 받은 쪽지 중 답장을 하지 않은 쪽지가 있음 and 쪽지 작성 갯수 남음
     * [노출] : 오늘 받은 쪽지 중 답장하지 않은 쪽지가 있음.
     * [미노출] : 오늘 받은 쪽지에 모두 답장을 했지만, 오늘 이전에 받은 쪽지 중 답장하지 않은 쪽지가 있음.
     */
    fun shouldShowReplyBanner(): Boolean {
        /** 쪽지를 보낼 수 없는 상태일 때 false */
        if (countRemainingMessages <= 0) {
            return false
        }

        if (countUnReplayMessages > 0 || countUnReadMessages > 0) {
            return true
        }

        return false
    }
}
