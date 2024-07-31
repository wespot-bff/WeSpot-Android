package com.bff.wespot.message.screen

import android.graphics.Color.parseColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.indicator.WSToast
import com.bff.wespot.designsystem.component.indicator.WSToastType
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.message.screen.send.EditMessageScreenArgs
import com.bff.wespot.message.state.MessageAction
import com.bff.wespot.message.state.send.SendAction
import com.bff.wespot.message.viewmodel.MessageViewModel
import com.bff.wespot.message.viewmodel.SendViewModel
import com.bff.wespot.model.message.response.Message
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
                text = "예약 중인 쪽지",
                style = StaticTypeScale.Default.header1,
                color = WeSpotThemeManager.colors.txtTitleColor,
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(state.reservedMessageList, key = { message -> message.id }) { item ->
                    ReservedMessageItem(
                        reservedMessage = item,
                        onClick = {
                            navigator.navigateMessageEditScreen(
                                args = EditMessageScreenArgs(true, item.id),
                            )
                        },
                    )
                }
            }
        }
    }

    if (showToast) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp),
            contentAlignment = Alignment.TopCenter,
        ) {
            WSToast(
                text = "수정 완료",
                toastType = WSToastType.Success,
                showToast = showToast,
                closeToast = { showToast = false },
            )
        }
    }

    LaunchedEffect(Unit) {
        action(MessageAction.OnReservedMessageScreenEntered)
        sendViewModel.onAction(SendAction.OnReservedMessageScreenEntered)
        showToast = navArgs.isMessageEdit
    }
}

@Composable
fun ReservedMessageItem(
    reservedMessage: Message,
    onClick: () -> Unit,
) {
    Column {
        Row(
            modifier = Modifier.padding(start = 18.dp, end = 18.dp, top = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val profile = reservedMessage.receiver.profileCharacter

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        runCatching {
                            Color(parseColor(profile.backgroundColor))
                        }.getOrDefault(WeSpotThemeManager.colors.cardBackgroundColor),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(profile.iconUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(com.bff.wespot.ui.R.string.user_character_image),
                )
            }

            Text(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .weight(1f),
                text = "To.\n" + reservedMessage.receiver.toDescription(),
                style = StaticTypeScale.Default.body6,
                color = WeSpotThemeManager.colors.txtSubColor,
            )

            FilterChip(
                shape = WeSpotThemeManager.shapes.extraLarge,
                onClick = {
                    onClick()
                },
                selected = false,
                label = {
                    Text(
                        text = "수정하기",
                        style = StaticTypeScale.Default.body6,
                    )
                },
                border = null,
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = WeSpotThemeManager.colors.secondaryBtnColor,
                    labelColor = Color(0xFFF7F7F8),
                ),
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(top = 24.dp, start = 24.dp, end = 24.dp),
            thickness = 1.dp,
            color = WeSpotThemeManager.colors.cardBackgroundColor,
        )
    }
}

@Preview
@Composable
fun preview() {
    WeSpotTheme {
        ReservedMessageItem(
            reservedMessage = Message(),
            onClick = { },
        )
    }
}
