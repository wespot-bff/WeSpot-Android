package com.bff.wespot.state

import com.bff.wespot.model.VersionUpdateType

sealed interface MainSideEffect {
    data class ShowVersionUpdateDialog(val versionUpdateType: VersionUpdateType): MainSideEffect
}
