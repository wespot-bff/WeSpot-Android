package com.bff.wespot.message.screen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bff.wespot.designsystem.component.indicator.WSHomeTabRow
import com.bff.wespot.designsystem.component.indicator.WSToastType
import com.bff.wespot.message.R
import com.bff.wespot.message.common.HOME_SCREEN_INDEX
import com.bff.wespot.message.common.STORAGE_SCREEN_INDEX
import com.bff.wespot.message.screen.send.ReceiverSelectionScreenArgs
import com.bff.wespot.message.screen.storage.MessageStorageScreen
import com.bff.wespot.message.state.send.SendAction
import com.bff.wespot.message.viewmodel.SendViewModel
import com.bff.wespot.model.ToastState
import com.bff.wespot.model.common.RestrictionArg
import com.bff.wespot.model.notification.NotificationType
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.persistentListOf

interface MessageNavigator {
    fun navigateUp()
    fun navigateReceiverSelectionScreen(args: ReceiverSelectionScreenArgs)
    fun navigateToReservedMessageScreen(args: ReservedMessageScreenArgs)
}

data class MessageScreenArgs(
    val isMessageSent: Boolean = false,
    val type: NotificationType = NotificationType.IDLE,
    val messageId: Int = -1,
)

@Destination(navArgsDelegate = MessageScreenArgs::class)
@Composable
internal fun MessageScreen(
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
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            WSHomeTabRow(
                selectedTabIndex = selectedTabIndex,
                tabList = tabList,
                onTabSelected = { index -> selectedTabIndex = index },
            )

            Crossfade(
                targetState = selectedTabIndex,
                label = stringResource(R.string.message_screen_crossfade),
            ) { page ->
                when (page) {
                    HOME_SCREEN_INDEX -> {
                        MessageHomeScreen(
                            navigateToReservedMessageScreen = {
                                messageNavigator.navigateToReservedMessageScreen(
                                    args = ReservedMessageScreenArgs(false),
                                )
                            },
                            navigateToMessageStorageScreen = {
                                selectedTabIndex = STORAGE_SCREEN_INDEX
                            },
                            navigateToReceiverSelectionScreen = {
                                messageNavigator.navigateReceiverSelectionScreen(
                                    ReceiverSelectionScreenArgs(false),
                                )
                            },
                            restricted = restricted,
                        )
                    }

                    STORAGE_SCREEN_INDEX -> {
                        MessageStorageScreen(
                            type = navArgs.type,
                            messageId = navArgs.messageId,
                            navigateToReservedMessageScreen = {
                                messageNavigator.navigateToReservedMessageScreen(
                                    args = ReservedMessageScreenArgs(false),
                                )
                            },
                            showToast = showToast,
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        if (navArgs.isMessageSent) {
            showToast(
                ToastState(
                    message = R.string.message_reserve_success,
                    show = true,
                    type = WSToastType.Success,
                ),
            )
        }

        when (navArgs.type) {
            NotificationType.MESSAGE_RECEIVED, NotificationType.MESSAGE_SENT -> {
                selectedTabIndex = STORAGE_SCREEN_INDEX
            }
            else -> { }
        }

        sendViewModel.onAction(SendAction.OnMessageScreenEntered)
    }
}
