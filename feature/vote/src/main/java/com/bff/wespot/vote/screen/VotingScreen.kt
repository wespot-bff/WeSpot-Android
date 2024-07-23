package com.bff.wespot.vote.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.vote.R
import com.bff.wespot.vote.viewmodel.VotingViewModel
import com.ramcosta.composedestinations.annotation.Destination

interface VotingNavigator {
    fun navigateUp()
    fun navigateToVotingScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun VotingScreen(
    votingNavigator: VotingNavigator,
    viewModel: VotingViewModel = hiltViewModel(),
) {
    Scaffold(
        topBar = {
            WSTopBar(
                title = "1/5",
                action = {
                    Text(text = stringResource(id = R.string.report), style = it)
                },
                canNavigateBack = true,
                navigateUp = votingNavigator::navigateUp,
            )
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 20.dp),
        ) {
            Text(text = "박주현님은 반에서 어떤 친구인가요?", style = StaticTypeScale.Default.header1)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, bottom = 24.dp),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = com.bff.wespot.ui.R.drawable.male_student),
                    contentDescription = "male",
                    modifier = Modifier.size(120.dp),
                )
            }

            repeat(5) {
                WSButton(onClick = { }, text = "투표 1") {
                    it.invoke()
                }
            }
        }
    }
}
