package com.bff.wespot.auth.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bff.wespot.auth.R
import com.bff.wespot.auth.state.AuthAction
import com.bff.wespot.auth.viewmodel.AuthViewModel
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.button.WSOutlineButton
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.designsystem.util.OrientationPreviews
import com.bff.wespot.ui.WSBottomSheet
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradeScreen(viewModel: AuthViewModel = viewModel()) {
    val state by viewModel.collectAsState()
    val action = viewModel::onAction

    Scaffold(
        topBar = {
            WSTopBar(title = stringResource(id = R.string.register), canNavigateBack = true)
        },
    ) {
        Column(
            modifier = Modifier.padding(it),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(id = R.string.grade),
                style = StaticTypeScale.Default.header1,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Text(
                text = stringResource(id = R.string.cannot_change_grade_after_register),
                style = StaticTypeScale.Default.body6,
                color = Color(0xFF7A7A7A),
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        action(AuthAction.OnGradeBottomSheetChanged(true))
                    }
            ) {
                WSOutlineButton(
                    text = "",
                    onClick = {
                        action(AuthAction.OnGradeBottomSheetChanged(true))
                    }
                ) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)) {
                        Text(
                            text = if (state.grade == -1) {
                                stringResource(id = R.string.select_grade)
                            } else {
                                "${state.grade}${stringResource(id = R.string.grade)}"
                            },
                            style = StaticTypeScale.Default.body4,
                            color = if (state.grade == -1) {
                                WeSpotThemeManager.colors.disableBtnColor
                            } else {
                                WeSpotThemeManager.colors.txtTitleColor
                            }
                        )
                    }
                }
            }

            if (state.gradeBottomSheet) {
                WSBottomSheet(
                    closeSheet = { action(AuthAction.OnGradeBottomSheetChanged(false)) },
                    sheetState = rememberModalBottomSheetState(
                        skipPartiallyExpanded = true
                    )
                ) {
                    BottomSheetContent(currentGrade = state.grade, onGradeSelected = { grade ->
                        action(AuthAction.OnGradeChanged(grade))
                    })
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            WSButton(onClick = { }, text = stringResource(id = R.string.next)) {
                it.invoke()
            }
        }
    }
}

@Composable
private fun BottomSheetContent(
    currentGrade: Int,
    onGradeSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier.padding(vertical = 28.dp, horizontal = 32.dp)
    ) {
        Text(
            text = stringResource(id = R.string.select_grade),
            style = StaticTypeScale.Default.body1
        )
        Spacer(modifier = Modifier.padding(vertical = 4.dp))

        Text(
            text = stringResource(id = R.string.more_than_14_to_register),
            style = StaticTypeScale.Default.body6,
            color = Color(0xFF7A7A7A)
        )

        Spacer(modifier = Modifier.padding(vertical = 8.dp))

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(3) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .clickable { onGradeSelected(it + 1) }
                        .padding(vertical = 12.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "${it + 1}${stringResource(id = R.string.grade)}",
                        style = StaticTypeScale.Default.body3
                    )

                    Icon(
                        painter = painterResource(id = com.bff.wespot.ui.R.drawable.exclude),
                        contentDescription = stringResource(id = R.string.check_icon),
                        tint = if (it == currentGrade - 1) {
                            WeSpotThemeManager.colors.primaryColor
                        } else {
                            WeSpotThemeManager.colors.disableBtnColor
                        }
                    )
                }
            }
        }
    }
}


@OrientationPreviews
@Composable
private fun GradeScreenPreview() {
    WeSpotTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            GradeScreen()
        }
    }
}