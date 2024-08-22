package com.bff.wespot.auth.screen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bff.wespot.analytic.AnalyticsEvent
import com.bff.wespot.analytic.AnalyticsEvent.Param
import com.bff.wespot.analytic.LocalAnalyticsHelper
import com.bff.wespot.analytic.TrackScreenViewEvent
import com.bff.wespot.auth.R
import com.bff.wespot.auth.state.AuthAction
import com.bff.wespot.auth.viewmodel.AuthViewModel
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.button.WSOutlineButton
import com.bff.wespot.designsystem.component.modal.WSDialog
import com.bff.wespot.navigation.Navigator
import com.ramcosta.composedestinations.annotation.Destination
import org.orbitmvi.orbit.compose.collectAsState

@Composable
@Destination
fun CompleteScreen(
    viewModel: AuthViewModel,
    navigator: Navigator,
) {
    var dialog by remember {
        mutableStateOf(false)
    }

    val state by viewModel.collectAsState()

    val activity = (LocalContext.current as? Activity)
    val context = LocalContext.current
    var inviteClicked by remember {
        mutableStateOf(false)
    }

    val analyticsHelper = LocalAnalyticsHelper.current

    val message = context.getString(com.bff.wespot.designsystem.R.string.invite_message)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 86.dp),
    ) {
        Image(
            painter = painterResource(id = R.drawable.register_complete),
            contentDescription = stringResource(R.string.register_done),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
        )
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Column {
            WSButton(
                onClick = {
                    navigator.navigateToSharing(
                        context,
                        message + state.playStoreLink,
                    )
                    inviteClicked = true
                },
                text = stringResource(id = R.string.invite_friend_and_start),
                paddingValues = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            ) {
                it.invoke()
            }

            WSOutlineButton(
                onClick = {
                    analyticsHelper.logEvent(
                        AnalyticsEvent(
                            type = "invite_friend_before_sign_up",
                            extras = listOf(
                                Param("screen_name", "complete_sign_up"),
                                Param("invite_clicked", inviteClicked.toString()),
                            ),
                        ),
                    )
                    viewModel.onAction(AuthAction.Signup)
                },
                text = stringResource(id = R.string.start),
                paddingValues = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            ) {
                it.invoke()
            }
        }
    }

    BackHandler {
        dialog = true
    }

    if (dialog) {
        WSDialog(
            title = stringResource(id = R.string.finish_check),
            subTitle = stringResource(id = R.string.finish_check_detail),
            onDismissRequest = {
                dialog = false
            },
            okButtonClick = {
                activity?.finish()
            },
            cancelButtonClick = {
                dialog = false
            },
            okButtonText = stringResource(id = R.string.ok),
            cancelButtonText = stringResource(id = R.string.cancel),
        )
    }

    TrackScreenViewEvent(screenName = "complete_screen", id = state.uuid)
}
