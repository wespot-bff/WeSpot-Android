package com.bff.wespot.message.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bff.wespot.designsystem.R.string
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.indicator.WSToastType
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.message.R
import com.bff.wespot.message.screen.send.EditMessageScreenArgs
import com.bff.wespot.message.state.MessageAction
import com.bff.wespot.message.state.send.SendAction
import com.bff.wespot.message.viewmodel.MessageViewModel
import com.bff.wespot.message.viewmodel.SendViewModel
import com.bff.wespot.ui.LoadingAnimation
import com.bff.wespot.ui.NetworkDialog
import com.bff.wespot.ui.ReservedMessageItem
import com.bff.wespot.ui.TopToast
import com.bff.wespot.util.handleSideEffect
import com.ramcosta.composedestinations.annotation.Destination
import org.orbitmvi.orbit.compose.collectAsState

interface ReservedMessageNavigator {
    fun navigateUp()
    fun navigateMessageEditScreen(args: EditMessageScreenArgs)
}

data class ReservedMessageScreenArgs(
    val isMessageEdit: Boolean,
)

@OptIn(ExperimentalMaterial3Api::class)
@Destination(navArgsDelegate = ReservedMessageScreenArgs::class)
@Composable
fun ReservedMessageScreen(
    navigator: ReservedMessageNavigator,
    navArgs: ReservedMessageScreenArgs,
    sendViewModel: SendViewModel,
    viewModel: MessageViewModel = hiltViewModel(),
) {
    var showToast by remember { mutableStateOf(false) }

    val state by viewModel.collectAsState()
    val action = viewModel::onAction

    val networkState by viewModel.networkState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    viewModel.sideEffect.handleSideEffect()

    Scaffold(
        topBar = {
            WSTopBar(
                title = "",
                canNavigateBack = true,
                navigateUp = {
                    navigator.navigateUp()
                },
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, start = 24.dp, end = 24.dp),
                text = stringResource(R.string.reserving_message),
                style = StaticTypeScale.Default.header1,
                color = WeSpotThemeManager.colors.txtTitleColor,
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(state.reservedMessageList, key = { message -> message.id }) { item ->
                    ReservedMessageItem(
                        title = stringResource(string.letter_receiver),
                        subTitle = item.receiver.toDescription(),
                        backgroundColor = item.receiver.profileCharacter.backgroundColor,
                        iconUrl = item.receiver.profileCharacter.iconUrl,
                        chipText = stringResource(R.string.message_edit),
                        onClick = {
                            navigator.navigateMessageEditScreen(
                                args = EditMessageScreenArgs(true, item.id),
                            )
                        },
                    )
                }
            }
        }

        if (state.isLoading) {
            LoadingAnimation()
        }
    }

    TopToast(
        message = stringResource(id = R.string.edit_done),
        toastType = WSToastType.Success,
        showToast = showToast,
    ) {
        showToast = false
    }

    NetworkDialog(context = context, networkState = networkState)

    LaunchedEffect(Unit) {
        action(MessageAction.OnReservedMessageScreenEntered)
        sendViewModel.onAction(SendAction.OnReservedMessageScreenEntered)
        showToast = navArgs.isMessageEdit
    }
}
