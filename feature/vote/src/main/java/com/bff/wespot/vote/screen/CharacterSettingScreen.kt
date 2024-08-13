package com.bff.wespot.vote.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bff.wespot.designsystem.component.button.WSTextButton
import com.bff.wespot.designsystem.component.modal.WSDialog
import com.bff.wespot.ui.CharacterScreen
import com.bff.wespot.ui.LoadingAnimation
import com.bff.wespot.vote.R
import com.bff.wespot.vote.viewmodel.CharacterSettingViewModel
import com.ramcosta.composedestinations.annotation.Destination

interface CharacterSettingNavigator {
    fun navigateUp()
    fun navigateToIntroduction(args: IntroductionArgs)
}

@Destination
@Composable
fun CharacterSettingScreen(
    navigator: CharacterSettingNavigator,
    viewModel: CharacterSettingViewModel = hiltViewModel(),
) {
    val color by viewModel.backgroundColor.collectAsStateWithLifecycle()
    val character by viewModel.characters.collectAsStateWithLifecycle()

    var showDialog by remember {
        mutableStateOf(false)
    }

    if (color.isEmpty() || character.isEmpty()) {
        LoadingAnimation()
        return
    }
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                WSTextButton(
                    text = stringResource(id = com.bff.wespot.designsystem.R.string.close),
                    onClick = {
                        showDialog = true
                    },
                )
            }
        },
    ) {
        Box(modifier = Modifier.padding(it)) {
            CharacterScreen(
                name = "",
                characterList = character,
                colorList = color,
            ) { iconUrl, color ->
                navigator.navigateToIntroduction(
                    IntroductionArgs(
                        backgroundColor = color,
                        iconUrl = iconUrl,
                    ),
                )
            }
        }
    }

    if (showDialog) {
        WSDialog(
            title = stringResource(R.string.quite_setting),
            subTitle = stringResource(R.string.auto_select),
            okButtonText = stringResource(R.string.yes_stop),
            cancelButtonText = stringResource(id = com.bff.wespot.designsystem.R.string.close),
            okButtonClick = navigator::navigateUp,
            cancelButtonClick = {
                showDialog = false
            },
        ) {
            showDialog = false
        }
    }
}
