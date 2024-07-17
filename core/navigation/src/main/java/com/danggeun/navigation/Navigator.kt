package com.danggeun.navigation

import android.content.Context
import android.content.Intent

interface Navigator {
    fun navigateToMain(
        context: Context
    ) : Intent
}