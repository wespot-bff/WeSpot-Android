package com.bff.wespot.message.screen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.bff.wespot.designsystem.component.indicator.WSHomeTabRow
import com.bff.wespot.message.R
import com.bff.wespot.message.common.HOME_SCREEN_INDEX
import com.bff.wespot.message.common.STORAGE_SCREEN_INDEX
import com.bff.wespot.message.screen.storage.MessageStorageScreen
import com.bff.wespot.message.state.MessageAction
import com.bff.wespot.message.viewmodel.MessageViewModel
import com.bff.wespot.message.viewmodel.SendViewModel
import com.bff.wespot.model.common.RestrictionArg
import com.bff.wespot.model.notification.NotificationType
import com.bff.wespot.ui.model.ToastState
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState

interface MessageNavigator {
    fun navigateUp()
    fun navigateReceiverSelectionScreen()
}

data class MessageScreenArgs(
    val type: NotificationType = NotificationType.IDLE,
    val messageId: Int? = null,
)

@Destination(navArgsDelegate = MessageScreenArgs::class)
@Composable
internal fun MessageScreen(
    viewModel: MessageViewModel = hiltViewModel(),
    sendViewModel: SendViewModel,
    messageNavigator: MessageNavigator,
    navArgs: MessageScreenArgs,
    showToast: (ToastState) -> Unit,
    restricted: RestrictionArg,
) {
    val tabList = persistentListOf(
        stringResource(R.string.message_home_screen),
        stringResource(R.string.message_storage_screen),
    )
    val state by viewModel.collectAsState()
    val action = viewModel::onAction

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            WSHomeTabRow(
                selectedTabIndex = state.selectedTabIndex,
                tabList = tabList,
                onTabSelected = { action(MessageAction.OnTabSelected(it)) },
            )

            Crossfade(
                targetState = state.selectedTabIndex,
                label = stringResource(R.string.message_screen_crossfade),
            ) { page ->
                when (page) {
                    HOME_SCREEN_INDEX -> {
                        MessageHomeScreen(
                            viewModel = viewModel,
                            navigateToMessageStorageScreen = {
                                action(MessageAction.OnTabSelected(STORAGE_SCREEN_INDEX))
                            },
                            navigateToReceiverSelectionScreen = {
                                messageNavigator.navigateReceiverSelectionScreen()
                            },
                            restricted = restricted,
                        )
                    }

                    STORAGE_SCREEN_INDEX -> {
                        MessageStorageScreen(
                            type = navArgs.type,
                            messageId = navArgs.messageId,
                            showToast = showToast,
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        when (navArgs.type) {
            NotificationType.MESSAGE_RECEIVED, NotificationType.MESSAGE_SENT -> {
                action(MessageAction.OnTabSelected(STORAGE_SCREEN_INDEX))
            }
            else -> { }
        }
        sendViewModel.clearUiState()
    }
}
