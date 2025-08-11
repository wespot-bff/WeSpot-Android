package com.bff.wespot.community.categorydetail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.paging.compose.collectAsLazyPagingItems
import com.bff.wespot.community.categorydetail.screen.CategoryScreen
import com.bff.wespot.community.categorydetail.state.CategoryDetailSideEffect
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import javax.inject.Inject

@AndroidEntryPoint
class CategoryDetailActivity : ComponentActivity() {
    @Inject
    lateinit var navigator: Navigator

    private val viewModel: CategoryDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            WeSpotTheme {
                val uiState by viewModel.collectAsState()
                val paging = uiState.posts.collectAsLazyPagingItems()

                CategoryScreen(
                    uiState = uiState,
                    paging = paging,
                    onAction = viewModel::onAction,
                    navigateToPost = { postId ->
                        val intent = navigator.navigateToPostDetailActivity(
                            context = this,
                            postId = postId,
                        )
                        startActivity(intent)
                    },
                )

                viewModel.collectSideEffect { sideEffect ->
                    when (sideEffect) {
                        is CategoryDetailSideEffect.NavigateToPostDetail -> {
                            val intent = navigator.navigateToPostDetailActivity(
                                context = this,
                                postId = sideEffect.postId,
                            )
                            startActivity(intent)
                        }

                        is CategoryDetailSideEffect.NavigateBack -> {
                            finish()
                        }

                        is CategoryDetailSideEffect.NavigateToCreate -> {
                            val intent = navigator.navigateToWriteActivity(
                                context = this,
                                category = sideEffect.category.text,
                            )
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}
