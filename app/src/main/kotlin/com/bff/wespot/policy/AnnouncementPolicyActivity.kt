package com.bff.wespot.policy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.bff.wespot.R
import com.bff.wespot.designsystem.component.button.WSButton
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.component.modal.WSDialog
import com.bff.wespot.designsystem.theme.StaticTypeScale
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.designsystem.theme.WeSpotThemeManager
import com.bff.wespot.domain.util.RemoteConfigKey
import com.bff.wespot.navigation.Navigator
import com.bff.wespot.navigation.util.EXTRA_TOAST_MESSAGE
import com.bff.wespot.policy.state.AnnouncementPolicyAction
import com.bff.wespot.policy.state.AnnouncementPolicySideEffect
import com.bff.wespot.policy.viewmodel.AnnouncementPolicyViewModel
import com.bff.wespot.ui.component.WSBottomSheet
import com.bff.wespot.ui.util.clickableSingle
import dagger.hilt.android.AndroidEntryPoint
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import javax.inject.Inject

@AndroidEntryPoint
class AnnouncementPolicyActivity : ComponentActivity() {
    @Inject
    lateinit var navigator: Navigator

    private val viewModel: AnnouncementPolicyViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by viewModel.collectAsState()
            val action = viewModel::onAction

            WeSpotTheme {
                Scaffold(
                    topBar = {
                        AnnouncementTopBar(
                            onThreeDotClick = { action(AnnouncementPolicyAction.OnThreeDotClicked) },
                        )
                    },
                    bottomBar = {
                        WSButton(
                            text = stringResource(R.string.agree),
                            onClick = {
                                action(AnnouncementPolicyAction.OnAgreeClicked)
                            },
                        ) {
                            it()
                        }
                    },
                    modifier = Modifier.padding(bottom = 16.dp),
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .padding(it)
                                .padding(horizontal = 20.dp)
                                .verticalScroll(state = rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            Text(
                                text = stringResource(R.string.policy_title),
                                style = StaticTypeScale.Default.header3,
                                color = WeSpotThemeManager.colors.txtTitleColor,
                            )

                            AnnouncementContent(
                                navigator = navigator,
                                webLinkMap = state.webLinkMap,
                            )
                        }

                        if (state.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center),
                                color = WeSpotThemeManager.colors.primaryColor,
                            )
                        }
                    }
                }

                if (state.isBottomSheetShown) {
                    WSBottomSheet(closeSheet = {
                        action(AnnouncementPolicyAction.OnBottomSheetDismissed)
                    }) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 28.dp),
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickableSingle {
                                        action(AnnouncementPolicyAction.OnRevokeClicked)
                                    }
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = stringResource(R.string.revoke_account),
                                    style = StaticTypeScale.Default.body4,
                                    color = WeSpotThemeManager.colors.txtTitleColor,
                                )
                            }
                        }
                    }
                }

                if (state.isConfirmBottomSheetShown) {
                    WSBottomSheet(
                        closeSheet = { action(AnnouncementPolicyAction.OnConfirmBottomSheetDismissed) },
                    ) {
                        RevokeConfirmBottomSheetContent(
                            revokeConfirmed = state.revokeConfirmed,
                            onButtonClicked = { action(AnnouncementPolicyAction.OnConfirmRevokeClicked) },
                            onRevokeConfirmed = { action(AnnouncementPolicyAction.OnRevokeConfirmed) },
                        )
                    }
                }

                if (state.isDialogShown) {
                    WSDialog(
                        title = stringResource(com.bff.wespot.entire.R.string.revoke_dialog_title),
                        subTitle = "",
                        okButtonText = stringResource(com.bff.wespot.entire.R.string.close),
                        cancelButtonText = stringResource(com.bff.wespot.entire.R.string.revoke),
                        okButtonClick = { action(AnnouncementPolicyAction.OnDialogDismissed) },
                        cancelButtonClick = { action(AnnouncementPolicyAction.OnFinalRevokeClicked) },
                        onDismissRequest = { },
                    )
                }
            }

            viewModel.collectSideEffect {
                when (it) {
                    AnnouncementPolicySideEffect.NavigateToAuth -> {
                        val intent = navigator.navigateToAuth(this@AnnouncementPolicyActivity)
                        intent.putExtra(
                            EXTRA_TOAST_MESSAGE,
                            getString(com.bff.wespot.entire.R.string.revoke_done),
                        )
                        startActivity(intent)
                        finish()
                    }

                    AnnouncementPolicySideEffect.FinishActivity -> {
                        finish()
                    }
                }
            }

            LaunchedEffect(Unit) {
                action(AnnouncementPolicyAction.OnScreenEntered)
            }
        }
    }
}

