package com.bff.wespot.message.screen

import androidx.annotation.RawRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bff.wespot.designsystem.component.banner.WSBanner
import com.bff.wespot.designsystem.component.banner.WSBannerType
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.button.WSButtonType
import com.bff.wespot.designsystem.theme.Gray600
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.designsystem.util.textDp
import com.bff.wespot.message.R
import com.bff.wespot.message.common.convertMillisToTime
import com.bff.wespot.message.state.MessageAction
import com.bff.wespot.message.viewmodel.MessageViewModel
import com.bff.wespot.model.common.RestrictionArg
import com.bff.wespot.ui.util.handleSideEffect
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun MessageHomeScreen(
    viewModel: MessageViewModel = hiltViewModel(),
    navigateToReceiverSelectionScreen: () -> Unit,
    navigateToMessageStorageScreen: () -> Unit,
    restricted: RestrictionArg,
) {
    val state by viewModel.collectAsState()
    val action = viewModel::onAction

    handleSideEffect(viewModel.sideEffect)

    Column(modifier = Modifier.fillMaxSize()) {
        ReplyMessageBanner(
            visible = state.messageStatus.shouldShowReplyBanner(),
            onBannerClick = navigateToMessageStorageScreen,
        )

        if (state.messageStatus.countRemainingMessages > 0) {
            MessageCard(
                canSendMessage = !restricted.restricted,
                title = state.homeTitle ?: stringResource(
                    R.string.message_card_title_ready_to_send,
                    state.profile.name,
                ),
                buttonText = stringResource(R.string.message_card_button_text),
                imageRes = R.raw.message_evening,
                content = {
                    RemainingMessageCounter(state.messageStatus.countRemainingMessages)
                },
                onButtonClick = {
                    navigateToReceiverSelectionScreen()
                },
            )
        } else {
            MessageCard(
                canSendMessage = false,
                title = stringResource(R.string.message_card_title_all_sent, state.profile.name),
                buttonText = stringResource(R.string.message_card_button_text_disabled),
                content = {
                    MessageTimer(viewModel)
                },
                imageRes = R.raw.message_dawn,
            )
        }
    }

    LaunchedEffect(Unit) {
        action(MessageAction.OnMessageHomeScreenEntered)
    }

    LifecycleStartEffect(Unit) {
        action(MessageAction.OnLifecycleStart)
        onStopOrDispose {
            action(MessageAction.OnLifecycleStop)
        }
    }
}

@Composable
private fun MessageCard(
    canSendMessage: Boolean,
    title: String,
    buttonText: String,
    @RawRes imageRes: Int,
    onButtonClick: () -> Unit = { },
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .height(413.dp)
            .fillMaxWidth()
            .animateContentSize()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Gray600),
    ) {
        MessageLottieAnimation(imageRes)

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

            content()

            WSButton(
                text = buttonText,
                paddingValues = PaddingValues(vertical = 0.dp, horizontal = 20.dp),
                buttonType = WSButtonType.Primary,
                enabled = canSendMessage,
                onClick = { onButtonClick() },
            ) {
                it()
            }
        }
    }
}

@Composable
private fun ReplyMessageBanner(visible: Boolean, onBannerClick: () -> Unit) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically { initialOffsetY -> -initialOffsetY },
    ) {
        Box(modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp)) {
            WSBanner(
                title = stringResource(R.string.received_message_banner_title),
                subTitle = stringResource(R.string.received_message_banner_subtitle),
                image = painterResource(id = R.drawable.received_message),
                onBannerClick = { onBannerClick() },
                bannerType = WSBannerType.Primary,
            )
        }
    }
}

@Composable
private fun MessageTimer(viewModel: MessageViewModel) {
    val remainingTimeMillis by viewModel.remainingTimeMillis.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.padding(bottom = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.message_card_description_all_sent),
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
private fun RemainingMessageCounter(count: Int) {
    Column(
        modifier = Modifier.padding(bottom = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.message_card_description_ready_to_send),
            style = StaticTypeScale.Default.body9,
            color = WeSpotThemeManager.colors.txtSubColor,
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.pencil),
                contentDescription = "Pencil Image",
            )

            Text(
                text = "${count}개",
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
private fun MessageLottieAnimation(
    @RawRes imageRes: Int,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(imageRes))
    val progress by animateLottieCompositionAsState(composition)

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        LottieAnimation(
            modifier = Modifier
                .padding(top = 20.dp)
                .size(320.dp)
                .paint(
                    painter = painterResource(R.drawable.message_gradient_dawn_evening),
                    contentScale = ContentScale.Crop,
                ),
            composition = composition,
            progress = { progress },
        )
    }
}
