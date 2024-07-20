package com.bff.wespot.vote.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bff.wespot.designsystem.component.banner.WSBanner
import com.bff.wespot.designsystem.component.banner.WSBannerType
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.indicator.WSHomeTabRow
import com.bff.wespot.designsystem.theme.Gray100
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.util.OnLifecycleEvent
import com.bff.wespot.vote.R
import com.bff.wespot.vote.state.VoteAction
import com.bff.wespot.vote.viewmodel.VoteHomeViewModel
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.persistentListOf

interface VoteNavigator {
    fun navigateUp()
    fun navigateToVotingScreen()
}

@Destination
@Composable
internal fun VoteHomeScreen(
    viewModel: VoteHomeViewModel = hiltViewModel(),
    voteNavigator: VoteNavigator,
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            VoteTopBar(
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it },
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
        ) {
            WSBanner(
                icon = painterResource(id = com.bff.wespot.ui.R.drawable.send),
                title = stringResource(id = R.string.invite_friend),
                subTitle = stringResource(id = R.string.invite_friend_description),
                bannerType = WSBannerType.Primary,
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(390.dp)
                    .padding(horizontal = 20.dp),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(1f),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.vote),
                        contentDescription = stringResource(
                            id = R.string.vote_description,
                        ),
                        modifier = Modifier.size(310.dp),
                    )
                }
                Card(
                    modifier = Modifier
                        .clip(WeSpotThemeManager.shapes.medium)
                        .zIndex(0f),
                    colors = CardDefaults.cardColors(
                        containerColor = WeSpotThemeManager.colors.cardBackgroundColor,
                        contentColor = Gray100,
                    ),
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    ClockTime(viewModel = viewModel)
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .zIndex(1f)
                            .padding(bottom = 24.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = stringResource(id = R.string.vote_ongoing),
                            style = StaticTypeScale.Default.body1,
                            modifier = Modifier.padding(horizontal = 28.dp),
                        )

                        WSButton(
                            onClick = voteNavigator::navigateToVotingScreen,
                            text = stringResource(id = R.string.voting)
                        ) {
                            it.invoke()
                        }
                    }
                }
            }
        }
    }

    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                viewModel.onAction(VoteAction.StartDate)
            }

            Lifecycle.Event.ON_STOP -> {
                viewModel.onAction(VoteAction.EndDate)
            }

            else -> {}
        }
    }
}

@Composable
private fun ClockTime(viewModel: VoteHomeViewModel) {
    val currentDate by viewModel.currentDate.collectAsStateWithLifecycle()

    Text(
        text = currentDate,
        style = StaticTypeScale.Default.body6,
        color = WeSpotThemeManager.colors.disableBtnTxtColor,
        modifier = Modifier.padding(horizontal = 28.dp),
    )
}

@Composable
private fun VoteTopBar(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
) {
    val tabList = persistentListOf(
        stringResource(R.string.vote_home_screen),
        stringResource(R.string.vote_result_screen),
    )

    WSHomeTabRow(
        selectedTabIndex = selectedTabIndex,
        tabList = tabList,
        onTabSelected = onTabSelected,
    )
}
