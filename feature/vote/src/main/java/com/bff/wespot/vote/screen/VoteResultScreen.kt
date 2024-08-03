package com.bff.wespot.vote.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bff.wespot.common.util.toDateString
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.button.WSTextButton
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.theme.Primary100
import com.bff.wespot.designsystem.theme.Primary300
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.model.vote.response.VoteResult
import com.bff.wespot.model.vote.response.VoteUser
import com.bff.wespot.navigation.Navigator
import com.bff.wespot.ui.DotIndicators
import com.bff.wespot.ui.MultiLineText
import com.bff.wespot.ui.WSCarousel
import com.bff.wespot.ui.WSHomeChipGroup
import com.bff.wespot.util.hexToColor
import com.bff.wespot.vote.R
import com.bff.wespot.vote.state.result.ResultAction
import com.bff.wespot.vote.ui.EmptyResultScreen
import com.bff.wespot.vote.viewmodel.VoteResultViewModel
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState
import java.time.LocalDate

interface VoteResultNavigator {
    fun navigateToVoteHome()
    fun navigateUp()
}

data class VoteResultScreenArgs(
    val isVoting: Boolean,
)

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Destination(
    navArgsDelegate = VoteResultScreenArgs::class,
)
@Composable
fun VoteResultScreen(
    voteNavigator: VoteResultNavigator,
    navigator: Navigator,
    viewModel: VoteResultViewModel = hiltViewModel(),
) {
    val state by viewModel.collectAsState()
    val action = viewModel::onAction
    val context = LocalContext.current

    val pagerState = rememberPagerState(pageCount = { state.voteResults.voteResults.size })

    var voteType by remember {
        mutableStateOf(TODAY)
    }

    if (state.onBoarding) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color(0x00000000).copy(alpha = 0.4f))
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()

                    val (x, y) = dragAmount
                    when {
                        x < 0 -> {
                            action(ResultAction.SetVoteOnBoarding)
                        }
                    }
                }
            }
            .zIndex(1f)
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (state.isVoting) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(end = 12.dp),
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    WSTextButton(
                        text = stringResource(id = R.string.go_to_home),
                        onClick = {
                            voteNavigator.navigateToVoteHome()
                        },
                    )
                }
            } else {
                WSTopBar(
                    title = "",
                    canNavigateBack = true,
                    navigateUp = { voteNavigator.navigateUp() },
                )
            }
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
        ) {
            if (!state.isVoting) {
                WSHomeChipGroup(
                    items = persistentListOf(
                        stringResource(id = R.string.past_vote),
                        stringResource(
                            id = R.string.real_time_vote,
                        ),
                    ),
                    selectedItemIndex = voteType,
                    onSelectedChanged = { voteType = it },
                )
            }

            WSCarousel(pagerState = pagerState) { page ->
                VoteResultItem(
                    result = state.voteResults.voteResults[page],
                    empty = state.voteResults.voteResults[page].results.size < MIN_REQUIREMENT,
                )
            }

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, top = 12.dp, bottom = 12.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    WSButton(
                        onClick = {
                            navigator.navigateToSharing(context)
                        },
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
                        onClick = {
                        },
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 104.dp),
            contentAlignment = Alignment.BottomCenter,
        ) {
            DotIndicators(pagerState = pagerState)
        }
    }

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

    LaunchedEffect(voteType) {
        val today = LocalDate.now()
        action(
            ResultAction.LoadVoteResults(
                today.minusDays(voteType.toLong()).toDateString(),
            ),
        )
    }

    LaunchedEffect(Unit) {
        if (state.isVoting) {
            action(ResultAction.GetOnBoarding)
        }
    }

    BackHandler {
        voteNavigator.navigateToVoteHome()
    }
}

@Composable
private fun VoteResultItem(
    result: VoteResult,
    empty: Boolean,
) {
    BoxWithConstraints {
        val height = maxHeight

        Column(modifier = Modifier.fillMaxWidth()) {
            MultiLineText(
                text = result.voteOption.content,
                style = StaticTypeScale.Default.header1,
                line = if (height < 600.dp) {
                    1
                } else {
                    2
                },
                modifier = Modifier.padding(horizontal = 24.dp),
            )
            if (empty) {
                EmptyResultScreen()
            } else {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    val results = result.results.take(3)
                    RankCard(user = results[1].user, vote = results[1].voteCount, rank = 2)
                    RankCard(user = results[0].user, vote = results[0].voteCount, rank = 1)
                    RankCard(user = results[2].user, vote = results[2].voteCount, rank = 3)
                }

                Spacer(modifier = Modifier.height(36.dp))
                (3..<5).forEach {
                    RankTile(
                        user = result.results[it].user,
                        vote = result.results[it].voteCount,
                        rank = it + 1,
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
private fun RankCard(
    user: VoteUser,
    vote: Int,
    rank: Int,
) {
    val widthPixels = LocalConfiguration.current.screenWidthDp.dp

    val cardWidth = remember {
        (widthPixels - 44.dp) / 3
    }

    val rankIcon = when (rank) {
        1 -> R.drawable.first
        2 -> R.drawable.second
        else -> R.drawable.third
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(
            top = if (rank == 1) {
                8.dp
            } else {
                47.dp
            },
        ),
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 33.dp)
                .zIndex(1f),
        ) {
            Image(
                painter = painterResource(id = rankIcon),
                contentDescription = "",
                modifier = Modifier
                    .size(40.dp)
                    .offset(
                        x = 0.dp,
                        y = if (rank == 1) {
                            14.dp
                        } else {
                            11.dp
                        },
                    ),
            )
        }

        Card(
            modifier = Modifier
                .clip(WeSpotThemeManager.shapes.medium)
                .width(cardWidth)
                .zIndex(0f),
            colors = CardDefaults.cardColors(
                containerColor = WeSpotThemeManager.colors.cardBackgroundColor,
                contentColor = WeSpotThemeManager.colors.abledTxtColor,
            ),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .wrapContentWidth(),
            ) {
                Text(
                    text = "$vote${stringResource(id = R.string.ballot)}",
                    style = StaticTypeScale.Default.body1,
                    maxLines = 1,
                    color = if (rank == 1) {
                        Primary300
                    } else {
                        WeSpotThemeManager.colors.abledTxtColor
                    },
                )
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(user.profile.iconUrl)
                        .build(),
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(hexToColor(user.profile.backgroundColor)),
                    contentDescription = stringResource(id = R.string.ballot),
                )
                Text(text = user.name, style = StaticTypeScale.Default.body2, maxLines = 1)
                Text(
                    text = user.introduction,
                    style = StaticTypeScale.Default.badge,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Clip,
                )
            }
        }
    }
}

@Composable
private fun RankTile(
    user: VoteUser,
    vote: Int,
    rank: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
    ) {
        Text(text = "$rank", style = StaticTypeScale.Default.body1)

        Spacer(modifier = Modifier.width(18.dp))
        AsyncImage(
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(user.profile.iconUrl)
                .build(),
            contentDescription = stringResource(id = R.string.ballot),
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(hexToColor(user.profile.backgroundColor)),
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(text = user.name, style = StaticTypeScale.Default.body2)
            Text(text = user.introduction, style = StaticTypeScale.Default.body9)
        }

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            Text(
                text = "$vote${stringResource(id = R.string.ballot)}",
                style = StaticTypeScale.Default.body2,
            )
        }
    }
}

private const val MIN_REQUIREMENT = 5
private const val TODAY = 0
private const val YESTERDAY = 1
