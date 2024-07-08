package com.bff.wespot.auth.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bff.wespot.auth.R
import com.bff.wespot.auth.state.AuthAction
import com.bff.wespot.auth.viewmodel.AuthViewModel
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.button.WSTextButton
import com.bff.wespot.designsystem.component.button.WSTextButtonType
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.input.WsTextField
import com.bff.wespot.designsystem.component.input.WsTextFieldType
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.designsystem.util.OrientationPreviews
import com.bff.wespot.ui.SchoolListItem
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolScreen(viewModel: AuthViewModel = viewModel()) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    val state by viewModel.collectAsState()
    val action = viewModel::onAction

    Scaffold(
        topBar = {
            WSTopBar(title = stringResource(id = R.string.register))
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 24.dp)
        ) {
            Text(
                stringResource(id = R.string.search_school),
                style = StaticTypeScale.Default.header1
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                stringResource(id = R.string.search_base_on_your_school),
                style = StaticTypeScale.Default.body8,
                color = Color(0xFF7A7A7A)
            )
            Spacer(modifier = Modifier.padding(12.dp))

            WsTextField(
                value = state.schoolName,
                onValueChange = {
                    action(AuthAction.OnSchoolSearchChanged(it))
                },
                placeholder = stringResource(id = R.string.search_with_school_name),
                textFieldType = WsTextFieldType.Search,
                focusRequester = focusRequester,
                singleLine = true
            )

            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            if (state.schoolName.length >= 20) {
                Text(
                    stringResource(id = R.string.within_20_characters),
                    style = StaticTypeScale.Default.body8,
                    color = WeSpotThemeManager.colors.dangerColor
                )
            }

            if (state.schoolSearchList.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    WSTextButton(
                        text = stringResource(id = R.string.no_school_found),
                        onClick = { },
                        buttonType = WSTextButtonType.Underline
                    )
                }
            }

            LazyColumn() {
                items(state.schoolSearchList, key = { school ->
                    school.id
                }) { school ->
                    SchoolListItem(
                        schoolName = school.name,
                        address = school.address,
                        selected = state.selectedSchool?.name == school.name
                    ) {
                        action(AuthAction.OnSchoolSelected(school))
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        contentAlignment = Alignment.BottomCenter
    ) {
        WSButton(onClick = { }, enabled = false, text = stringResource(id = R.string.next)) {
            it()
        }
    }

    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
        delay(10)
        keyboard?.show()
    }
}

@OrientationPreviews
@Composable
private fun SchoolScreenPreview() {
    WeSpotTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SchoolScreen()
        }
    }
}