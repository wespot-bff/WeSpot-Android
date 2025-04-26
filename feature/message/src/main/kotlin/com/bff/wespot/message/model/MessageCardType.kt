package com.bff.wespot.message.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import com.bff.wespot.message.R

enum class MessageCardType {
    RECEIVED,
    SENT,
    ;

    val chipBackgroundColor
        get() = when (this) {
            RECEIVED -> Color(0xFFB5D1FF)
            SENT -> Color(0xFFFFCBC6)
        }

    val chipTextColor
        get() = when (this) {
            RECEIVED -> Color(0xFF3782FF)
            SENT -> Color(0xFFFF5946)
        }

    val chipText
        @Composable
        @ReadOnlyComposable
        get() = when (this) {
            RECEIVED -> stringResource(id = R.string.received_message)
            SENT -> stringResource(id = R.string.reserved_message)
        }

    val backgroundImage
        @Composable
        get() = when (this) {
            RECEIVED -> ImageBitmap.imageResource(id = R.drawable.received_message_card)
            SENT -> ImageBitmap.imageResource(id = R.drawable.sent_message_card)
        }

    val buttonText
        @Composable
        @ReadOnlyComposable
        get() = when (this) {
            RECEIVED -> stringResource(R.string.send_reply)
            SENT -> stringResource(R.string.reply_complete)
        }

    val buttonEnabled
        get() = when (this) {
            RECEIVED -> true
            SENT -> false
        }
}
