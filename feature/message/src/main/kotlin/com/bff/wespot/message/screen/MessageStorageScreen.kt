package com.bff.wespot.message.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
        WSHomeChipGroup(
            items = chipList,
            selectedItemIndex = selectedChipIndex,
            onSelectedChanged = { index -> selectedChipIndex = index },
        )

        when (selectedChipIndex) {
            RECEIVED_MESSAGE_INDEX -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(state.receivedMessageList.messages, key = { it.id }) { item ->
                        WSMessageItem(
                            userInfo = item.senderName,
                            date = item.receivedAt.toStringWithDotSeparator(),
                            wsMessageItemType = if (item.isRead) {
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
                    modifier = Modifier.padding(start = 24.dp),
                    text = "전송 완료된 쪽지",
                    color = WeSpotThemeManager.colors.txtTitleColor,
                    style = StaticTypeScale.Default.body3,
                )

                // TODO
            }
        }
    }

    if (showMessageDialog) {
        Dialog(onDismissRequest = { }) {
            Box(
                modifier = Modifier.size(width = 310.dp, height = 376.dp),
            ) {
                Column(
                    modifier = Modifier
                        .clip(WeSpotThemeManager.shapes.extraLarge)
                        .background(WeSpotThemeManager.colors.modalColor)
                        .border(
                            width = 1.dp,
                            color = Primary400,
                            shape = WeSpotThemeManager.shapes.extraLarge,
                        )
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    MessageDialogText("To.\n" + state.myProfile.toDescription())

                    MessageDialogText(state.clickedMessage.content)

                    Spacer(modifier = Modifier.weight(1f))

                    MessageDialogText(
                        text = "From.\n" + state.clickedMessage.senderName,
                        textAlign = TextAlign.End,
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, end = 8.dp)
                        .clickable { showMessageDialog = false },
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.close),
                        contentDescription = stringResource(id = R.string.close),
                    )
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
fun MessageDialogText(text: String, textAlign: TextAlign = TextAlign.Start) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = text,
        style = StaticTypeScale.Default.body4,
        color = WeSpotThemeManager.colors.txtTitleColor,
        textAlign = textAlign,
    )
}
