package com.bff.wespot.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.bff.wespot.auth.screen.AuthNavGraph
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
import com.bff.wespot.designsystem.component.indicator.WSToastType
import com.bff.wespot.designsystem.component.modal.WSDialog
import com.bff.wespot.designsystem.theme.WeSpotTheme
import com.bff.wespot.model.constants.LoginState
import com.bff.wespot.navigation.Navigator
import com.bff.wespot.navigation.util.EXTRA_DATE
import com.bff.wespot.navigation.util.EXTRA_TARGET_ID
import com.bff.wespot.navigation.util.EXTRA_TOAST_MESSAGE
import com.bff.wespot.navigation.util.EXTRA_TYPE
import com.bff.wespot.ui.LoadingAnimation
import com.bff.wespot.ui.TopToast
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.rememberNavHostEngine
import dagger.hilt.android.AndroidEntryPoint
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {
    private val viewModel by viewModels<AuthViewModel>()
    private lateinit var loginState: LoginState

    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val toastMessage = intent.getStringExtra(EXTRA_TOAST_MESSAGE)

        setContent {
            var showDialog by remember {
                mutableStateOf(false)
            }
            val context = LocalContext.current

            val navController = rememberNavController()
            val engine = rememberNavHostEngine()
            var showToast by remember { mutableStateOf(true) }

            val state by viewModel.collectAsState()
            val action = viewModel::onAction

            login {
                showDialog = true
            }

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
                        val intent = navigator.navigateToMain(
                            this,
                            Pair("", 0),
                            Pair("", ""),
                            Pair("", ""),
                        )
                        startActivity(intent)
                    }
                }
            }

            WeSpotTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    DestinationsNavHost(
                        navGraph = AuthNavGraph,
                        navController = navController,
                        engine = engine,
                        dependenciesContainerBuilder = {
                            dependency(viewModel)
                            dependency(navigator)
                        },
                    )
                }

                if (toastMessage != null) {
                    TopToast(
                        message = toastMessage,
                        toastType = WSToastType.Success,
                        showToast = showToast,
                    ) {
                        showToast = false
                    }
                }
                if (state.loading) {
                    LoadingAnimation()
                }

                if (showDialog) {
                    WSDialog(
                        title = stringResource(R.string.new_version),
                        subTitle = stringResource(R.string.update_to_new_version),
                        okButtonText = stringResource(R.string.update),
                        cancelButtonText = stringResource(R.string.cancel),
                        okButtonClick = {
                            navigator.navigateToWebLink(context, state.playStoreLink)
                        },
                        cancelButtonClick = {
                            finish()
                        },
                        onDismissRequest = {}
                    )
                }
            }
        }
    }

    private fun login(
        showUpdateDialog: () -> Unit,
    ) {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val versionCode = packageInfo.versionName

        viewModel.onAction(AuthAction.AutoLogin(versionCode))
        viewModel.loginState.observe(this) {
            loginState = it
        }

        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                return if (::loginState.isInitialized) {
                    if (loginState == LoginState.LOGIN_SUCCESS) {
                        val targetId = intent.getStringExtra("targetId")?.toInt() ?: -1
                        val date = intent.getStringExtra("date") ?: ""
                        val type = intent.getStringExtra("type") ?: ""

                        val intent = navigator.navigateToMain(
                            this@AuthActivity,
                            targetId = Pair(EXTRA_TARGET_ID, targetId),
                            date = Pair(EXTRA_DATE, date),
                            type = Pair(EXTRA_TYPE, type),
                        )
                        startActivity(intent)
                    } else if (loginState == LoginState.FORCE_UPDATE) {
                        showUpdateDialog()
                    }

                    content.viewTreeObserver.removeOnPreDrawListener(this)
                    true
                } else {
                    false
                }
            }
        })
    }

    companion object {
        fun intent(context: Context): Intent {
            return Intent(context, AuthActivity::class.java)
        }
    }
}
