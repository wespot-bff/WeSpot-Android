package com.bff.wespot.entire.screen.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.entire.R
import com.bff.wespot.entire.component.EntireListItem
import com.bff.wespot.navigation.Navigator
import com.bff.wespot.navigation.util.WebLink
import com.ramcosta.composedestinations.annotation.Destination

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
) {
    val context = LocalContext.current

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
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                thickness = 1.dp,
                color = WeSpotThemeManager.colors.cardBackgroundColor,
            )

            EntireListItem(text = stringResource(R.string.privacy_policy)) {
                activityNavigator.navigateToWebLink(context, WebLink.PRIVACY_POLICY)
            }

            EntireListItem(text = stringResource(R.string.terms_of_service)) {
                activityNavigator.navigateToWebLink(context, WebLink.TERMS_OF_SERVICE)
            }

            EntireListItem(text = stringResource(R.string.latest_updates)) {
                activityNavigator.navigateToWebLink(context, WebLink.PLAY_STORE)
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
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
}
