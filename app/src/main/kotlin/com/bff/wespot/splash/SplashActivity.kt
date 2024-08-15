package com.bff.wespot.splash

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bff.wespot.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : ComponentActivity() {
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                return@setKeepOnScreenCondition viewModel.start.value.not()
            }
            setOnExitAnimationListener { splashScreen ->
                ObjectAnimator.ofFloat(
                    splashScreen.view,
                    View.TRANSLATION_Y,
                    0f, splashScreen.view.height.toFloat()
                ).apply {
                    interpolator = DecelerateInterpolator()
                    duration = 500L
                    val intent = AuthActivity.intent(this@SplashActivity)
                    startActivity(intent)
                    doOnEnd {
                        splashScreen.remove()
                        finish()
                    }
                    start()
                }
            }
        }
    }
}
