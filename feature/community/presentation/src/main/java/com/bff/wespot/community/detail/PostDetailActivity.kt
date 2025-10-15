package com.bff.wespot.community.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import com.bff.wespot.community.detail.screen.PostDetailScreen
import com.bff.wespot.community.detail.state.PostDetailSideEffect
import com.bff.wespot.community.uimodel.PostDetailUiModel.PostDetailContentUiModel.ContentSectionUiModel
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import javax.inject.Inject

@AndroidEntryPoint
class PostDetailActivity : ComponentActivity() {
    private val viewModel: PostDetailViewModel by viewModels()

    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val state by viewModel.collectAsState()
            val onAction = viewModel::onAction
            WeSpotTheme {
                PostDetailScreen(
                    uiState = state,
                    onAction = onAction,
                )
            }

            viewModel.collectSideEffect { sideEffect ->
                when (sideEffect) {
                    is PostDetailSideEffect.NavigateToEditPost -> {
                        val postData = sideEffect.postData
                        val intent = navigator.navigateToEditPostActivity(
                            context = this@PostDetailActivity,
                            title = postData.infoSection.title.text,
                            description = postData.infoSection.description.text,
                            category = postData.category.target,
                            images = when (val contentSection = postData.contentSection) {
                                is ContentSectionUiModel.ImagesContentUiModel -> contentSection.images
                                is ContentSectionUiModel.SingleImageUiModel -> listOf(contentSection.image)
                                else -> emptyList()
                            },
                        )
                        startActivity(intent)
                    }
                }
            }
        }
    }
}
