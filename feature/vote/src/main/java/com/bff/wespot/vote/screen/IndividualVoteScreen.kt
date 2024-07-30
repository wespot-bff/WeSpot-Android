package com.bff.wespot.vote.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.theme.Primary100
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.model.vote.response.IndividualReceived
import com.bff.wespot.model.vote.response.IndividualSent
import com.bff.wespot.model.vote.response.Result
import com.bff.wespot.model.vote.response.VoteProfile
import com.bff.wespot.ui.DotIndicators
import com.bff.wespot.ui.WSCarousel
import com.bff.wespot.vote.R
import com.bff.wespot.vote.ui.VoteCard
import com.bff.wespot.vote.viewmodel.IndividualViewModel
import com.ramcosta.composedestinations.annotation.Destination

data class IndividualVoteArgs(
    val optionId: Int,
    val date: String,
    val isReceived: Boolean
)

interface IndividualVoteNavigator {
    fun navigateUp()
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Destination(
    navArgsDelegate = IndividualVoteArgs::class
)
@Composable
fun IndividualVoteScreen(
    navigator: IndividualVoteNavigator,
    viewModel: IndividualViewModel = hiltViewModel()
) {
    val individual by viewModel.individual.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            WSTopBar(title = "", canNavigateBack = true, navigateUp = navigator::navigateUp)
        }
    ) {
        Image(
            painter = painterResource(id = R.drawable.vote_background),
            contentDescription = stringResource(
                id = R.string.vote
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp),
            contentScale = ContentScale.FillBounds
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(start = 38.dp, end = 38.dp, top = 60.dp)
        ) {
            when (individual) {
                is IndividualReceived -> {
                    val result = (individual as IndividualReceived).voteResult
                    val user = result.user
                    VoteCard(
                        result = Result(
                            user = VoteProfile(
                                id = user.id,
                                name = user.name,
                                introduction = user.introduction,
                                profile = user.profile
                            ),
                            voteCount = result.voteCount
                        ),
                        question = result.voteOption.content,
                        page = 1,
                    )
                }

                is IndividualSent -> {
                    val result = (individual as IndividualSent).voteResult

                    val pagerState = rememberPagerState {
                        result.voteUsers.size
                    }

                    Column {
                        WSCarousel(
                            pagerState = pagerState,
                            pageSpacing = 4.dp,
                            contentPadding = PaddingValues(horizontal = 46.dp),
                        ) { index ->
                            val user = result.voteUsers[index]
                            VoteCard(
                                result = Result(
                                    user = VoteProfile(
                                        id = user.id,
                                        name = user.name,
                                        introduction = "",
                                        profile = user.profile
                                    ),
                                    voteCount = 0
                                ),
                                question = result.voteOption.content,
                                pagerState = pagerState,
                                page = index,
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        DotIndicators(pagerState = pagerState)
                    }
                }
            }
        }



        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 12.dp, bottom = 12.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                WSButton(
                    onClick = { },
                    paddingValues = PaddingValues(
                        end = 87.dp,
                    ),
                    text = stringResource(id = R.string.tell_friend),
                ) {
                    it.invoke()
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 20.dp, top = 12.dp, bottom = 12.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Button(
                    onClick = { },
                    modifier = Modifier.size(52.dp),
                    shape = WeSpotThemeManager.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = WeSpotThemeManager.colors.secondaryBtnColor,
                        contentColor = Primary100,
                    ),
                    contentPadding = PaddingValues(1.dp),
                ) {
                    Icon(
                        painter = painterResource(id = com.bff.wespot.designsystem.R.drawable.insta),
                        contentDescription = "",
                    )
                }
            }
        }
    }
}