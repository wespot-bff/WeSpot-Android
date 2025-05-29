package com.bff.wespot.message.screen.storage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bff.wespot.designsystem.component.modal.WSDialog
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.message.R
import com.bff.wespot.message.common.BOOKMARKED_MESSAGE_INDEX
import com.bff.wespot.message.component.MessageItem
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
            icon = ImageVector.vectorResource(id = R.drawable.bookmark_chip),
        ),
    )

    var selectedChipIndex by remember { mutableIntStateOf(0) }
    var showOptionBottomSheet by remember { mutableStateOf(false) }
    var showBlockDialog by remember { mutableStateOf(false) }

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

            is StorageSideEffect.ShowOptionBottomSheet -> {
                showOptionBottomSheet = true
            }

            is StorageSideEffect.ShowBlockDialog -> {
                showBlockDialog = true
            }

            is StorageSideEffect.CloseOptionBottomSheet -> {
                showOptionBottomSheet = false
            }

            is StorageSideEffect.CloseBlockDialog -> {
                showBlockDialog = false
            }

            is StorageSideEffect.NavigateToMessageRoom -> {
                // TODO Navigate to Message Room
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        WSChipGroup(
            type = WSChipGroupType.LeadingIcon(chipItems),
            selectedItemIndex = selectedChipIndex,
            onSelectedChanged = { index -> selectedChipIndex = index },
        )

        if (selectedChipIndex == BOOKMARKED_MESSAGE_INDEX && state.showEmptyBookmarkScreen) {
            EmptyBookmarkScreen()
        } else {
            MessageStorageContent(
                data = state.messageList,
                itemClick = { item ->
                    action(StorageAction.OnMessageClicked(message = item))
                },
                optionButtonClick = { message ->
                    action(StorageAction.OnOptionButtonClicked(message = message))
                },
            )
        }
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
                    text = stringResource(R.string.do_block),
                    onClick = {
                        action(StorageAction.OnBlockBottomSheetItemClicked)
                    },
                )

                BottomSheetText(
                    text = if (state.optionButtonClickedMessage.isBookmarked) {
                        stringResource(R.string.undo_bookmark)
                    } else {
                        stringResource(R.string.do_bookmark)
                    },
                    onClick = {
                        action(StorageAction.OnBookmarkBottomSheetItemClicked)
                    },
                )
            }
        }
    }

    if (showBlockDialog) {
        WSDialog(
            title = stringResource(id = R.string.message_block_dialog_title),
            subTitle = stringResource(id = R.string.message_block_dialog_subtitle),
            okButtonText = stringResource(id = R.string.message_block_dialog_ok_button),
            cancelButtonText = stringResource(id = R.string.close),
            okButtonClick = { action(StorageAction.OnBlockButtonClicked) },
            onDismissRequest = { action(StorageAction.OnBlockDialogClosed) },
            cancelButtonClick = { action(StorageAction.OnBlockDialogClosed) },
        )
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
    data: List<Message>,
    itemClick: (Message) -> Unit,
    optionButtonClick: (Message) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(
            data.size,
            key = { index -> data[index].id },
        ) { index ->
            val item = data[index]

            item.let { message ->
                MessageItem(
                    message = message,
                    itemClick = {
                        itemClick(message)
                    },
                    optionButtonClick = {
                        optionButtonClick(message)
                    },
                )
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
