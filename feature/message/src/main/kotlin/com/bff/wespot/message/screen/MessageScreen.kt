package com.bff.wespot.message.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bff.wespot.designsystem.component.indicator.WSHomeTabRow
import com.bff.wespot.designsystem.component.indicator.WSToast
import com.bff.wespot.designsystem.component.indicator.WSToastType
import com.bff.wespot.message.R
import com.bff.wespot.message.common.HOME_SCREEN_INDEX
import com.bff.wespot.message.common.STORAGE_SCREEN_INDEX
import com.bff.wespot.message.screen.send.ReceiverSelectionScreenArgs
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.persistentListOf

interface MessageNavigator {
    fun navigateUp()
    fun navigateStorageScreen()
    fun navigateNotificationScreen()
    fun navigateReceiverSelectionScreen(args: ReceiverSelectionScreenArgs)
}

data class MessageScreenArgs(
    val isMessageSent: Boolean = false,
)

@Destination(navArgsDelegate = MessageScreenArgs::class)
@Composable
internal fun MessageScreen(
    messageNavigator: MessageNavigator,
    navArgs: MessageScreenArgs,
) {
    var messageSentToast by remember { mutableStateOf(false) }
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

            when (selectedTabIndex) {
                HOME_SCREEN_INDEX -> {
                    MessageHomeScreen(
                        navigateToMessageStorageScreen = {
                            messageNavigator.navigateStorageScreen()
                        },
                        navigateToNotificationScreen = {
                            messageNavigator.navigateNotificationScreen()
                        },
                        navigateToReceiverSelectionScreen = {
                            messageNavigator.navigateReceiverSelectionScreen(
                                ReceiverSelectionScreenArgs(false),
                            )
                        },
                    )
                }

                STORAGE_SCREEN_INDEX -> {
                    MessageStorageScreen(
                        navigateToReceiverSelectionScreen = {
                            messageNavigator.navigateReceiverSelectionScreen(
                                ReceiverSelectionScreenArgs(false),
                            )
                        },
                    )
                }
            }
        }
    }

    if (messageSentToast) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            WSToast(
                text = stringResource(R.string.message_reserve_success),
                toastType = WSToastType.Success,
                showToast = messageSentToast,
                closeToast = { messageSentToast = false },
            )
        }
    }

    LaunchedEffect(Unit) {
        messageSentToast = navArgs.isMessageSent
    }
}
