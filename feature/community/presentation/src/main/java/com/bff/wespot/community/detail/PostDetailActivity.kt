package com.bff.wespot.community.detail

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import com.bff.wespot.community.detail.screen.PostDetailScreen
import com.bff.wespot.community.detail.state.PostDetailAction
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

    private val editPostLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            viewModel.onAction(PostDetailAction.RefreshPost)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                scrim = android.graphics.Color.TRANSPARENT,
            ),
            navigationBarStyle = SystemBarStyle.dark(
                scrim = android.graphics.Color.TRANSPARENT,
            ),
        )

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
                            postId = sideEffect.id,
                            title = postData.infoSection.title?.text,
                            description = postData.infoSection.description.text,
                            category = postData.category.text.text,
                            images = when (val contentSection = postData.contentSection) {
                                is ContentSectionUiModel.ImagesContentUiModel -> contentSection.images
                                is ContentSectionUiModel.SingleImageUiModel -> listOf(contentSection.image)
                                else -> emptyList()
                            },
                        )
                        editPostLauncher.launch(intent)
                    }

                    is PostDetailSideEffect.OnBackClick -> {
                        finish()
                    }

                    is PostDetailSideEffect.OnPostDeletedOrBlocked -> {
                        val resultIntent = Intent().apply {
                            putExtra("refresh", true)
                        }
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    }

                    is PostDetailSideEffect.OnCategoryClick -> {
                        val intent = navigator.navigateToCategoryDetail(
                            this@PostDetailActivity,
                            sideEffect.target,
                            sideEffect.categoryText,
                        )

                        startActivity(intent)
                    }

                    is PostDetailSideEffect.NavigateToPostReportScreen -> {
                        val intent = navigator.navigateToCommunityReport(
                            context = this@PostDetailActivity,
                            targetId = sideEffect.postId,
                            reportType = "POST",
                        )
                        startActivity(intent)
                    }

                    is PostDetailSideEffect.NavigateToCommentReportScreen -> {
                        val intent = navigator.navigateToCommunityReport(
                            context = this@PostDetailActivity,
                            targetId = sideEffect.commentId,
                            reportType = "COMMENT",
                        )
                        startActivity(intent)
                    }
                }
            }
        }
    }
}
