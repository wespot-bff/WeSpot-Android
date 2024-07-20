package com.bff.wespot.message.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bff.wespot.designsystem.component.banner.WSBanner
import com.bff.wespot.designsystem.component.banner.WSBannerType
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.button.WSButtonType
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.indicator.WSHomeTabRow
import com.bff.wespot.designsystem.theme.Gray200
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.designsystem.util.OrientationPreviews
import com.bff.wespot.designsystem.util.textDp
import com.bff.wespot.message.R
import com.bff.wespot.message.common.convertMillisToTime
import com.bff.wespot.message.model.TimePeriod
import com.bff.wespot.message.state.MessageAction
import com.bff.wespot.message.state.MessageSideEffect
import com.bff.wespot.message.state.NavigationAction
import com.bff.wespot.message.viewmodel.MessageViewModel
import com.bff.wespot.model.message.response.MessageList
import com.bff.wespot.model.message.response.MessageStatus
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import com.bff.wespot.designsystem.R.drawable as designSystemDrawable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen(
    viewModel: MessageViewModel = hiltViewModel(),
) {
    val state by viewModel.collectAsState()
    val action = viewModel::onAction
    viewModel.collectSideEffect {
        when (it) {
            is MessageSideEffect.Error -> {
            }

            is MessageSideEffect.NavigateToStorageScreen -> {
            }

            is MessageSideEffect.NavigateToSendScreen -> {
            }

            is MessageSideEffect.NavigateToNotification -> {
            }
        }
    }

    Scaffold(
        topBar = {
            WSTopBar(
                title = "",
                navigation = {
                    Image(
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 8.dp, start = 16.dp)
                            .size(width = 112.dp, height = 44.dp),
                        painter = painterResource(id = designSystemDrawable.logo),
                        contentDescription = stringResource(id = R.string.wespot_logo),
                    )
                },
                action = {
                    IconButton(
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, end = 4.dp),
                        onClick = {
                            action(
                                MessageAction.Navigation(NavigationAction.NavigateToNotification),
                            )
                        },
                    ) {
                        Icon(
                            painter = painterResource(id = designSystemDrawable.notification),
                            contentDescription = stringResource(id = R.string.notification_icon),
                        )
                    }
                },
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            MessageTopBar()

            when (state.timePeriod) {
                TimePeriod.DAWN_TO_EVENING -> {
                    MessageCard(
                        height = state.timePeriod.height,
                        timePeriod = state.timePeriod,
                        title = state.timePeriod.title,
                        buttonText = stringResource(R.string.message_card_button_text_dawn),
                        image = state.timePeriod.image,
                        onButtonClick = { },
                    )
                }

                TimePeriod.EVENING_TO_NIGHT -> {
                    ReservedMessageBanner(
                        messageStatus = state.messageStatus,
                        onBannerClick = {
                            action(
                                MessageAction.Navigation(
                                    NavigationAction.NavigateToStorageScreen,
                                ),
                            )
                        },
                    )

                    MessageCard(
                        height = state.timePeriod.height,
                        timePeriod = state.timePeriod,
                        title = state.timePeriod.title,
                        buttonText = if (state.messageStatus.canSend) {
                            stringResource(R.string.message_card_button_text_evening)
                        } else {
                            stringResource(R.string.message_card_button_text_evening_disabled)
                        },
                        image = state.timePeriod.image,
                        isBannerVisible = state.messageStatus.hasReservedMessages(),
                        isButtonEnable = state.messageStatus.canSend,
                        onButtonClick = {
                            action(
                                MessageAction.Navigation(
                                    NavigationAction.NavigateToSendScreen,
                                ),
                            )
                        },
                    )

                    MessageHomeDescription(
                        title = stringResource(R.string.message_card_description_evening),
                    )
                }

                TimePeriod.NIGHT_TO_DAWN -> {
                    ReceivedMessageBanner(
                        messageList = state.receivedMessageList,
                        onBannerClick = {
                            action(
                                MessageAction.Navigation(
                                    NavigationAction.NavigateToStorageScreen,
                                ),
                            )
                        },
                    )

                    MessageCard(
                        height = state.timePeriod.height,
                        timePeriod = state.timePeriod,
                        title = state.timePeriod.title,
                        buttonText = stringResource(R.string.message_card_button_text_night),
                        image = state.timePeriod.image,
                        isBannerVisible = state.receivedMessageList.hasUnReadMessages(),
                        onButtonClick = { },
                    )

                    MessageHomeDescription(
                        title = stringResource(R.string.message_card_description_night),
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        action(MessageAction.OnHomeScreenEntered)
    }
}

@Composable
private fun MessageTopBar() {
    val tabList = persistentListOf(
        stringResource(R.string.message_home_screen),
        stringResource(R.string.message_storage_screen),
    )
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    WSHomeTabRow(
        selectedTabIndex = selectedTabIndex,
        tabList = tabList,
        onTabSelected = { index -> selectedTabIndex = index },
    )
}

@Composable
private fun MessageCard(
    height: Dp,
    title: String,
    buttonText: String,
    image: Painter,
    timePeriod: TimePeriod,
    isBannerVisible: Boolean = false,
    isButtonEnable: Boolean = false,
    onButtonClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .height(height)
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = if (isBannerVisible) 16.dp else 20.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(WeSpotThemeManager.colors.modalColor),
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = image,
            contentDescription = stringResource(R.string.message_card_image),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp),
                text = title,
                maxLines = 2,
                style = StaticTypeScale.Default.body1,
                color = WeSpotThemeManager.colors.txtTitleColor,
            )

            Spacer(modifier = Modifier.weight(1f))

            if (timePeriod == TimePeriod.EVENING_TO_NIGHT) {
                MessageTimer()
            }

            WSButton(
                text = buttonText,
                paddingValues = PaddingValues(vertical = 0.dp, horizontal = 20.dp),
                buttonType = WSButtonType.Primary,
                enabled = isButtonEnable,
                onClick = { onButtonClick() },
            ) {
                it()
            }
        }
    }
}

