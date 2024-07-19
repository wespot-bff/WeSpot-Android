package com.bff.wespot.auth

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.bff.wespot.auth.screen.NavGraphs
import com.bff.wespot.auth.screen.destinations.ClassScreenDestination
import com.bff.wespot.auth.screen.destinations.CompleteScreenDestination
import com.bff.wespot.auth.screen.destinations.EditScreenDestination
import com.bff.wespot.auth.screen.destinations.GenderScreenDestination
import com.bff.wespot.auth.screen.destinations.GradeScreenDestination
import com.bff.wespot.auth.screen.destinations.NameScreenDestination
import com.bff.wespot.auth.screen.destinations.SchoolScreenDestination
import com.bff.wespot.auth.state.AuthAction
import com.bff.wespot.auth.state.AuthSideEffect
import com.bff.wespot.auth.viewmodel.AuthViewModel
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.model.constants.LoginState
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.wespot.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {
    private val viewModel by viewModels<AuthViewModel>()
    private lateinit var loginState: LoginState

    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        login()

        setContent {
            val navController = rememberNavController()
            val engine = rememberNavHostEngine()

            val state by viewModel.collectAsState()
            val action = viewModel::onAction

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

                    AuthSideEffect.NavigateToMainActivity -> {
                        val intent = navigator.navigateToMain(this)
                        startActivity(intent)
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

            if (state.loading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            LaunchedEffect(key1 = Unit) {
                action(AuthAction.AutoLogin)
            }
        }
    }

    private fun login() {
        viewModel.onAction(AuthAction.AutoLogin)
        viewModel.loginState.observe(this) {
            loginState = it
        }

        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                return if (::loginState.isInitialized) {
                    if (loginState == LoginState.LOGIN_SUCCESS) {
                        val intent = navigator.navigateToMain(this@AuthActivity)
                        startActivity(intent)
                    }
                    content.viewTreeObserver.removeOnPreDrawListener(this)
                    true
                } else {
                    false
                }
            }
        })
    }
}
