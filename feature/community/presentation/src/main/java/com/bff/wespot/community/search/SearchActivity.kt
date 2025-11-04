package com.bff.wespot.community.search

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import com.bff.wespot.community.search.state.SearchAction
import com.bff.wespot.community.search.state.SearchSideEffect
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import javax.inject.Inject

@AndroidEntryPoint
class SearchActivity : ComponentActivity() {
    private val viewModel: SearchViewModel by viewModels()

    @Inject
    lateinit var navigator: Navigator

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
            val uiState by viewModel.collectAsState()

            WeSpotTheme {
                SearchScreen(
                    state = uiState,
                    navigateUp = ::finish,
                    action = viewModel::onAction,
                )
            }

            viewModel.collectSideEffect {
                when (it) {
                    is SearchSideEffect.NavigateToDetail -> {
                        val intent = navigator.navigateToPostDetailActivity(
                            this@SearchActivity,
                            it.postId,
                            it.navigateToComment,
                        )

                        startActivity(intent)
                    }

                    is SearchSideEffect.NavigateToCategory -> {
                        val intent = navigator.navigateToCategoryDetail(
                            context = this@SearchActivity,
                            categoryId = it.categoryId,
                            categoryText = it.categoryText,
                        )
                        startActivity(intent)
                    }
                }
            }

            LaunchedEffect(Unit) {
                viewModel.onAction(SearchAction.MonitorUserInput)
            }
        }
    }
}
