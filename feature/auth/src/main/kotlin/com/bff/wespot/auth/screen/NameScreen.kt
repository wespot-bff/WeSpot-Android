package com.bff.wespot.auth.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bff.wespot.auth.R
import com.bff.wespot.auth.screen.destinations.EditScreenDestination
import com.bff.wespot.auth.state.AuthAction
import com.bff.wespot.auth.viewmodel.AuthViewModel
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.input.WsTextField
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun NameScreen(
    viewModel: AuthViewModel,
    edit: Boolean,
    navigator: DestinationsNavigator,
) {
    val keyboard = LocalSoftwareKeyboardController.current

    val state by viewModel.collectAsState()
    val action = viewModel::onAction

    val focusRequester = remember {
        FocusRequester()
    }

    var error by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            WSTopBar(
                title = stringResource(id = R.string.register),
                canNavigateBack = true,
                navigateUp = {
                    navigator.navigateUp()
                },
            )
        },
    ) {
        Column(
            modifier = Modifier.padding(it).padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(id = R.string.name),
                style = StaticTypeScale.Default.header1,
            )

            Text(
                text = stringResource(id = R.string.cannot_change_name_after_register),
                style = StaticTypeScale.Default.body6,
                color = Color(0xFF7A7A7A),
            )

            WsTextField(
                value = state.name,
                onValueChange = { name ->
                    if (name.length > 5) {
                        error = true
                        return@WsTextField
                    }
                    error = false
                    action(AuthAction.OnNameChanged(name))
                },
                placeholder = stringResource(id = R.string.enter_name),
                focusRequester = focusRequester,
            )

            if (error) {
                Text(
                    text = stringResource(id = R.string.name_error),
                    color = WeSpotThemeManager.colors.dangerColor,
                    style = StaticTypeScale.Default.body6,
                )
            }

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                Text(
                    text = "${state.name.length} / 5",
                    color = Color(0xFF7A7A7A),
                    style = StaticTypeScale.Default.body7,
                )
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        WSButton(
            onClick = {
                if (edit) {
                    navigator.popBackStack()
                    return@WSButton
                }
                navigator.navigate(EditScreenDestination)
            },
            text = stringResource(
                id = if (edit) {
                    R.string.edit_complete
                } else {
                    R.string.next
                },
            ),
            enabled = state.name.length in 2..5,
        ) {
            it.invoke()
        }
    }

    LaunchedEffect(key1 = focusRequester) {
        focusRequester.requestFocus()
        delay(10)
        keyboard?.show()
    }
}
