package com.bff.wespot.message.screen.storage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.bff.wespot.designsystem.component.indicator.WSToastType
import com.bff.wespot.designsystem.component.modal.WSDialog
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.message.R
import com.bff.wespot.message.component.MessageItem
import com.bff.wespot.message.component.MessageItemType
import com.bff.wespot.message.model.MessageOptionType
import com.bff.wespot.message.screen.MessageReportScreen
import com.bff.wespot.message.state.storage.StorageAction
import com.bff.wespot.message.state.storage.StorageSideEffect
import com.bff.wespot.message.viewmodel.StorageViewModel
import com.bff.wespot.model.message.response.Message
import com.bff.wespot.model.notification.NotificationType
import com.bff.wespot.ui.component.LoadingAnimation
import com.bff.wespot.ui.component.NetworkDialog
import com.bff.wespot.ui.component.WSBottomSheet
import com.bff.wespot.ui.component.WSChipGroup
import com.bff.wespot.ui.component.WSChipGroupType
import com.bff.wespot.ui.model.ToastState
import com.bff.wespot.ui.util.handleSideEffect
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageStorageScreen(
    type: NotificationType,
    messageId: Int? = null,
    showToast: (ToastState) -> Unit,
    viewModel: StorageViewModel = hiltViewModel(),
) {
    val chipItems = persistentListOf(
        WSChipGroupType.WSIconChipItem(
            label = stringResource(R.string.all),
            icon = ImageVector.vectorResource(id = R.drawable.all),
        ),
        WSChipGroupType.WSIconChipItem(
            label = stringResource(R.string.favories),
            icon = ImageVector.vectorResource(id = R.drawable.favorites_chip),
        ),
    )

    var selectedChipIndex by remember { mutableIntStateOf(0) }
    var showOptionBottomSheet by remember { mutableStateOf(false) }
    var showOptionDialog by remember { mutableStateOf(false) }
    var showMessageReportScreen by remember { mutableStateOf(false) }

    val networkState by viewModel.networkState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val state by viewModel.collectAsState()
    val action = viewModel::onAction

    handleSideEffect(viewModel.sideEffect)

    viewModel.collectSideEffect {
        when (it) {
            is StorageSideEffect.ShowToast -> {
                showToast(it.toastState)
            }

            is StorageSideEffect.ShowReportMessageScreen -> {
                showMessageReportScreen = true
            }

            is StorageSideEffect.ShowOptionBottomSheet -> {
                showOptionBottomSheet = true
            }

            is StorageSideEffect.ShowOptionDialog -> {
                showOptionDialog = true
            }

            is StorageSideEffect.CloseOptionBottomSheet -> {
                showOptionBottomSheet = false
            }

            is StorageSideEffect.CloseOptionDialog -> {
                showOptionDialog = false
            }

            is StorageSideEffect.CloseReportMessageScreen -> {
                showMessageReportScreen = false
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        WSChipGroup(
            type = WSChipGroupType.LeadingIcon(chipItems),
            selectedItemIndex = selectedChipIndex,
            onSelectedChanged = { index -> selectedChipIndex = index },
        )

        MessageStorageContent(
            data = state.messageList.collectAsLazyPagingItems(),
            showToast = showToast,
            itemClick = { item ->
                action(StorageAction.OnMessageClicked(message = item))
            },
            optionButtonClick = { messageId ->
                action(StorageAction.OnOptionButtonClicked(messageId = messageId))
            },
            bookmarkedButtonClick = { messageId ->
                action(StorageAction.OnBookmarkButtonClicked(messageId = messageId))
            },
        )
    }

    if (showOptionBottomSheet) {
        WSBottomSheet(
            closeSheet = { action(StorageAction.OnOptionBottomSheetClosed) },
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 32.dp),
            ) {
                BottomSheetText(
                    text = stringResource(R.string.delete),
                    onClick = {
                        action(StorageAction.OnOptionBottomSheetClicked(MessageOptionType.DELETE))
                    },
                )

                BottomSheetText(
                    text = stringResource(R.string.report_title),
                    onClick = {
                        action(StorageAction.OnOptionBottomSheetClicked(MessageOptionType.REPORT))
                    },
                )

                BottomSheetText(
                    text = stringResource(R.string.block),
                    showDivider = false,
                    onClick = {
                        action(StorageAction.OnOptionBottomSheetClicked(MessageOptionType.BLOCK))
                    },
                )
            }
        }
    }

    if (showOptionDialog) {
        WSDialog(
            title = state.messageOptionType.title,
            subTitle = state.messageOptionType.subTitle,
            okButtonText = state.messageOptionType.okButtonText,
            cancelButtonText = state.messageOptionType.cancelButtonText,
            okButtonClick = {
                when (state.messageOptionType) {
                    MessageOptionType.DELETE -> {
                        action(StorageAction.OnMessageDeleteButtonClicked)
                    }
                    MessageOptionType.BLOCK -> {
                        action(StorageAction.OnMessageBlockButtonClicked)
                    }
                    MessageOptionType.REPORT -> {
                        action(StorageAction.OnMessageReportButtonClicked)
                    }
                }
            },
            onDismissRequest = { action(StorageAction.OnOptionDialogClosed) },
            cancelButtonClick = { action(StorageAction.OnOptionDialogClosed) },
        )
    }

    if (showMessageReportScreen) {
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(usePlatformDefaultWidth = false),
        ) {
            MessageReportScreen(
                messageId = state.optionButtonClickedMessageId,
                showToast = showToast,
                onDismiss = { action(StorageAction.OnMessageReportScreenClosed) },
            )
        }
    }

    if (state.isLoading) {
        LoadingAnimation()
    }

    NetworkDialog(context = context, networkState = networkState)

    LaunchedEffect(Unit) {
        when (type) {
            NotificationType.MESSAGE_RECEIVED, NotificationType.MESSAGE_SENT -> {
                action(
                    StorageAction.OnPushNotificationNavigated(
                        messageId = messageId ?: return@LaunchedEffect,
                    ),
                )
            }

            else -> { }
        }
    }

    LaunchedEffect(selectedChipIndex) {
        action(StorageAction.OnStorageChipSelected(selectedChipIndex))
    }
}

