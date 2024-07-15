package com.bff.wespot.auth.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bff.wespot.auth.R
import com.bff.wespot.auth.state.AuthAction
import com.bff.wespot.auth.state.NavigationAction
import com.bff.wespot.auth.viewmodel.AuthViewModel
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.button.WSButtonType
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.ui.WSBottomSheet
import com.ramcosta.composedestinations.annotation.Destination
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun EditScreen(
    viewModel: AuthViewModel,
) {
    val state by viewModel.collectAsState()
    val action = viewModel::onAction

    var firstEnter by remember {
        mutableStateOf(true)
    }
    var register by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            WSTopBar(
                title = stringResource(id = R.string.register),
                canNavigateBack = true,
                navigateUp = {
                    action(AuthAction.Navigation(NavigationAction.PopBackStack))
                },
            )
        },
    ) {
        Column(
            modifier = Modifier.padding(it),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            EditField(
                title = stringResource(id = R.string.name),
                value = state.name,
            ) {
                action(AuthAction.Navigation(NavigationAction.NavigateToNameScreen(true)))
            }

            EditField(
                title = stringResource(id = R.string.gender),
                value = if (state.gender == "male") {
                    stringResource(id = R.string.male_student)
                } else {
                    stringResource(id = R.string.female_student)
                },
            ) {
                action(AuthAction.Navigation(NavigationAction.NavigateToGenderScreen(true)))
            }

            EditField(
                title = stringResource(id = R.string.get_class),
                value = state.classNumber.toString(),
            ) {
                action(AuthAction.Navigation(NavigationAction.NavigateToClassScreen(true)))
            }

            EditField(
                title = stringResource(id = R.string.grade),
                value = "${state.grade}학년",
            ) {
                action(AuthAction.Navigation(NavigationAction.NavigateToGradeScreen(true)))
            }

            EditField(
                title = stringResource(id = R.string.school),
                value = state.selectedSchool?.name ?: "",
            ) {
                action(AuthAction.Navigation(NavigationAction.NavigateToSchoolScreen(true)))
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        WSButton(onClick = { }, text = stringResource(id = R.string.confirm)) {
            it.invoke()
        }
    }

    if (firstEnter) {
        WSBottomSheet(closeSheet = { firstEnter = false }) {
            ConfirmBottomSheetContent(
                name = state.name,
                gender = state.gender,
                classNumber = state.classNumber,
                grade = state.grade,
                school = state.selectedSchool?.name ?: "",
                closeSheet = {
                    firstEnter = false
                },
                moveSheet = {
                    register = true
                    firstEnter = false
                },
            )
        }
    }

    if (register) {
        WSBottomSheet(closeSheet = { register = true }) {
            RegisterBottomSheetContent {
                action(AuthAction.Navigation(NavigationAction.NavigateToCompleteScreen))
            }
        }
    }
}

@Composable
private fun EditField(
    title: String,
    value: String,
    onClicked: () -> Unit,
) {
    Column {
        Text(
            text = title,
            style = StaticTypeScale.Default.body4,
            modifier = Modifier.padding(horizontal = 30.dp),
        )

        WSButton(
            onClick = onClicked,
            buttonType = WSButtonType.Tertiary,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
            ) {
                Text(
                    text = value,
                    style = StaticTypeScale.Default.body4,
                )
            }
        }
    }
}

@Composable
private fun ConfirmBottomSheetContent(
    name: String,
    gender: String,
    classNumber: Int,
    grade: Int,
    school: String,
    closeSheet: () -> Unit,
    moveSheet: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 28.dp,
                end = 28.dp,
                top = 28.dp,
                bottom = 8.dp,
            ),
    ) {
        Text(
            text = stringResource(id = R.string.register_confirm),
            style = StaticTypeScale.Default.body1,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.register_confirm_detail),
            style = StaticTypeScale.Default.body6,
            color = WeSpotThemeManager.colors.disableBtnTxtColor,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 28.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Icon",
                modifier = Modifier.size(56.dp),
            )
            Column {
                Text(
                    text = "$name (${
                        if (gender == "male") {
                            stringResource(id = R.string.male_student)
                        } else {
                            stringResource(id = R.string.female_student)
                        }
                    })",
                    style = StaticTypeScale.Default.header1,
                )

                Text(
                    text = "$school ${grade}학년 ${classNumber}반",
                    style = StaticTypeScale.Default.body2,
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier = Modifier.weight(1f),
            ) {
                WSButton(
                    onClick = { closeSheet.invoke() },
                    text = stringResource(id = R.string.edit),
                    paddingValues = PaddingValues(0.dp),
                    buttonType = WSButtonType.Secondary,
                ) {
                    it.invoke()
                }
            }
            Box(
                modifier = Modifier.weight(1f),
            ) {
                WSButton(
                    onClick = { moveSheet.invoke() },
                    text = stringResource(id = R.string.register_complete),
                    paddingValues = PaddingValues(0.dp),
                    buttonType = WSButtonType.Primary,
                ) {
                    it.invoke()
                }
            }
        }
    }
}

