package com.bff.wespot.auth.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bff.wespot.auth.R
import com.bff.wespot.auth.screen.destinations.NameScreenDestination
import com.bff.wespot.auth.state.AuthAction
import com.bff.wespot.auth.viewmodel.AuthViewModel
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun GenderScreen(
    viewModel: AuthViewModel,
    navigator: DestinationsNavigator
) {
    val state by viewModel.collectAsState()
    val action = viewModel::onAction

    Scaffold(
        topBar = {
            WSTopBar(title = stringResource(id = R.string.register), canNavigateBack = true)
        },
        modifier = Modifier.padding(horizontal = 20.dp),
    ) {
        Column(
            modifier = Modifier.padding(it),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(id = R.string.gender),
                style = StaticTypeScale.Default.header1,
            )
            Text(
                text = stringResource(id = R.string.cannot_change_gender_after_register),
                style = StaticTypeScale.Default.body6,
                color = Color(0xFF7A7A7A),
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                GenderBox(
                    title = stringResource(id = R.string.male_student),
                    icon =
                    painterResource(
                        id = com.bff.wespot.ui.R.drawable.male_student,
                    ),
                    selected = "male" == state.gender,
                    onClicked = {
                        action(AuthAction.OnGenderChanged("male"))
                        navigator.navigate(NameScreenDestination)
                    },
                )
                GenderBox(
                    title = stringResource(id = R.string.female_student),
                    icon =
                    painterResource(
                        id = com.bff.wespot.ui.R.drawable.female_student,
                    ),
                    selected = "female" == state.gender,
                    onClicked = {
                        action(AuthAction.OnGenderChanged("female"))
                        navigator.navigate(NameScreenDestination)
                    },
                )
            }
        }
    }
}

@Composable
private fun RowScope.GenderBox(
    title: String,
    icon: Painter,
    selected: Boolean = false,
    onClicked: () -> Unit,
) {
    Box(
        modifier =
        Modifier
            .weight(1f)
            .clip(WeSpotThemeManager.shapes.medium)
            .border(
                width = 1.dp,
                color = if (selected) WeSpotThemeManager.colors.primaryColor else Color.Transparent,
                shape = WeSpotThemeManager.shapes.medium,
            )
            .clickable { onClicked() }
            .background(WeSpotThemeManager.colors.cardBackgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(start = 30.dp, end = 30.dp, top = 30.dp, bottom = 17.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = icon,
                contentDescription = stringResource(id = R.string.gender_icon),
                modifier = Modifier.size(90.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = title, style = StaticTypeScale.Default.header2)
        }
    }
}
