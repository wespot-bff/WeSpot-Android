package com.wespot.navigation

import android.content.Context
import android.content.Intent

interface Navigator {
    fun navigateToMain(
        context: Context
    ) : Intent
}