@Composable
private fun AnnouncementContent(
    navigator: Navigator,
    webLinkMap: Map<String, String>,
) {
    val context = LocalContext.current

    val termsUrl = webLinkMap.getOrDefault(
        RemoteConfigKey.TERMS_OF_SERVICE_URL,
        stringResource(R.string.announcement_terms_url),
    )
    val privacyUrl = webLinkMap.getOrDefault(
        RemoteConfigKey.PRIVACY_POLICY_URL,
        stringResource(R.string.announcement_privacy_url),
    )
    val communityUrl = webLinkMap.getOrDefault(
        RemoteConfigKey.COMMUNITY_POLICY_URL,
        stringResource(R.string.announcement_community_url),
    )

    val annotatedText = buildAnnotatedString {
        append(stringResource(R.string.announcement_intro))

        withLink(
            LinkAnnotation.Clickable(
                tag = "terms",
                linkInteractionListener = {
                    navigator.navigateToWebLink(context, termsUrl)
                },
            ),
        ) {
            withStyle(
                style = SpanStyle(
                    color = WeSpotThemeManager.colors.primaryColor,
                    textDecoration = TextDecoration.None,
                ),
            ) {
                append(stringResource(R.string.announcement_terms_link))
            }
        }

        withLink(
            LinkAnnotation.Clickable(
                tag = "privacy",
                linkInteractionListener = {
                    navigator.navigateToWebLink(context, privacyUrl)
                },
            ),
        ) {
            withStyle(
                style = SpanStyle(
                    color = WeSpotThemeManager.colors.primaryColor,
                    textDecoration = TextDecoration.None,
                ),
            ) {
                append(stringResource(R.string.announcement_privacy_link))
            }
        }

        withLink(
            LinkAnnotation.Clickable(
                tag = "community",
                linkInteractionListener = {
                    navigator.navigateToWebLink(context, communityUrl)
                },
            ),
        ) {
            withStyle(
                style = SpanStyle(
                    color = WeSpotThemeManager.colors.primaryColor,
                    textDecoration = TextDecoration.None,
                ),
            ) {
                append(stringResource(R.string.announcement_community_link))
            }
        }

        append(stringResource(R.string.announcement_middle))

        withLink(
            LinkAnnotation.Clickable(
                tag = "email",
                linkInteractionListener = {
                    navigator.navigateToWebLink(
                        context,
                        "mailto:wespot.official.app@gmail.com",
                    )
                },
            ),
        ) {
            withStyle(
                style = SpanStyle(
                    color = WeSpotThemeManager.colors.primaryColor,
                    textDecoration = TextDecoration.None,
                ),
            ) {
                append(stringResource(R.string.announcement_email))
            }
        }

        append(stringResource(R.string.announcement_outro))
    }

    Text(
        text = annotatedText,
        style = StaticTypeScale.Default.body5,
        color = WeSpotThemeManager.colors.txtTitleColor,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnnouncementTopBar(
    onThreeDotClick: () -> Unit,
) {
    WSTopBar(
        title = "",
        action = {
            IconButton(
                onClick = { onThreeDotClick.invoke() },
            ) {
                Icon(
                    painter = painterResource(
                        com.bff.wespot.community.presentation.R.drawable.horizontal_3dot,
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                )
            }
        },
    )
}

@Composable
private fun RevokeConfirmBottomSheetContent(
    revokeConfirmed: Boolean,
    onButtonClicked: () -> Unit,
    onRevokeConfirmed: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(top = 28.dp),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            text = stringResource(com.bff.wespot.entire.R.string.revoke_bottom_sheet_title),
            style = StaticTypeScale.Default.body1,
            color = WeSpotThemeManager.colors.txtTitleColor,
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 20.dp, end = 20.dp),
            text = stringResource(com.bff.wespot.entire.R.string.revoke_bottom_sheet_content),
            style = StaticTypeScale.Default.body6,
            color = WeSpotThemeManager.colors.txtSubColor,
            maxLines = 2,
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp, start = 20.dp, end = 20.dp),
            text = stringResource(com.bff.wespot.entire.R.string.revoke_bottom_sheet_content2),
            style = StaticTypeScale.Default.body6,
            color = WeSpotThemeManager.colors.txtSubColor,
            maxLines = 2,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 19.dp, bottom = 12.dp, start = 20.dp, end = 20.dp)
                .clickable { onRevokeConfirmed() },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = com.bff.wespot.entire.R.drawable.ic_check),
                contentDescription = stringResource(com.bff.wespot.entire.R.string.check_icon),
                tint = if (revokeConfirmed) {
                    WeSpotThemeManager.colors.primaryColor
                } else {
                    WeSpotThemeManager.colors.disableIcnColor
                },
            )

            Text(
                text = stringResource(com.bff.wespot.entire.R.string.revoke_confirm_text),
                style = StaticTypeScale.Default.body5,
                color = WeSpotThemeManager.colors.txtTitleColor,
            )
        }

        WSButton(
            text = stringResource(com.bff.wespot.entire.R.string.do_revoke),
            onClick = { onButtonClicked() },
            enabled = revokeConfirmed,
            content = { it() },
        )
    }
}
