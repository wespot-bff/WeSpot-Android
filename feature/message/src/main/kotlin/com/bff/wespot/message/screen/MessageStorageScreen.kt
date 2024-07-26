package com.bff.wespot.message.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.bff.wespot.designsystem.component.list.WSMessageItem
import com.bff.wespot.designsystem.component.list.WSMessageItemType
import com.bff.wespot.designsystem.theme.Primary400
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.message.R
import com.bff.wespot.message.common.RECEIVED_MESSAGE_INDEX
import com.bff.wespot.message.common.SENT_MESSAGE_INDEX
import com.bff.wespot.message.common.toStringWithDotSeparator
import com.bff.wespot.message.component.ReservedMessageBanner
import com.bff.wespot.message.model.TimePeriod
import com.bff.wespot.message.state.MessageAction
import com.bff.wespot.message.viewmodel.MessageViewModel
import com.bff.wespot.model.message.request.MessageType
import com.bff.wespot.ui.WSHomeChipGroup
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun MessageStorageScreen(
    viewModel: MessageViewModel = hiltViewModel(),
    navigateToReceiverSelectionScreen: (Boolean) -> Unit,
) {
    val chipList = persistentListOf(
        stringResource(R.string.received_message),
        stringResource(R.string.reserved_message),
    )
    var selectedChipIndex by remember { mutableIntStateOf(0) }
    var showMessageDialog by remember { mutableStateOf(false) }

    val state by viewModel.collectAsState()
    val action = viewModel::onAction

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(12.dp))

        WSHomeChipGroup(
            items = chipList,
            selectedItemIndex = selectedChipIndex,
            onSelectedChanged = { index -> selectedChipIndex = index },
        )

        when (selectedChipIndex) {
            RECEIVED_MESSAGE_INDEX -> {
                LazyColumn {
                    items(state.receivedMessageList.messages, key = { it.id }) { item ->
                        WSMessageItem(
                            userInfo = item.senderName,
                            date = item.receivedAt.toStringWithDotSeparator(),
                            wsMessageItemType = if (item.read) {
                                WSMessageItemType.ReadReceivedMessage
                            } else {
                                WSMessageItemType.UnreadReceivedMessage
                            },
                            itemClick = {
                                action(MessageAction.OnMessageItemClicked(item))
                                showMessageDialog = true
                            },
                            optionButtonClick = {
                            },
                        )
                    }
                }
            }

            SENT_MESSAGE_INDEX -> {
                if (state.timePeriod == TimePeriod.EVENING_TO_NIGHT) {
                    ReservedMessageBanner(
                        paddingValues = PaddingValues(top = 12.dp, start = 20.dp, end = 20.dp),
                        messageStatus = state.messageStatus,
                        onBannerClick = { navigateToReceiverSelectionScreen(false) },
                    )
                }

                Text(
                    text = "전송 완료된 쪽지",
                    color = WeSpotThemeManager.colors.txtTitleColor,
                    style = StaticTypeScale.Default.body3,
                )

                // TODO
                LazyColumn {
                    items(state.sentMessageList.messages, key = { it.id }) { item ->
                    }
                }
            }
        }
    }

    if (showMessageDialog) {
        Dialog(onDismissRequest = { }) {
            Box {
                Icon(
                    modifier = Modifier.padding(top = 8.dp, end = 8.dp),
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = stringResource(id = R.string.close),
                )

                Column(
                    modifier = Modifier
                        .clip(WeSpotThemeManager.shapes.extraLarge)
                        .background(WeSpotThemeManager.colors.modalColor)
                        .border(width = 1.dp, color = Primary400)
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    MessageDialogText("To.\n" + state.myProfile.toDescription())

                    MessageDialogText(state.clickedMessage.content)

                    MessageDialogText("From.\n" + state.clickedMessage.senderName)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        action(MessageAction.OnMessageStorageScreenEntered)
        when (selectedChipIndex) {
            RECEIVED_MESSAGE_INDEX -> {
                action(MessageAction.OnStorageChipSelected(MessageType.RECEIVED))
            }

            SENT_MESSAGE_INDEX -> {
                action(MessageAction.OnStorageChipSelected(MessageType.SENT))
            }
        }
    }
}

@Composable
fun MessageDialogText(title: String) {
    Text(
        text = title,
        style = StaticTypeScale.Default.body4,
        color = WeSpotThemeManager.colors.txtTitleColor,
    )
}
