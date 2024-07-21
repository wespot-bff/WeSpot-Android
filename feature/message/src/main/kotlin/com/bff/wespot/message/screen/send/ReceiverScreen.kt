package com.bff.wespot.message.screen.send

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.input.WsTextField
import com.bff.wespot.designsystem.component.input.WsTextFieldType
import com.bff.wespot.designsystem.component.modal.WSDialog
import com.bff.wespot.designsystem.theme.Gray300
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.message.state.send.NavigationAction
import com.bff.wespot.message.state.send.SendAction
import com.bff.wespot.message.viewmodel.SendViewModel
import com.bff.wespot.ui.UserListItem
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiverScreen(
    isEditing: Boolean,
    viewModel: SendViewModel = hiltViewModel(),
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    var dialogState by remember { mutableStateOf(false) }

    val state by viewModel.collectAsState()
    val action = viewModel::onAction

    Scaffold(
        topBar = {
            WSTopBar(
                title = "",
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
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 20.dp),
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 4.dp),
                text = "오늘 당신의 마음을 설레게 한\n친구는 누구인가요?",
                style = StaticTypeScale.Default.header1,
                color = WeSpotThemeManager.colors.txtTitleColor,
            )

            Spacer(modifier = Modifier.height(16.dp))

            WsTextField(
                value = state.nameInput,
                onValueChange = {
                    action(SendAction.OnSearchContentChanged(it))
                },
                placeholder = "이름으로 검색해 보세요",
                textFieldType = WsTextFieldType.Search,
                focusRequester = focusRequester,
                singleLine = true,
            )

            if (state.userList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        modifier = Modifier
                            .drawBehind {
                                drawLine(
                                    strokeWidth = 1f * density,
                                    color = Gray300,
                                    start = Offset(0f, size.height),
                                    end = Offset(size.width, size.height),
                                )
                            }
                            .clickable {
                                action(SendAction.OnInviteFriendTextClicked)
                            },
                        text = "찾는 친구가 없다면",
                        style = StaticTypeScale.Default.body5,
                        color = WeSpotThemeManager.colors.txtSubColor,
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.padding(top = 16.dp),
            ) {
                items(state.userList, key = { user -> user.id }) { item ->
                    UserListItem(
                        title = item.name,
                        subTitle = item.toSchoolInfo(),
                        selected = state.selectedUser?.id == item.id,
                        onClick = {
                            action(SendAction.OnUserSelected(item))
                        },
                    )
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        WSButton(
            onClick = {
                if (isEditing) {
                    action(SendAction.Navigation(NavigationAction.PopBackStack))
                    return@WSButton
                }
                action(SendAction.Navigation(NavigationAction.NavigateToWriteScreen(false)))
            },
            enabled = state.selectedUser.name.isNotBlank(),
            text = if (isEditing) "다음" else "수정 완료",
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

    LaunchedEffect(Unit) {
        action(SendAction.OnReceiverScreenEntered)
    }
}
