package com.bff.wespot.community.all

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bff.wespot.community.all.screen.AllPostsScreen
import com.bff.wespot.community.all.screen.CommunityAllScreen
import com.bff.wespot.community.all.state.CommunityAllSideEffect
import com.bff.wespot.community.all.state.MenuType
import com.bff.wespot.community.presentation.R
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint
import org.orbitmvi.orbit.compose.collectSideEffect
import javax.inject.Inject

@AndroidEntryPoint
class CommunityAllActivity : ComponentActivity() {
    private val viewModel: CommunityAllViewModel by viewModels()

    @Inject
    lateinit var navigator: Navigator

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            var title by remember {
                mutableStateOf("")
            }

            WeSpotTheme {
                Scaffold(
                    topBar = {
                        WSTopBar(
                            title = title,
                            canNavigateBack = true,
                            navigateUp = ::finish,
                        )
                    },
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = "community_all_menu",
                        modifier = Modifier.padding(paddingValues),
                        enterTransition = { fadeIn() },
                        exitTransition = { fadeOut() },
                    ) {
                        composable("community_all_menu") {
                            CommunityAllScreen(
                                onNavigateToAllPosts = {
                                    navController.navigate("all_posts/${MenuType.Written.name}")
                                    title = getString(R.string.all_post_written)
                                },
                                onNavigateToMyComments = {
                                    navController.navigate("all_posts/${MenuType.Commented.name}")
                                    title = getString(R.string.all_comment_written)
                                },
                                onNavigateToScraps = {
                                    navController.navigate("all_posts/${MenuType.Scrapped.name}")
                                    title = getString(R.string.all_post_scraped)
                                },
                            )
                        }

                        composable("all_posts/{menuType}") { backStackEntry ->
                            val menuType = MenuType.valueOf(
                                backStackEntry.arguments?.getString("menuType")
                                    ?: MenuType.Written.name,
                            )

                            AllPostsScreen(
                                menuType = menuType,
                                viewModel = viewModel,
                            )
                        }
                    }

                    viewModel.collectSideEffect {
                        when (it) {
                            is CommunityAllSideEffect.NavigateUp -> {
                                navController.navigateUp()
                            }

                            is CommunityAllSideEffect.NavigateToDetail -> {
                                val intent = navigator.navigateToPostDetailActivity(
                                    this@CommunityAllActivity,
                                    it.id,
                                )
                                startActivity(intent)
                            }

                            is CommunityAllSideEffect.NavigateToCategory -> {
                                val intent = navigator.navigateToCategoryDetail(
                                    this@CommunityAllActivity,
                                    it.categoryId,
                                )
                                startActivity(intent)
                            }
                        }
                    }
                }
            }
        }
    }
}
