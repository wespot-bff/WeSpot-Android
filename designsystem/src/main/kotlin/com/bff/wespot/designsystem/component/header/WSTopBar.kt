package com.bff.wespot.designsystem.component.header

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.bff.wespot.designsystem.R
import com.bff.wespot.designsystem.theme.Gray400
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.designsystem.util.OrientationPreviews


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WSTopBar(
    title: String,
    navigation: @Composable () -> Unit = {},
    canNavigateBack: Boolean = false,
    navigateUp: () -> Unit = {},
    action: @Composable RowScope.(textStyle: TextStyle) -> Unit = { },
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = StaticTypeScale.Default.header2,
                color = WeSpotThemeManager.colors.txtTitleColor
            )
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(
                    modifier = Modifier.padding(start = 4.dp),
                    onClick = { navigateUp.invoke() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.left_arrow),
                        contentDescription = stringResource(id = R.string.navigate_back)
                    )
                }
            } else {
                navigation()
            }
        },
        actions = {
            action(StaticTypeScale.Default.body4)
            Spacer(modifier = Modifier.padding(end = 12.dp))
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = WeSpotThemeManager.colors.backgroundColor,
            navigationIconContentColor = Gray400,
            actionIconContentColor = Gray400
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@OrientationPreviews
@Composable
private fun WSTopBarPreview() {
    WeSpotTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column {
                WSTopBar(title = stringResource(id = R.string.register))
                WSTopBar(title = stringResource(id = R.string.register), canNavigateBack = true)
                WSTopBar(title = stringResource(id = R.string.register), action = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(id = R.string.search)
                        )
                    }
                })
                WSTopBar(title = "", canNavigateBack = true, action = {
                    Text(
                        text = stringResource(id = R.string.to_home),
                        style = it,
                        color = WeSpotThemeManager.colors.txtTitleColor
                    )
                })
            }
        }
    }
}