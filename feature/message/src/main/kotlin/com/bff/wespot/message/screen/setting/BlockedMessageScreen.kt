package com.bff.wespot.message.screen.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.message.R
import com.bff.wespot.message.component.MessageItem
import com.bff.wespot.message.component.MessageItemType
import com.bff.wespot.message.state.setting.BlockedMessageAction
import com.bff.wespot.message.state.setting.BlockedMessageSideEffect
import com.bff.wespot.message.viewmodel.BlockedMessageViewModel
import com.bff.wespot.ui.model.ToastState
import com.bff.wespot.ui.util.handleSideEffect
import com.ramcosta.composedestinations.annotation.Destination
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

interface BlockedMessageNavigator {
    fun navigateUp()
}

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun BlockedMessageScreen(
    viewModel: BlockedMessageViewModel = hiltViewModel(),
    navigator: BlockedMessageNavigator,
    showToast: (ToastState) -> Unit,
) {
    val state by viewModel.collectAsState()
    val action = viewModel::onAction

    handleSideEffect(viewModel.sideEffect)

    viewModel.collectSideEffect {
        when (it) {
            is BlockedMessageSideEffect.ShowToast -> {
                showToast(it.toastState)
            }
        }
    }

    Scaffold(
        topBar = {
            WSTopBar(
                title = "",
                canNavigateBack = true,
                navigateUp = { navigator.navigateUp() },
            )
        },
    ) {
        Column(
            modifier = Modifier.padding(it),
        ) {
            Text(
                modifier = Modifier.padding(bottom = 16.dp, start = 30.dp, end = 30.dp),
                text = stringResource(R.string.blocked_message_title),
                style = StaticTypeScale.Default.header1,
                color = WeSpotThemeManager.colors.txtTitleColor,
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 20.dp, end = 20.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(
                    count = state.messageList.size,
                    key = { index -> state.messageList[index].id },
                ) { index ->
                    val item = state.messageList[index]

                    item.let { message ->
                        MessageItem(
                            itemType = MessageItemType.Blocked(message.isBlocked),
                            message = message,
                            itemClick = { },
                            optionButtonClick = {
                                action(BlockedMessageAction.OnUnBlockButtonClicked(item.id))
                            },
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        action(BlockedMessageAction.OnScreenEntered)
    }
}
