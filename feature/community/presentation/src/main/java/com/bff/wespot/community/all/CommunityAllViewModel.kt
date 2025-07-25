package com.bff.wespot.community.all

import com.bff.wespot.community.all.state.CommunityAllAction
import com.bff.wespot.community.all.state.CommunityAllSideEffect
import com.bff.wespot.community.all.state.CommunityAllUiState
import com.bff.wespot.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class CommunityAllViewModel @Inject constructor() :
    BaseViewModel(),
    ContainerHost<CommunityAllUiState, CommunityAllSideEffect> {
        override val container: Container<CommunityAllUiState, CommunityAllSideEffect> = container(
            CommunityAllUiState(),
        )

        fun onAction(action: CommunityAllAction) = intent {
        }

        private fun handleTabChanged(index: Int) = intent {
            reduce {
                state.copy(selectedTabIndex = index)
            }
        }
    }
