package com.bff.wespot.vote.screen

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bff.wespot.analytic.LocalAnalyticsHelper
import com.bff.wespot.analytic.TrackScreenViewEvent
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.indicator.WSToastType
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.model.vote.response.IndividualReceived
import com.bff.wespot.model.vote.response.Result
import com.bff.wespot.model.vote.response.VoteUser
import com.bff.wespot.navigation.Navigator
import com.bff.wespot.ui.CaptureBitmap
import com.bff.wespot.ui.NetworkDialog
import com.bff.wespot.ui.TopToast
import com.bff.wespot.ui.saveBitmap
import com.bff.wespot.vote.R
import com.bff.wespot.vote.ui.VoteCard
import com.bff.wespot.vote.viewmodel.IndividualViewModel
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

data class IndividualVoteArgs(
    val optionId: Int,
    val date: String,
    val isReceived: Boolean,
)

interface IndividualVoteNavigator {
    fun navigateUp()
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Destination(
    navArgsDelegate = IndividualVoteArgs::class,
)
@Composable
fun IndividualVoteScreen(
    voteNavigator: IndividualVoteNavigator,
    navigator: Navigator,
    viewModel: IndividualViewModel = hiltViewModel(),
) {
    val individual by viewModel.individual.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val analyticsHelper = LocalAnalyticsHelper.current

    val networkState by viewModel.networkState.collectAsStateWithLifecycle()

    var showToast by remember { mutableStateOf(false) }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val intent = it.data
            }
        }

    Scaffold(
        topBar = {
            WSTopBar(title = "", canNavigateBack = true, navigateUp = voteNavigator::navigateUp)
        },
    ) {
        val snapshot = CaptureBitmap {
            Image(
                painter = painterResource(id = R.drawable.vote_background),
                contentDescription = stringResource(
                    id = R.string.vote,
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 60.dp),
                contentScale = ContentScale.FillBounds,
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(top = 60.dp, start = 36.dp, end = 36.dp),
            ) {
                if (individual != null) {
                    val result = (individual as IndividualReceived).voteResult
                    val user = result.user
                    VoteCard(
                        result = Result(
                            user = VoteUser(
                                id = user.id,
                                name = user.name,
                                introduction = user.introduction,
                                profile = user.profile,
                            ),
                            voteCount = result.voteCount,
                        ),
                        question = result.voteOption.content,
                        page = -1,
                    )
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
                    onClick = {
                        showToast = true
                    },
                    paddingValues = PaddingValues(
                        end = 87.dp,
                    ),
                    text = stringResource(R.string.who_send),
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
                        MainScope().launch {
                            val bitmap = snapshot.invoke()
                            val uri = saveBitmap(context, bitmap)

                            if (uri != null) {
                                val intent = navigator.navigateToInstaStory(context, uri)
                                intent.let {
                                    launcher.launch(intent)
                                }
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

    TopToast(
        message = stringResource(R.string.next_update),
        toastType = WSToastType.Error,
        showToast = showToast,
    ) {
        showToast = false
    }

    NetworkDialog(context = context, networkState = networkState)

    TrackScreenViewEvent(screenName = "receive_vote_screen")
}