@Composable
internal fun MessageStorageContent(
    data: LazyPagingItems<Message>,
    itemClick: (Message) -> Unit,
    optionButtonClick: (Int) -> Unit,
    bookmarkedButtonClick: (Int) -> Unit,
    showToast: (ToastState) -> Unit,
) {
    when (data.loadState.refresh) {
        is LoadState.Error -> {
            showToast(
                ToastState(
                    show = true,
                    message = R.string.load_message_error_message,
                    type = WSToastType.Error,
                ),
            )
        }

        is LoadState.Loading -> {
            LoadingAnimation()
        }

        else -> {
            if (data.itemCount == 0) {
                EmptyMessageScreen()
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(
                    data.itemCount,
                    key = data.itemKey { it.id },
                ) { index ->
                    val item = data[index]

                    item?.let { message ->
                        MessageItem(
                            message = message,
                            messageItemType = when {
                                message.isBlocked -> MessageItemType.Blocked
                                message.isReported -> MessageItemType.Reported
                                message.isEver -> MessageItemType.Ever
                                else -> MessageItemType.Normal(item.isBookmarked)
                            },
                            itemClick = {
                                itemClick(message)
                            },
                            optionButtonClick = {
                                optionButtonClick(message.id)
                            },
                            favoritesButtonClick = {
                                bookmarkedButtonClick(message.id)
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomSheetText(
    text: String,
    showDivider: Boolean = true,
    onClick: () -> Unit,
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        text = text,
        style = StaticTypeScale.Default.body4,
        color = Color(0xFFF7F7F8),
        textAlign = TextAlign.Center,
    )

    if (showDivider) {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Color(0xFF4F5157),
        )
    }
}
