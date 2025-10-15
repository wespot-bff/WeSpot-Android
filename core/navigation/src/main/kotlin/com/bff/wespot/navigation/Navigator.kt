package com.bff.wespot.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri

interface Navigator {
    fun navigateToMain(
        context: Context,
        type: Pair<String, String> = Pair("", ""),
        date: Pair<String, String> = Pair("", ""),
        deepLink: Pair<String, String> = Pair("", ""),
    ): Intent

    fun navigateToAuth(context: Context): Intent

    fun navigateToAuthWithExtra(
        context: Context,
        type: Pair<String, String> = Pair("", ""),
        date: Pair<String, String> = Pair("", ""),
        deepLink: Pair<String, String> = Pair("", ""),
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

    fun navigateToWriteActivity(context: Context): Intent

    fun navigateToPostDetailActivity(context: Context, postId: String): Intent

    fun navigateToCommunitySearch(context: Context): Intent
}
