package com.bff.wespot.vote.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.button.WSButtonType
import com.bff.wespot.designsystem.theme.Gray300
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.designsystem.util.OrientationPreviews
import com.bff.wespot.model.user.response.ProfileCharacter
import com.bff.wespot.model.vote.response.Result
import com.bff.wespot.model.vote.response.VoteUser
import com.bff.wespot.ui.MultiLineText
import com.bff.wespot.util.carouselTransition
import com.bff.wespot.vote.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun VoteCard(
    result: Result,
    question: String,
    pagerState: PagerState = rememberPagerState { 0 },
    page: Int,
    onClick: (() -> Unit)? = null,
) {
    val height = LocalConfiguration.current.screenHeightDp.dp
    val ratio = remember {
        height / NORMAL_SCREEN_HEIGHT.dp
    }

    val modifier = if (page == -1) {
        Modifier
            .clip(WeSpotThemeManager.shapes.medium)
    } else {
        Modifier
            .clip(WeSpotThemeManager.shapes.medium)
            .carouselTransition(pagerState, page)
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.08f),
        ),
    ) {
        Column(
            modifier = Modifier.padding(
                start = 24.dp,
                end = 24.dp,
                top = 24.dp,
            ),
        ) {
            Box(
                modifier = Modifier
                    .border(1.dp, Gray300, RoundedCornerShape(30.dp)),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp * ratio),
                    horizontalArrangement = Arrangement.spacedBy(8.dp * ratio),
                ) {
                    if (result.voteCount != 0) {
                        Image(
                            painter = painterResource(id = R.drawable.first_empty),
                            contentDescription = stringResource(R.string.first_icon),
                            modifier = Modifier.size(30.dp * ratio),
                        )
                    }

                    Text(
                        text = if (result.voteCount == 0) {
                            "??${stringResource(id = R.string.ballot)}"
                        } else {
                            "${result.voteCount}${stringResource(id = R.string.ballot)}"
                        },
                        style = StaticTypeScale.Default.header1.copy(fontSize = 20.sp * ratio),
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp * ratio))

            MultiLineText(
                text = question,
                style = StaticTypeScale.Default.header2.copy(fontSize = 18.sp * ratio),
                line = if (ratio > 0.9f) {
                    2
                } else {
                    1
                },
                modifier = Modifier.width(208.dp),
            )

            Spacer(modifier = Modifier.height(18.dp * ratio))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (result.voteCount == 0) {
                    Image(
                        painter = painterResource(
                            id = com.bff.wespot.designsystem.R.drawable.question,
                        ),
                        contentDescription = stringResource(
                            id = R.string.vote_ongoing,
                        ),
                        modifier = Modifier.size(100.dp * ratio),
                    )
                } else {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(result.user.profile.iconUrl)
                            .build(),
                        contentDescription = stringResource(R.string.user_icon),
                        modifier = Modifier.size(100.dp * ratio),
                    )
                }

                Spacer(modifier = Modifier.height(18.dp * ratio))

                Text(
                    text = result.user.name,
                    style = StaticTypeScale.Default.header1.copy(fontSize = 20.sp * ratio),
                    color = WeSpotThemeManager.colors.primaryColor,
                )

                Spacer(modifier = Modifier.height(4.dp * ratio))

                Text(
                    text = result.user.introduction,
                    style = StaticTypeScale.Default.body3.copy(fontSize = 16.sp * ratio),
                    maxLines = 1,
                )

                Spacer(modifier = Modifier.height(12.dp * ratio))
            }
            if (result.voteCount == 0) {
                WSButton(
                    onClick = {},
                    buttonType = WSButtonType.Secondary,
                    paddingValues = PaddingValues(horizontal = 60.dp, vertical = 24.dp * ratio),
                ) {
                    Text(
                        text = stringResource(R.string.ask_friend),
                        style = StaticTypeScale.Default.body3.copy(fontSize = 16.sp * ratio),
                        maxLines = 1,
                    )
                }
            } else if (onClick != null) {
                WSButton(
                    onClick = onClick,
                    buttonType = WSButtonType.Secondary,
                    paddingValues = PaddingValues(horizontal = 60.dp, vertical = 24.dp * ratio),
                ) {
                    Text(
                        text = stringResource(R.string.check_overall_result),
                        style = StaticTypeScale.Default.body3.copy(fontSize = 16.sp * ratio),
                        maxLines = 1,
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp * ratio),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(id = com.bff.wespot.designsystem.R.drawable.white_logo),
                        contentDescription = stringResource(R.string.white_logo),
                        modifier = Modifier
                            .width(90.dp * ratio)
                            .aspectRatio(2.64f),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@OrientationPreviews
@Composable
private fun PreviewVoteCard() {
    WeSpotTheme {
        Surface {
            VoteCard(
                result = Result(
                    voteCount = 10,
                    user = VoteUser(
                        id = 1,
                        name = "Name",
                        introduction = "Introduction",
                        profile = ProfileCharacter(iconUrl = "", backgroundColor = "#FFFFFF"),
                    ),
                ),
                question = "Question",
                pagerState = rememberPagerState(pageCount = { 10 }),
                1,
                {},
            )
        }
    }
}

private const val NORMAL_SCREEN_HEIGHT = 800
