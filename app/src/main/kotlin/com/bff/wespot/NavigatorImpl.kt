package com.bff.wespot

import android.content.Context
import android.content.Intent
import com.danggeun.common.util.buildIntent
import com.danggeun.navigation.Navigator

class NavigatorImpl : Navigator {
    override fun navigateToMain(
        context: Context,
    ): Intent {
        val intent = context.buildIntent<MainActivity>()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        return intent
    }
}
