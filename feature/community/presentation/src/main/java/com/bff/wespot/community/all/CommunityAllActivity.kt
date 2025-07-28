package com.bff.wespot.community.all

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bff.wespot.community.all.screen.AllPostsScreen
import com.bff.wespot.community.all.screen.CommunityAllScreen
import com.bff.wespot.community.all.screen.MyCommentsScreen
import com.bff.wespot.community.all.screen.ScrapsScreen
import com.bff.wespot.community.presentation.R
import com.bff.wespot.designsystem.component.header.WSTopBar
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint
import org.orbitmvi.orbit.compose.collectAsState
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
                    ) {
                        composable("community_all_menu") {
                            val communityAllViewModel: CommunityAllViewModel = hiltViewModel()
                            val uiState by communityAllViewModel.collectAsState()

                            CommunityAllScreen(
                                onNavigateToAllPosts = {
                                    title = getString(R.string.all_post_written)
                                    navController.navigate("all_posts")
                                },
                                onNavigateToMyComments = {
                                    title = getString(R.string.all_comment_written)
                                    navController.navigate("my_comments")
                                },
                                onNavigateToScraps = {
                                    title = getString(R.string.all_post_scraped)
                                    navController.navigate("scraps")
                                },
                            )
                        }

                        composable("all_posts") {
                            AllPostsScreen(
                                navigateUp = navController::navigateUp,
                            )
                        }

                        composable("my_comments") {
                            MyCommentsScreen(
                                navigateUp = navController::navigateUp,
                            )
                        }

                        composable("scraps") {
                            ScrapsScreen(
                                navigateUp = navController::navigateUp,
                            )
                        }
                    }
                }
            }
        }
    }
}
