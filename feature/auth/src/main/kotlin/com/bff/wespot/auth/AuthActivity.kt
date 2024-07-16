package com.bff.wespot.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.bff.wespot.auth.screen.NavGraphs
import com.bff.wespot.auth.screen.destinations.ClassScreenDestination
import com.bff.wespot.auth.screen.destinations.CompleteScreenDestination
import com.bff.wespot.auth.screen.destinations.EditScreenDestination
import com.bff.wespot.auth.screen.destinations.GenderScreenDestination
import com.bff.wespot.auth.screen.destinations.GradeScreenDestination
import com.bff.wespot.auth.screen.destinations.NameScreenDestination
import com.bff.wespot.auth.screen.destinations.SchoolScreenDestination
import com.bff.wespot.auth.state.AuthSideEffect
import com.bff.wespot.auth.viewmodel.AuthViewModel
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.rememberNavHostEngine
import dagger.hilt.android.AndroidEntryPoint
import org.orbitmvi.orbit.compose.collectSideEffect

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val engine = rememberNavHostEngine()
            val viewModel: AuthViewModel = viewModel()

            viewModel.collectSideEffect {
                when (it) {
                    AuthSideEffect.PopBackStack -> navController.popBackStack()
                    is AuthSideEffect.NavigateToGradeScreen -> {
                        navController.navigate(GradeScreenDestination(it.edit))
                    }

                    is AuthSideEffect.NavigateToSchoolScreen -> {
                        navController.navigate(SchoolScreenDestination(it.edit))
                    }

                    is AuthSideEffect.NavigateToClassScreen -> {
                        navController.navigate(ClassScreenDestination(it.edit))
                    }

                    is AuthSideEffect.NavigateToGenderScreen -> {
                        navController.navigate(GenderScreenDestination(it.edit))
                    }

                    is AuthSideEffect.NavigateToNameScreen -> {
                        navController.navigate(NameScreenDestination(it.edit))
                    }

                    AuthSideEffect.NavigateToEditScreen -> {
                        navController.navigate(EditScreenDestination)
                    }

                    AuthSideEffect.NavigateToCompleteScreen -> {
                        navController.navigate(CompleteScreenDestination, navOptionsBuilder = {
                            popUpTo(SchoolScreenDestination.route) { inclusive = true }
                        })
                    }
                }
            }

            WeSpotTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        navController = navController,
                        engine = engine,
                        dependenciesContainerBuilder = {
                            dependency(viewModel)
                        },
                    )
                }
            }
        }
    }
}
