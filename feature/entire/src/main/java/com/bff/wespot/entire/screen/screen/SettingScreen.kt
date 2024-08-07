package com.bff.wespot.entire.screen.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.entire.R
import com.bff.wespot.entire.screen.component.EntireListItem
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
) {
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
                .padding(top = 8.dp, start = 24.dp, end = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            EntireListItem(text = stringResource(R.string.notification_setting)) {
                navigator.navigateToNotificationSetting()
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = WeSpotThemeManager.colors.cardBackgroundColor,
            )

            EntireListItem(text = stringResource(R.string.privacy_policy)) {
            }

            EntireListItem(text = stringResource(R.string.terms_of_service)) {
            }

            EntireListItem(text = stringResource(R.string.latest_updates)) {
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
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
