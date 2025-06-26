package com.bff.wespot.message.screen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bff.wespot.designsystem.component.indicator.WSHomeTabRow
import com.bff.wespot.message.R
import com.bff.wespot.message.common.HOME_SCREEN_INDEX
import com.bff.wespot.message.common.STORAGE_SCREEN_INDEX
import com.bff.wespot.message.screen.room.MessageRoomScreenArgs
import com.bff.wespot.message.screen.storage.MessageStorageScreen
import com.bff.wespot.message.viewmodel.MessageViewModel
import com.bff.wespot.message.viewmodel.SendViewModel
import com.bff.wespot.model.common.RestrictionArg
import com.bff.wespot.ui.model.ToastState
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.persistentListOf

interface MessageNavigator {
    fun navigateUp()
    fun navigateReceiverSelectionScreen()
    fun navigateToMessageRoomScreen(args: MessageRoomScreenArgs)
}

data class MessageArgs(
    val tab: String = "",
)

@Destination(
    deepLinks = [
        DeepLink(uriPattern = "wespot://message/main?tab={tab}"),
    ],
    navArgsDelegate = MessageArgs::class,
)
@Composable
internal fun MessageScreen(
    viewModel: MessageViewModel = hiltViewModel(),
    sendViewModel: SendViewModel,
    messageNavigator: MessageNavigator,
    showToast: (ToastState) -> Unit,
    restricted: RestrictionArg,
) {
    val selectedTabIndex = viewModel.selectedTabIndex.collectAsStateWithLifecycle().value

    val tabList = persistentListOf(
        stringResource(R.string.message_home_screen),
        stringResource(R.string.message_storage_screen),
    )

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            WSHomeTabRow(
                selectedTabIndex = selectedTabIndex,
                tabList = tabList,
                onTabSelected = { viewModel.updateTabIndex(it) },
            )

            Crossfade(
                targetState = selectedTabIndex,
                label = stringResource(R.string.message_screen_crossfade),
            ) { page ->
                when (page) {
                    HOME_SCREEN_INDEX -> {
                        MessageHomeScreen(
                            navigateToMessageStorageScreen = {
                                viewModel.updateTabIndex(STORAGE_SCREEN_INDEX)
                            },
                            navigateToReceiverSelectionScreen = {
                                messageNavigator.navigateReceiverSelectionScreen()
                            },
                            restricted = restricted,
                        )
                    }

                    STORAGE_SCREEN_INDEX -> {
                        MessageStorageScreen(
                            showToast = showToast,
                            navigateToMessageRoomScreen = {
                                messageNavigator.navigateToMessageRoomScreen(
                                    args = MessageRoomScreenArgs(it),
                                )
                            },
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        sendViewModel.clearUiState()
    }
}
