package com.bff.wespot.main.state

import com.bff.wespot.main.model.VersionUpdateType

sealed interface MainSideEffect {
    data class ShowVersionUpdateDialog(val versionUpdateType: VersionUpdateType): MainSideEffect
}
