package com.bff.wespot.auth.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.bff.wespot.analytic.TrackScreenViewEvent
import com.bff.wespot.auth.R
import com.bff.wespot.auth.state.AuthAction
import com.bff.wespot.auth.state.NavigationAction
import com.bff.wespot.auth.viewmodel.AuthViewModel
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.button.WSTextButton
import com.bff.wespot.designsystem.component.button.WSTextButtonType
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.input.WsTextField
import com.bff.wespot.designsystem.component.input.WsTextFieldType
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.navigation.Navigator
import com.bff.wespot.ui.ListBottomGradient
import com.bff.wespot.ui.WSListItem
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun SchoolScreen(
    edit: Boolean,
    viewModel: AuthViewModel,
    navigator: Navigator,
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    val state by viewModel.collectAsState()
    val action = viewModel::onAction
    val context = LocalContext.current

    val pagingData = state.schoolList.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            WSTopBar(
                title = stringResource(id = R.string.register),
                canNavigateBack = edit,
                navigateUp = {
                    action(AuthAction.Navigation(NavigationAction.PopBackStack))
                },
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 24.dp),
        ) {
            Text(
                stringResource(id = R.string.search_school),
                style = StaticTypeScale.Default.header1,
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                stringResource(id = R.string.search_base_on_your_school),
                style = StaticTypeScale.Default.body8,
                color = Color(0xFF7A7A7A),
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
                singleLine = true,
            )

            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            if (state.schoolName.length >= 20) {
                Text(
                    stringResource(id = R.string.within_20_characters),
                    style = StaticTypeScale.Default.body8,
                    color = WeSpotThemeManager.colors.dangerColor,
                )
            }

            if (pagingData.itemCount == 0) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    WSTextButton(
                        text = stringResource(id = R.string.no_school_found),
                        onClick = {
                            navigator.navigateToWebLink(
                                context = context,
                                webLink = state.schoolForm,
                            )
                        },
                        buttonType = WSTextButtonType.Underline,
                    )
                }
            }

            when (pagingData.loadState.refresh) {
                is LoadState.Error -> {
                    // TODO: Handle error
                }

                else -> {
                    LazyColumn {
                        items(
                            pagingData.itemCount,
                            key = pagingData.itemKey { it.id },
                        ) { index ->
                            val school = pagingData[index]

                            school?.let { school ->
                                WSListItem(
                                    title = school.name,
                                    subTitle = school.address,
                                    selected = state.selectedSchool?.name == school.name,
                                    imageContent = {
                                        Image(
                                            painter = painterResource(
                                                id = if (school.type == "HIGH") {
                                                    com.bff.wespot.designsystem.R.drawable.hight_school
                                                } else {
                                                    com.bff.wespot.designsystem.R.drawable.middle_school
                                                },
                                            ),
                                            contentDescription = stringResource(
                                                id = com.bff.wespot.ui.R.string.school_icon,
                                            ),
                                            modifier = Modifier.size(56.dp),
                                        )
                                    },
                                    onClick = {
                                        action(AuthAction.OnSchoolSelected(school))
                                    },
                                )
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(70.dp))
                        }
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .zIndex(2f),
        contentAlignment = Alignment.BottomCenter,
    ) {
        WSButton(
            onClick = {
                if (edit) {
                    action(AuthAction.Navigation(NavigationAction.PopBackStack))
                    return@WSButton
                }
                action(AuthAction.Navigation(NavigationAction.NavigateToGradeScreen(false)))
            },
            enabled = state.selectedSchool != null,
            text = stringResource(
                id = if (edit) {
                    R.string.edit_complete
                } else {
                    R.string.next
                },
            ),
        ) {
            it()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f),
        contentAlignment = Alignment.BottomCenter,
    ) {
        ListBottomGradient(height = 120)
    }

    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
        delay(10)
        keyboard?.show()
    }

    LaunchedEffect(Unit) {
        action(AuthAction.OnStartSchoolScreen)
    }

    TrackScreenViewEvent(screenName = "school_screen", id = state.uuid)
}