@Composable
private fun ReservedMessageBanner(messageStatus: MessageStatus, onBannerClick: () -> Unit) {
    AnimatedVisibility(
        modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp),
        visible = messageStatus.hasReservedMessages() && messageStatus.remainingMessages >= 0,
        enter = slideInVertically { initialOffsetY -> -initialOffsetY },
    ) {
        if (messageStatus.hasRemainingMessages()) {
            WSBanner(
                title = stringResource(
                    R.string.reserved_message_banner_title,
                    messageStatus.getReservedMessageCount(),
                ),
                subTitle = stringResource(
                    R.string.reserved_message_banner_subtitle,
                    messageStatus.remainingMessages,
                ),
                image = painterResource(id = R.drawable.reserved_message),
                onBannerClick = { onBannerClick() },
                bannerType = WSBannerType.Primary,
            )
        } else if (messageStatus.hasNoRemainingMessages()) {
            WSBanner(
                title = stringResource(
                    R.string.reserved_message_banner_title_disabled,
                    messageStatus.getReservedMessageCount(),
                ),
                image = painterResource(id = R.drawable.reserved_message),
                onBannerClick = { onBannerClick() },
                bannerType = WSBannerType.Secondary,
            )
        }
    }
}

@Composable
private fun ReceivedMessageBanner(messageList: MessageList, onBannerClick: () -> Unit) {
    AnimatedVisibility(
        modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp),
        visible = messageList.hasUnReadMessages(),
        enter = slideInVertically { initialOffsetY -> -initialOffsetY },
    ) {
        WSBanner(
            title = stringResource(R.string.received_message_banner_title),
            subTitle = stringResource(R.string.received_message_banner_subtitle),
            image = painterResource(id = R.drawable.received_message),
            onBannerClick = { onBannerClick() },
            bannerType = WSBannerType.Primary,
        )
    }
}

@Composable
private fun MessageTimer(viewModel: MessageViewModel = hiltViewModel()) {
    val remainingTimeMillis by viewModel.remainingTimeMillis.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.padding(bottom = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.message_timer_title),
            style = StaticTypeScale.Default.body9,
            color = WeSpotThemeManager.colors.txtSubColor,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier.size(40.dp),
                painter = painterResource(id = R.drawable.timer),
                contentDescription = "Timer Image",
            )

            Text(
                text = remainingTimeMillis.convertMillisToTime(),
                style = StaticTypeScale.Default.header1.copy(
                    fontSize = 28.textDp,
                    lineHeight = (28 * 1.4f).textDp,
                ),
                color = WeSpotThemeManager.colors.txtTitleColor,
            )
        }
    }
}

@Composable
private fun MessageHomeDescription(title: String) {
    Text(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(),
        text = title,
        style = StaticTypeScale.Default.body6,
        color = Gray200,
        textAlign = TextAlign.Center,
        maxLines = 1,
    )
}

@OrientationPreviews
@Composable
private fun WSBannerPreview() {
    WeSpotTheme {
        Surface {
            Column {
            }
        }
    }
}
