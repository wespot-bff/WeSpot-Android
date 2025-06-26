package com.bff.wespot.notification.screen

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.bff.wespot.common.util.toDateString
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.indicator.WSToastType
import com.bff.wespot.designsystem.theme.Gray400
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.model.notification.Notification
import com.bff.wespot.notification.R
import com.bff.wespot.notification.state.NotificationAction
import com.bff.wespot.notification.state.NotificationSideEffect
import com.bff.wespot.notification.viewmodel.NotificationViewModel
import com.bff.wespot.ui.component.LoadingAnimation
import com.bff.wespot.ui.component.RedDot
import com.bff.wespot.ui.model.ToastState
import com.bff.wespot.ui.util.handleSideEffect
import com.ramcosta.composedestinations.annotation.Destination
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

interface NotificationNavigator {
    fun navigateUp()
    fun navigateToReceiverSelectionScreen()
    fun navigateToVotingScreen()
    fun navigateToVoteResultScreen(
        isNavigateFromNotification: Boolean,
        isTodayVoteResult: Boolean,
    )
    fun navigateToVoteStorageScreen()
    fun navigateToProfileEditScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun NotificationScreen(
    navigator: NotificationNavigator,
    showToast: (ToastState) -> Unit,
    viewModel: NotificationViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.collectAsState()
    val pagingData = state.notificationList.collectAsLazyPagingItems()
    val action = viewModel::onActon

    handleSideEffect(viewModel.sideEffect)

    viewModel.collectSideEffect {
        when (it) {
            is NotificationSideEffect.ShowToast -> {
                showToast(it.toastState)
            }
            is NotificationSideEffect.NavigateByDeepLink -> {
                val intent = Intent(Intent.ACTION_VIEW, it.deepLink.toUri())
                context.startActivity(intent)
            }
            is NotificationSideEffect.NavigateToProfileEditScreen -> {
                navigator.navigateToProfileEditScreen()
            }
            is NotificationSideEffect.NavigateToReceiverSelectionScreen -> {
                navigator.navigateToReceiverSelectionScreen()
            }
            is NotificationSideEffect.NavigateToVotingScreen -> {
                navigator.navigateToVotingScreen()
            }
            is NotificationSideEffect.NavigateToVoteStorageScreen -> {
                navigator.navigateToVoteStorageScreen()
            }
            is NotificationSideEffect.NavigateToVoteResultScreen -> {
                navigator.navigateToVoteResultScreen(
                    isNavigateFromNotification = it.isNavigateFromNotification,
                    isTodayVoteResult = it.isTodayVoteResult,
                )
            }
        }
    }
    Scaffold(
        topBar = {
            WSTopBar(
                title = stringResource(R.string.notification),
                canNavigateBack = true,
                navigateUp = { navigator.navigateUp() },
            )
        },
    ) {
        when (pagingData.loadState.refresh) {
            is LoadState.Error -> {
                showToast(
                    ToastState(
                        show = true,
                        message = R.string.notification_load_error_message,
                        type = WSToastType.Error,
                    ),
                )
            }

            is LoadState.Loading -> {
                LoadingAnimation()
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.padding(it),
                ) {
                    items(
                        pagingData.itemCount,
                        key = pagingData.itemKey { it.id },
                    ) { index ->
                        val item = pagingData[index]

                        item?.let {
                            NotificationListItem(
                                isFirstItem = index == 0,
                                notification = item,
                            ) {
                                action(NotificationAction.OnNotificationClicked(item))
                            }
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        action(NotificationAction.OnNotificationScreenEntered)
    }
}

@Composable
fun NotificationListItem(
    isFirstItem: Boolean,
    notification: Notification,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .let {
                if (notification.isEnable) {
                    it.clickable { onClick() }
                } else {
                    it
                }
            }
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(top = if (isFirstItem) 8.dp else 18.dp)
                .let {
                    if (notification.isEnable.not()) {
                        it.alpha(0.5f)
                    } else {
                        it
                    }
                },
        ) {
            Box {
                Icon(
                    modifier = Modifier.padding(5.dp),
                    painter = painterResource(
                        id = if (notification.type.isVoteNotificationType()) {
                            R.drawable.ic_vote_notification
                        } else {
                            R.drawable.ic_message_notification
                        },
                    ),
                    contentDescription = stringResource(
                        com.bff.wespot.designsystem.R.string.notification_icon,
                    ),
                    tint = WeSpotThemeManager.colors.primaryBtnColor,
                )

                if (notification.isNew) {
                    RedDot(
                        modifier = Modifier
                            .padding(top = 2.dp, end = 2.dp)
                            .align(Alignment.TopEnd)
                            .zIndex(1f),
                        size = 4.dp,
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .weight(1f),
            ) {
                Text(
                    text = notification.type.toDescription(),
                    style = StaticTypeScale.Default.body9,
                    color = WeSpotThemeManager.colors.txtSubColor,
                )

                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text = notification.content,
                    style = StaticTypeScale.Default.body6,
                    color = WeSpotThemeManager.colors.txtTitleColor,
                )
            }

            Text(
                text = notification.createdAt.toDateString(),
                style = StaticTypeScale.Default.body11,
                color = Gray400,
            )
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = WeSpotThemeManager.colors.cardBackgroundColor,
        )
    }
}
