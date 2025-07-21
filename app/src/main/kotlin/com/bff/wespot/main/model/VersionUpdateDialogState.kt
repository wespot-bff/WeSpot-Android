package com.bff.wespot.main.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import com.bff.wespot.R

data class VersionUpdateDialogState(
    val show: Boolean = false,
    val versionUpdateType: VersionUpdateType = VersionUpdateType.USABILITY_IMPROVEMENT,
)

/**
 * @property [USABILITY_IMPROVEMENT] 사용성 개선
 * @property [NEW_FEATURE_ADDED] 새로운 기능 추가
 */
enum class VersionUpdateType {
    USABILITY_IMPROVEMENT,
    NEW_FEATURE_ADDED,
    ;

    val title: String
        @Composable
        @ReadOnlyComposable
        get() = when (this) {
            USABILITY_IMPROVEMENT -> stringResource(id = R.string.update_title_usability_improvement)
            NEW_FEATURE_ADDED -> stringResource(id = R.string.update_title_new_feature_added)
        }

    val subTitle: String
        @Composable
        @ReadOnlyComposable
        get() = when (this) {
            USABILITY_IMPROVEMENT -> stringResource(id = R.string.update_subtitle_usability_improvement)
            NEW_FEATURE_ADDED -> stringResource(id = R.string.update_subtitle_new_feature_added)
        }

    companion object {
        fun convertVersionUpdateType(versionUpdateTypeString: String) =
            when (versionUpdateTypeString) {
                USABILITY_IMPROVEMENT.name -> USABILITY_IMPROVEMENT
                NEW_FEATURE_ADDED.name -> NEW_FEATURE_ADDED
                else -> USABILITY_IMPROVEMENT
            }
    }
}
