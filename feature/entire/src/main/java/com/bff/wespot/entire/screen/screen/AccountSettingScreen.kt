package com.bff.wespot.entire.screen.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.modal.WSDialog
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.entire.R
import com.ramcosta.composedestinations.annotation.Destination

interface AccountSettingNavigator {
    fun navigateUp()
}

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun AccountSettingScreen(
    navigator: AccountSettingNavigator,
) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            WSTopBar(
                title = stringResource(id = R.string.account_setting),
                canNavigateBack = true,
                navigateUp = { navigator.navigateUp() },
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(top = 16.dp, start = 24.dp, end = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            AccountSettingItem(title = stringResource(R.string.sign_out)) {
                showDialog = true
            }

            AccountSettingItem(title = stringResource(R.string.revoke)) {
            }
        }
    }

    if (showDialog) {
        WSDialog(
            title = stringResource(R.string.sign_out_dialog_title),
            subTitle = "",
            okButtonText = stringResource(R.string.close),
            cancelButtonText = stringResource(id = R.string.sign_out),
            okButtonClick = { showDialog = false },
            cancelButtonClick = {
                // TODO SIGN_OUT
            },
            onDismissRequest = { showDialog = false },
        )
    }
}

@Composable
fun AccountSettingItem(title: String, onClick: () -> Unit) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        text = title,
        style = StaticTypeScale.Default.body4,
        color = WeSpotThemeManager.colors.txtTitleColor,
    )
}
