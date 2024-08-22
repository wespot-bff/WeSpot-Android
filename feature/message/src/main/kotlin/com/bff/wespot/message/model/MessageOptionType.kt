package com.bff.wespot.message.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import com.bff.wespot.message.R

enum class MessageOptionType {
    DELETE,
    BLOCK,
    REPORT,
    ;

    val title
        @Composable
        @ReadOnlyComposable
        get() = when (this) {
            DELETE -> stringResource(R.string.message_delete_dialog_title)
            BLOCK -> stringResource(R.string.message_block_dialog_title)
            REPORT -> stringResource(R.string.message_report_dialog_title)
        }

    val subTitle
        @Composable
        @ReadOnlyComposable
        get() = when (this) {
            DELETE -> stringResource(R.string.message_delete_dialog_subtitle)
            BLOCK -> stringResource(R.string.message_block_dialog_subtitle)
            REPORT -> stringResource(R.string.message_report_dialog_subtitle)
        }

    val okButtonText
        @Composable
        @ReadOnlyComposable
        get() = when (this) {
            DELETE -> stringResource(R.string.message_delete_dialog_ok_button)
            BLOCK -> stringResource(R.string.message_block_dialog_ok_button)
            REPORT -> stringResource(R.string.message_report_dialog_ok_button)
        }

    val cancelButtonText
        @Composable
        @ReadOnlyComposable
        get() = when (this) {
            DELETE, BLOCK, REPORT -> stringResource(id = R.string.close)
        }
}
