package com.bff.wespot.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.theme.Gray400
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.designsystem.util.OrientationPreviews

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WSHomeTopAppBar(
    onClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            Image(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp, start = 15.dp)
                    .size(width = 112.dp, height = 43.dp),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(id = R.string.wespot_logo),
            )
        },
        actions = {
            IconButton(
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, end = 16.dp),
                onClick = { onClick() },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.notification),
                    contentDescription = stringResource(id = R.string.notification_icon),
                )
            }
        },
        title = { },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = WeSpotThemeManager.colors.backgroundColor,
            navigationIconContentColor = Gray400,
            actionIconContentColor = Gray400,
        ),
    )
}

@OrientationPreviews
@Composable
private fun WSTopBarPreview() {
    WeSpotTheme {
        Surface(modifier = Modifier.fillMaxWidth()) {
            Column {
                WSHomeTopAppBar(
                    onClick = { },
                )
            }
        }
    }
}
