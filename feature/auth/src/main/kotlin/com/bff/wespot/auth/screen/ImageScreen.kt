package com.bff.wespot.auth.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bff.wespot.auth.R
import com.bff.wespot.auth.state.AuthAction
import com.bff.wespot.auth.state.NavigationAction
import com.bff.wespot.auth.viewmodel.AuthViewModel
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.input.WsTextField
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.ui.util.clickableSingle
import com.ramcosta.composedestinations.annotation.Destination
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
internal fun ImageScreen(
    viewModel: AuthViewModel,
) {
    val action = viewModel::onAction
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current
    var error by remember {
        mutableStateOf(false)
    }

    val pickImage =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
            it?.let {
                action(AuthAction.ChangeImage(it.toString()))
            }
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
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.set_image, uiState.name),
                style = StaticTypeScale.Default.header1,
                modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 36.dp),
            )

            Box(
                modifier = Modifier.align(Alignment.CenterHorizontally),
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(uiState.imagePath)
                        .error(R.drawable.default_character)
                        .placeholder(R.drawable.default_character)
                        .build(),
                    contentDescription = stringResource(R.string.profile_image),
                    modifier = Modifier
                        .clickableSingle {
                            pickImage.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.SingleMimeType(
                                        "image/*",
                                    ),
                                ),
                            )
                        }
                        .clip(CircleShape)
                        .size(110.dp),
                    contentScale = ContentScale.Crop,
                )

                Image(
                    painter = painterResource(id = R.drawable.gallery),
                    contentDescription = "",
                    modifier = Modifier
                        .size(28.dp)
                        .align(Alignment.BottomEnd),
                )
            }

            Text(
                text = stringResource(id = com.bff.wespot.ui.R.string.introduction),
                style = StaticTypeScale.Default.body4,
                modifier = Modifier.padding(
                    top = 22.dp,
                    bottom = 12.dp,
                    start = 10.dp,
                    end = 10.dp
                ),
            )

            WsTextField(
                value = uiState.introduction,
                onValueChange = { introduction ->
                    if (introduction.length > 20) {
                        error = true
                        return@WsTextField
                    }

                    error = false
                    action(AuthAction.ChangeIntroduction(introduction))
                },
                placeholder = stringResource(
                    id = R.string.introduction_placeholder,
                ),
                singleLine = true,
            )

            if (uiState.hasProfanity) {
                Text(
                    text = stringResource(id = com.bff.wespot.designsystem.R.string.has_profanity),
                    color = WeSpotThemeManager.colors.dangerColor,
                    style = StaticTypeScale.Default.body6,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 4.dp, end = 10.dp),
                )
            } else {
                Text(
                    text = stringResource(R.string.line_limit_20, uiState.introduction.length),
                    style = StaticTypeScale.Default.body7,
                    color = if (error) {
                        WeSpotThemeManager.colors.dangerColor
                    } else {
                        WeSpotThemeManager.colors.disableIcnColor
                    },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 4.dp, end = 10.dp),
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        WSButton(
            enabled = error.not() && uiState.hasProfanity.not(),
            onClick = {
                action(AuthAction.UploadImage)
            },
            text = stringResource(id = R.string.next),
        ) {
            it.invoke()
        }
    }

    LaunchedEffect(Unit) {
        action(AuthAction.OnStartImageScreen)
    }
}
