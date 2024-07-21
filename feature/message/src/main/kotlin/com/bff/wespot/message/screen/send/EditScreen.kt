package com.bff.wespot.message.screen.send

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.button.WSButtonType
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.input.WsTextField
import com.bff.wespot.designsystem.component.input.WsTextFieldType
import com.bff.wespot.designsystem.component.modal.WSDialog
import com.bff.wespot.designsystem.component.toggle.WSSwitch
import com.bff.wespot.designsystem.theme.Gray400
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.message.state.send.NavigationAction
import com.bff.wespot.message.state.send.SendAction
import com.bff.wespot.message.viewmodel.SendViewModel
import com.bff.wespot.ui.LetterCountIndicator
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    viewModel: SendViewModel = hiltViewModel(),
) {
    var dialogState by remember { mutableStateOf(false) }
    var reserveDialogState by remember { mutableStateOf(false) }

    val state by viewModel.collectAsState()
    val action = viewModel::onAction

    Scaffold(
        topBar = {
            WSTopBar(
                title = "",
                canNavigateBack = true,
                action = {
                    Text(
                        modifier = Modifier.clickable {
                            dialogState = true
                        },
                        text = "닫기",
                        style = StaticTypeScale.Default.body4,
                        color = WeSpotThemeManager.colors.abledTxtColor,
                    )
                },
            )
        },
    ) {
        Column(modifier = Modifier.padding(it)) {
            EditField(
                title = "받는 사람",
                value = requireNotNull(state.selectedUser).toDescription(),
            ) {
                action(
                    SendAction.Navigation(
                        NavigationAction.NavigateToReceiverScreen(true),
                    ),
                )
            }

            Column {
                Text(
                    text = "전달할 내용",
                    style = StaticTypeScale.Default.body4,
                    modifier = Modifier.padding(top = 16.dp, start = 30.dp, end = 30.dp),
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .clickable {
                            action(
                                SendAction.Navigation(
                                    NavigationAction.NavigateToWriteScreen(true),
                                ),
                            )
                        },
                ) {
                    WsTextField(
                        value = state.messageInput,
                        onValueChange = { },
                        placeholder = "",
                        isError = false,
                        textFieldType = WsTextFieldType.Message,
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    LetterCountIndicator(currentCount = state.messageInput.length, maxCount = 200)
                }
            }

            EditField(
                title = "보내는 사람",
                value = if (state.isRandomName) state.randomName else state.profile.toDescription(),
            ) {
                action(
                    SendAction.Navigation(
                        NavigationAction.NavigateToReceiverScreen(true),
                    ),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, start = 30.dp, end = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(
                        text = "랜덤 닉네임으로 보내기",
                        style = StaticTypeScale.Default.body1,
                        color = WeSpotThemeManager.colors.txtTitleColor,
                    )

                    Text(
                        text = "이 기능을 끄면 실명으로 전송돼요",
                        style = StaticTypeScale.Default.body8,
                        color = Gray400,
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                WSSwitch(checked = state.isRandomName) {
                    action(SendAction.OnRandomNameToggled(it))
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            WSButton(
                onClick = {
                    reserveDialogState = true
                },
                text = "쪽지 보내기",
            ) {
                it()
            }
        }

        if (dialogState) {
            WSDialog(
                title = "쪽지 작성을 중단하시나요",
                subTitle = "작성 중인 내용은 저장되지 않아요",
                okButtonText = "네 그만할래요",
                cancelButtonText = "닫기",
                okButtonClick = {
                    action(SendAction.Navigation(NavigationAction.NavigateToMessageScreen))
                },
                cancelButtonClick = { dialogState = false },
                onDismissRequest = { dialogState = false },
            )
        }

        if (reserveDialogState) {
            WSDialog(
                title = "쪽지를 예약할까요?",
                subTitle = "오늘 밤 10시에 상대에게 쪽지를 전달해 드릴게요\n예약 이후 전송 취소는 어려워요",
                okButtonText = "네 예약할래요",
                cancelButtonText = "취소",
                okButtonClick = { action(SendAction.SendMessage) },
                cancelButtonClick = { reserveDialogState = false },
                onDismissRequest = { reserveDialogState = false },
            )
        }
    }
}

@Composable
private fun EditField(
    title: String,
    value: String,
    onClicked: () -> Unit,
) {
    Column {
        Text(
            text = title,
            style = StaticTypeScale.Default.body4,
            modifier = Modifier.padding(horizontal = 30.dp),
        )

        WSButton(
            onClick = onClicked,
            buttonType = WSButtonType.Tertiary,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
            ) {
                Text(
                    text = value,
                    style = StaticTypeScale.Default.body4,
                )
            }
        }
    }
}
