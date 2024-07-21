package com.bff.wespot.message.screen.send

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.input.WsTextField
import com.bff.wespot.designsystem.component.input.WsTextFieldType
import com.bff.wespot.designsystem.component.modal.WSDialog
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.message.state.send.NavigationAction
import com.bff.wespot.message.state.send.SendAction
import com.bff.wespot.message.viewmodel.SendViewModel
import com.bff.wespot.ui.LetterCountIndicator
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteScreen(
    isEditing: Boolean,
    viewModel: SendViewModel = hiltViewModel(),
) {
    var dialogState by remember { mutableStateOf(false) }
    val keyboard = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

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
                        },
                        text = "닫기",
                        style = StaticTypeScale.Default.body4,
                        color = WeSpotThemeManager.colors.abledTxtColor,
                    )
                },
            )
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 20.dp),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 3.dp),
                text = "쪽지 내용을 작성해주세요\n상대에게 익명으로 전달해 드릴게요",
                style = StaticTypeScale.Default.header1,
                color = WeSpotThemeManager.colors.txtTitleColor,
            )

            Spacer(modifier = Modifier.padding(top = 16.dp))

            WsTextField(
                value = state.messageInput,
                onValueChange = {
                    action(SendAction.OnSearchContentChanged(it))
                },
                placeholder = "오늘 바나나 우유 고마웠어! 점심 시간에 경기하는 거 봤는데 잘 하더라! 다치지 말고 화이팅 해!",
                isError = false,
                focusRequester = focusRequester,
                textFieldType = WsTextFieldType.Message,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (state.hasProfanity) {
                    Text(
                        modifier = Modifier.padding(top = 4.dp, start = 10.dp, end = 10.dp),
                        text = "비속어가 포함되어 있어요",
                        style = StaticTypeScale.Default.body7,
                        color = WeSpotThemeManager.colors.dangerColor,
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                LetterCountIndicator(currentCount = state.messageInput.length, maxCount = 200)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        WSButton(
            onClick = {
                if (isEditing) {
                    action(SendAction.Navigation(NavigationAction.PopBackStack))
                    return@WSButton
                }
                action(SendAction.Navigation(NavigationAction.NavigateToEditScreen))
            },
            enabled = state.messageInput.length <= 200 && state.hasProfanity.not(),
            text = if (isEditing) "작성 완료" else "수정 완료",
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

    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
        delay(10)
        keyboard?.show()
    }
}
