package com.bff.wespot.community.detail.state

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bff.wespot.community.presentation.R
import com.bff.wespot.community.uimodel.PostCommentUiModel
import com.bff.wespot.community.uimodel.PostDetailUiModel

data class PostDetailUiState(
    val detail: PostDetailUiModel = PostDetailUiModel.Empty,
    val comments: List<PostCommentUiModel> = emptyList(),
    val isLiked: Boolean = false,
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val isScrapped: Boolean = false,
    val registered: Boolean = false,
    val commentInput: String = "",
    val scrollToComments: Boolean,
    val showPostOptionsBottomSheet: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val showBlockDialog: Boolean = false,
) {
    @Composable
    fun getSheetList(): List<SheetItem> = if (detail.isMyPost) {
        listOf(
            SheetItem(
                text = stringResource(R.string.bottomsheet_edit),
                type = SheetItem.SheetType.EDIT,
            ),
            SheetItem(
                text = stringResource(R.string.bottomsheet_delete),
                type = SheetItem.SheetType.DELETE,
            ),
        )
    } else {
        listOf(
            SheetItem(
                text = stringResource(R.string.bottomsheet_report),
                type = SheetItem.SheetType.REPORT,
            ),
            SheetItem(
                text = stringResource(R.string.bottomsheet_block),
                type = SheetItem.SheetType.BLOCK,
            ),
        )
    }

    data class SheetItem(
        val text: String,
        val type: SheetType,
    ) {
        enum class SheetType {
            EDIT,
            DELETE,
            REPORT,
            BLOCK,
        }
    }
}
