package com.bff.wespot.vote.screen

import android.app.Activity
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bff.wespot.common.util.toDateString
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.button.WSTextButton
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.theme.Primary300
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.model.user.response.ProfileCharacter
import com.bff.wespot.model.vote.response.VoteResult
import com.bff.wespot.model.vote.response.VoteUser
import com.bff.wespot.navigation.Navigator
import com.bff.wespot.ui.CaptureBitmap
import com.bff.wespot.ui.DotIndicators
import com.bff.wespot.ui.MultiLineText
import com.bff.wespot.ui.WSCarousel
import com.bff.wespot.ui.WSHomeChipGroup
import com.bff.wespot.ui.saveImage
import com.bff.wespot.util.hexToColor
import com.bff.wespot.vote.R
import com.bff.wespot.vote.state.result.ResultAction
import com.bff.wespot.vote.ui.EmptyResultScreen
import com.bff.wespot.vote.viewmodel.VoteResultViewModel
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

    var showLottie by remember {
        mutableStateOf(false)
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val intent = it.data
            }
        }

    if (state.onBoarding && !showLottie) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()

                        val (x, _) = dragAmount
                        when {
                            x < 0 -> {
                                action(ResultAction.SetVoteOnBoarding)
                            }
                        }
                    }
                }
                .zIndex(1f)
                .padding(top = 180.dp),
        ) {
            val composition by rememberLottieComposition(
                spec = LottieCompositionSpec.RawRes(
                    R.raw.vote_swipe,
                ),
            )

            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
            )
        }
    }

    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S && state.onBoarding) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x00000000).copy(alpha = 0.6f))
                .zIndex(1f),
        )
    }

    if (showLottie) {
        val composition by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(
                R.raw.vote_find,
            ),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            MultiLineText(
                text = stringResource(R.string.analysis_result),
                style = StaticTypeScale.Default.header1,
                line = 2,
                modifier = Modifier.padding(horizontal = 30.dp),
            )

            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
            )
        }

        LaunchedEffect(state.isLoading) {
            if (!state.isLoading) {
                delay(2000)
                showLottie = false
            }
        }
    } else {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .blur(if (state.onBoarding) 10.dp else 0.dp),
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

                val snapshot = CaptureBitmap {
                    WSCarousel(pagerState = pagerState) { page ->
                        VoteResultItem(
                            result = state.voteResults.voteResults[page],
                            empty = state.voteResults.voteResults[page].results.isEmpty(),
                        )
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
                            enabled = state.isLoading.not(),
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
                            enabled = state.isLoading.not(),
                            onClick = {
                                MainScope().launch {
                                    val bitmap = snapshot.invoke()
                                    val uri = saveImage(bitmap, context)

                                    if (uri != null) {
                                        val intent = navigator.navigateToInstaStory(context, uri)
                                        launcher.launch(intent)
                                    }
                                }
                            },
                            modifier = Modifier.size(52.dp),
                            shape = WeSpotThemeManager.shapes.medium,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = WeSpotThemeManager.colors.secondaryBtnColor,
                                contentColor = Color(0xFFEAEBEC),
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 104.dp)
                .blur(
                    if (state.onBoarding) 10.dp else 0.dp,
                    edgeTreatment = BlurredEdgeTreatment.Unbounded,
                ),
            contentAlignment = Alignment.BottomCenter,
        ) {
            DotIndicators(pagerState = pagerState)
        }
    }

    if (state.isLoading) {
        showLottie = true
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

                    if (results.getOrNull(1) == null) {
                        EmptyCard(2)
                    } else {
                        RankCard(user = results[1].user, vote = results[1].voteCount, rank = 2)
                    }

                    if (results.getOrNull(0) == null) {
                        EmptyCard(1)
                    } else {
                        RankCard(user = results[0].user, vote = results[0].voteCount, rank = 1)
                    }

                    if (results.getOrNull(2) == null) {
                        EmptyCard(3)
                    } else {
                        RankCard(user = results[2].user, vote = results[2].voteCount, rank = 3)
                    }
                }

                Spacer(modifier = Modifier.height(36.dp))
                (3..<5).forEach {
                    if (result.results.getOrNull(it) == null) {
                        EmptyTile(rank = it)
                    } else {
                        RankTile(
                            user = result.results[it].user,
                            vote = result.results[it].voteCount,
                            rank = it + 1,
                        )
                        if (result.results.getOrNull(4) != null) {
                            Spacer(modifier = Modifier.height(32.dp))
                        } else {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
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
                    .fillMaxWidth(),
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
                MultiLineText(
                    text = user.introduction,
                    style = StaticTypeScale.Default.badge,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    textAlign = TextAlign.Center,
                    line = 2,
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

@Composable
private fun EmptyCard(rank: Int) {
    val widthPixels = LocalConfiguration.current.screenWidthDp.dp
    val version = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    val user = VoteUser(
        id = 0,
        name = stringResource(id = nameList.random()),
        introduction = stringResource(id = textList.random()),
        profile = ProfileCharacter(
            iconUrl = "",
            backgroundColor = "#000000",
        ),
    )

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
        modifier = Modifier
            .padding(
                top = if (rank == 1) {
                    8.dp
                } else {
                    47.dp
                },
            )
            .blur(if (version) 10.dp else 0.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded),
    ) {
        if (version) {
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
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = "${(5..20).random()}${stringResource(id = R.string.ballot)}",
                        style = StaticTypeScale.Default.body1,
                        maxLines = 1,
                        color = if (rank == 1) {
                            Primary300
                        } else {
                            WeSpotThemeManager.colors.abledTxtColor
                        },
                    )
                    Image(
                        painter = painterResource(id = logoList.random()),
                        contentDescription = stringResource(id = R.string.ballot),
                        modifier = Modifier.size(48.dp),
                    )
                    Text(text = user.name, style = StaticTypeScale.Default.body2, maxLines = 1)
                    MultiLineText(
                        text = user.introduction,
                        style = StaticTypeScale.Default.badge,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        textAlign = TextAlign.Center,
                        line = 2,
                    )
                }
            }
        } else {
            if (rank == 2) {
                Image(
                    painter = painterResource(id = R.drawable.blur_2),
                    contentDescription = "",
                    modifier = Modifier.width(cardWidth),
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.blur_3),
                    contentDescription = "",
                    modifier = Modifier.width(cardWidth),
                )
            }
        }
    }
}

