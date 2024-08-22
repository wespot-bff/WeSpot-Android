package com.bff.wespot.entire.screen.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.domain.util.RemoteConfigKey
import com.bff.wespot.entire.R
import com.bff.wespot.entire.component.EntireListItem
import com.bff.wespot.entire.state.EntireAction
import com.bff.wespot.entire.viewmodel.EntireViewModel
import com.bff.wespot.navigation.Navigator
import com.ramcosta.composedestinations.annotation.Destination
import org.orbitmvi.orbit.compose.collectAsState

interface SettingNavigator {
    fun navigateUp()
    fun navigateToNotificationSetting()
    fun navigateToAccountSetting()
    fun navigateToBlockListScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun SettingScreen(
    navigator: SettingNavigator,
    activityNavigator: Navigator,
    viewModel: EntireViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val state by viewModel.collectAsState()
    val action = viewModel::onAction

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
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(top = 4.dp, start = 24.dp, end = 24.dp),
        ) {
            EntireListItem(text = stringResource(R.string.notification_setting)) {
                navigator.navigateToNotificationSetting()
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                thickness = 1.dp,
                color = WeSpotThemeManager.colors.cardBackgroundColor,
            )

            EntireListItem(text = stringResource(R.string.privacy_policy)) {
                activityNavigator.navigateToWebLink(
                    context = context,
                    webLink = state.webLinkMap.getOrDefault(
                        RemoteConfigKey.PRIVACY_POLICY_URL,
                        context.getString(R.string.privacy_policy_url),
                    ),
                )
            }

            EntireListItem(text = stringResource(R.string.terms_of_service)) {
                activityNavigator.navigateToWebLink(
                    context = context,
                    webLink = state.webLinkMap.getOrDefault(
                        RemoteConfigKey.TERMS_OF_SERVICE_URL,
                        context.getString(R.string.terms_of_service_url),
                    ),
                )
            }

            EntireListItem(text = stringResource(R.string.latest_updates)) {
                activityNavigator.navigateToWebLink(
                    context = context,
                    webLink = state.webLinkMap.getOrDefault(
                        RemoteConfigKey.PLAY_STORE_URL,
                        context.getString(R.string.play_store_url),
                    ),
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                thickness = 1.dp,
                color = WeSpotThemeManager.colors.cardBackgroundColor,
            )

            EntireListItem(text = stringResource(R.string.block_list)) {
                navigator.navigateToBlockListScreen()
            }

            EntireListItem(text = stringResource(R.string.account_setting)) {
                navigator.navigateToAccountSetting()
            }
        }
    }

    LaunchedEffect(Unit) {
        action(EntireAction.OnSettingScreenEntered)
    }
}
