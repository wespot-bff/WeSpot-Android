package com.bff.wespot.splash

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bff.wespot.R
import com.bff.wespot.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SplashActivity : ComponentActivity() {
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            installSplashScreen().apply {
                setKeepOnScreenCondition {
                    return@setKeepOnScreenCondition viewModel.start.value.not()
                }
                setOnExitAnimationListener {
                    val intent = AuthActivity.intent(this@SplashActivity)
                    startActivity(intent)
                    finish()
                }
            }
        } else {
            setContentView(R.layout.activity_splash)
            actionBar?.hide()

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.CREATED) {
                    viewModel.start.collect {
                        if (it) {
                            delay(500)
                            val intent = AuthActivity.intent(this@SplashActivity)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }
    }
}