@Composable
private fun EmptyTile(
    rank: Int,
) {
    val user = VoteUser(
        id = 0,
        name = stringResource(id = nameList.random()),
        introduction = stringResource(id = textList.random()),
        profile = ProfileCharacter(
            iconUrl = "",
            backgroundColor = "#000000",
        ),
    )

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 16.dp)
                .blur(10.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded),
        ) {
            Text(text = "$rank", style = StaticTypeScale.Default.body1)

            Spacer(modifier = Modifier.width(18.dp))
            Image(
                painter = painterResource(id = logoList.random()),
                contentDescription = stringResource(id = R.string.ballot),
                modifier = Modifier.size(48.dp),
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(text = user.name, style = StaticTypeScale.Default.body2)
                Text(text = user.introduction, style = StaticTypeScale.Default.body9)
            }

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Text(
                    text = "${(1..10).random()}${stringResource(id = R.string.ballot)}",
                    style = StaticTypeScale.Default.body2,
                )
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxWidth()) {
            if (rank == 3) {
                Image(
                    painter = painterResource(id = R.drawable.blur_4),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    contentScale = ContentScale.FillWidth,
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.blur_5),
                    contentDescription = "",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth,
                )
            }
        }
    }
}

private val logoList = listOf(
    R.drawable.logo1,
    R.drawable.logo2,
    R.drawable.logo3,
)

private val textList = listOf(
    R.string.invite_friend_description,
    R.string.analyzing_result,
    R.string.first_icon,
)

private val nameList = listOf(
    R.string.vote,
    R.string.report,
)

private const val MIN_REQUIREMENT = 5
private const val TODAY = 0
private const val YESTERDAY = 1
