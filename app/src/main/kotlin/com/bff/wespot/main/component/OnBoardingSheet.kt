package com.bff.wespot.main.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.bff.wespot.main.model.BottomBarDestinations
import com.bff.wespot.common.currentScreenAsState
import com.bff.wespot.model.serverDriven.OnBoardingCategory
import com.bff.wespot.server.driven.onboarding.OnBoardingBottomSheet
import com.bff.wespot.main.state.MainAction
import com.bff.wespot.main.state.MainUiState
import com.bff.wespot.ui.component.WSBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun OnBoardingSheet(
    state: MainUiState,
    navController: NavController,
    action: (MainAction) -> Unit,
) {
    val current by navController.currentScreenAsState()
    val category = when {
        current == BottomBarDestinations.Vote.screen && state.showVoteOnBoarding -> {
            OnBoardingCategory.VOTE
        }
        current == BottomBarDestinations.Message.screen && state.showMessageOnBoarding -> {
            OnBoardingCategory.MESSAGE
        }
        else -> null
    }

    category?.let {
        WSBottomSheet(
            closeSheet = {},
            sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true,
                confirmValueChange = { it != SheetValue.Hidden })
        ) {
            OnBoardingBottomSheet(
                category = it,
                closeOnBoarding = {
                    action(MainAction.CloseOnBoarding(it))
                }
            )
        }
    }
}
