package com.bff.wespot.community.viewmodel

import com.bff.wespot.community.state.CommunityAction
import com.bff.wespot.community.state.CommunitySideEffect
import com.bff.wespot.community.state.CommunityUiState
import com.bff.wespot.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class CommunityHomeViewModel @Inject constructor() : BaseViewModel(), ContainerHost<CommunityUiState, CommunitySideEffect> {
    override val container = container<CommunityUiState, CommunitySideEffect>(
        CommunityUiState(),
    )

    fun onAction(action: CommunityAction) {}
}
