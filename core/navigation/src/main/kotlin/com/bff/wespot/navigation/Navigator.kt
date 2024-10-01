package com.bff.wespot.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri

interface Navigator {
    fun navigateToMain(
        context: Context,
        targetId: Pair<String, Int> = Pair("", 0),
        userId: Pair<String, String> = Pair("", ""),
        type: Pair<String, String> = Pair("", ""),
        date: Pair<String, String> = Pair("", ""),
    ): Intent

    fun navigateToAuth(context: Context): Intent

    fun navigateToAuthWithExtra(
        context: Context,
        targetId: Pair<String, Int> = Pair("", 0),
        userId: Pair<String, String> = Pair("", ""),
        type: Pair<String, String> = Pair("", ""),
        date: Pair<String, String> = Pair("", ""),
    ): Intent

    fun navigateToInstaStory(
        context: Context,
        file: Uri,
    ): Intent

    fun navigateToSharing(context: Context, text: String)

    fun navigateToKakao(
        context: Context,
        title: String,
        description: String,
        imageUrl: String,
        buttonText: String,
        url: String,
    )

    fun redirectToPlayStoreForInstagram(context: Context)

    fun navigateToWebLink(context: Context, webLink: String)
}