@Composable
private fun RegisterBottomSheetContent(
    onClicked: () -> Unit,
) {
    var checked by remember {
        mutableStateOf(listOf(false, false, false, false))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp, top = 28.dp)
            .navigationBarsPadding(),
    ) {
        Text(
            text = stringResource(id = R.string.terms_title),
            style = StaticTypeScale.Default.body1,
            modifier = Modifier.padding(horizontal = 28.dp, vertical = 18.dp),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(WeSpotThemeManager.shapes.medium)
                .background(
                    WeSpotThemeManager.colors.cardBackgroundColor,
                    shape = WeSpotThemeManager.shapes.medium,
                ),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(18.dp),
            ) {
                Icon(
                    painter = painterResource(id = com.bff.wespot.ui.R.drawable.exclude),
                    contentDescription = stringResource(id = R.string.check_icon),
                    tint = if (checked[0]) {
                        WeSpotThemeManager.colors.primaryColor
                    } else {
                        WeSpotThemeManager.colors.disableIcnColor
                    },
                    modifier = Modifier.clickable {
                        if (checked[0]) {
                            checked = listOf(false, false, false, false)
                        } else {
                            checked = listOf(true, true, true, true)
                        }
                    },
                )

                Text(
                    text = stringResource(id = R.string.accept_all),
                    style = StaticTypeScale.Default.body4,
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(30.dp),
            modifier = Modifier.padding(top = 14.dp, bottom = 38.dp, start = 30.dp, end = 36.dp),
        ) {
            TermRow(
                title = stringResource(id = R.string.service_term),
                url = "",
                checked = checked[1],
                onClicked = {
                    checked = checked.toMutableList().apply {
                        if (this[1]) {
                            this[1] = false
                            this[0] = false
                        } else {
                            this[1] = true
                        }
                    }
                },
            )

            TermRow(
                title = stringResource(id = R.string.privacy_term),
                url = "",
                checked = checked[2],
                onClicked = {
                    checked = checked.toMutableList().apply {
                        if (this[2]) {
                            this[2] = false
                            this[0] = false
                        } else {
                            this[2] = true
                        }
                    }
                },
            )

            TermRow(
                title = stringResource(id = R.string.marketing_term),
                url = "",
                checked = checked[3],
                onClicked = {
                    checked = checked.toMutableList().apply {
                        if (this[3]) {
                            this[3] = false
                            this[0] = false
                        } else {
                            this[3] = true
                        }
                    }
                },
            )
        }

        WSButton(
            onClick = onClicked,
            text = stringResource(id = R.string.accept_and_start),
            enabled = checked.drop(1).take(2).all { it },
        ) {
            it.invoke()
        }
    }
}

@Composable
private fun TermRow(
    title: String,
    url: String,
    checked: Boolean,
    onClicked: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 22.dp),
    ) {
        Icon(
            painter = painterResource(id = com.bff.wespot.ui.R.drawable.exclude),
            contentDescription = stringResource(id = R.string.check_icon),
            tint = if (checked) {
                WeSpotThemeManager.colors.primaryColor
            } else {
                WeSpotThemeManager.colors.disableIcnColor
            },
            modifier = Modifier.clickable {
                onClicked.invoke()
            },
        )

        Text(text = title, style = StaticTypeScale.Default.body6)

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            Icon(
                painter = painterResource(id = com.bff.wespot.designsystem.R.drawable.right_arrow),
                contentDescription = "화살 아이콘",
                tint = WeSpotThemeManager.colors.disableIcnColor,
            )
        }
    }
}
