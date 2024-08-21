package com.bff.wespot.vote.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.bff.wespot.designsystem.component.button.WSTextButton
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.modal.WSDialog
import com.bff.wespot.vote.R
import com.bff.wespot.vote.state.profile.ProfileAction
import com.bff.wespot.vote.state.profile.ProfileSideEffect
import com.bff.wespot.vote.viewmodel.IntroductionViewModel
import com.ramcosta.composedestinations.annotation.Destination
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

interface IntroductionNavigator {
    fun navigateToVoteHome()
    fun navigateUp()
}

data class IntroductionArgs(
    val backgroundColor: String,
    val iconUrl: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Destination(
    navArgsDelegate = IntroductionArgs::class,
)
@Composable
fun IntroductionScreen(
    navigator: IntroductionNavigator,
    viewModel: IntroductionViewModel = hiltViewModel(),
) {
    val state by viewModel.collectAsState()
    val action = viewModel::onAction

    var showDialog by remember {
        mutableStateOf(false)
    }

    viewModel.collectSideEffect {
        when (it) {
            is ProfileSideEffect.NavigateToVoteHome -> navigator.navigateToVoteHome()
        }
    }

    Scaffold(
        topBar = {
            WSTopBar(
                title = "",
                canNavigateBack = true,
                navigateUp = navigator::navigateUp,
                action = {
                    WSTextButton(
                        text = stringResource(id = com.bff.wespot.designsystem.R.string.close),
                        onClick = { showDialog = true },
                    )
                },
            )
        },
    ) {
        Box(modifier = Modifier.padding(it)) {
            com.bff.wespot.ui.IntroductionScreen(
                name = stringResource(R.string.write_introduction_for_friends, state.name),
                introduction = state.introduction,
                onTextChange = {
                    action(ProfileAction.UpdateIntroduction(introduction = it))
                },
                onEditComplete = {
                    action(ProfileAction.EditProfile)
                },
                error = state.hasProfanity,
            )
        }
    }

    if (showDialog) {
        WSDialog(
            title = stringResource(R.string.quite_setting),
            subTitle = stringResource(R.string.auto_select),
            okButtonText = stringResource(R.string.yes_stop),
            cancelButtonText = stringResource(id = com.bff.wespot.designsystem.R.string.close),
            okButtonClick = navigator::navigateToVoteHome,
            cancelButtonClick = {
                showDialog = false
            },
        ) {
            showDialog = false
        }
    }

    LaunchedEffect(Unit) {
        action(ProfileAction.StartIntroduction)
    }
}
