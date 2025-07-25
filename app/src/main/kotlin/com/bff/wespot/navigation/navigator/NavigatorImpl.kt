package com.bff.wespot.navigation.navigator

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.bff.wespot.BuildConfig
import com.bff.wespot.auth.AuthActivity
import com.bff.wespot.community.detail.PostDetailActivity
import com.bff.wespot.community.write.WritePostActivity
import com.bff.wespot.main.MainActivity
import com.bff.wespot.navigation.Navigator
import com.bff.wespot.navigation.util.buildIntent
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import timber.log.Timber
import javax.inject.Inject

class NavigatorImpl @Inject constructor() : Navigator {
    override fun navigateToMain(
        context: Context,
        type: Pair<String, String>,
        date: Pair<String, String>,
        deepLink: Pair<String, String>,
    ): Intent {
        val intent = context.buildIntent<MainActivity>(type, date, deepLink)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        return intent
    }

    override fun navigateToAuth(context: Context): Intent {
        val intent = context.buildIntent<AuthActivity>()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        return intent
    }

    override fun navigateToAuthWithExtra(
        context: Context,
        type: Pair<String, String>,
        date: Pair<String, String>,
        deepLink: Pair<String, String>,
    ): Intent {
        val intent = context.buildIntent<AuthActivity>(type, date, deepLink)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        return intent
    }

    override fun navigateToSharing(context: Context, text: String) {
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.setType("text/plain")

        val chooser: MutableList<Intent> = mutableListOf()
        val resInfo = context.packageManager.queryIntentActivities(sendIntent, 0)

        if (resInfo.isEmpty()) {
            return
        }

        val kakaoIntent = Intent(Intent.ACTION_SEND).apply {
            setComponent(
                ComponentName(
                    "com.kakao.talk",
                    "com.kakao.talk.activity.IntentFilterActivity",
                ),
            )
            putExtra(Intent.EXTRA_TEXT, text)
            setType("text/plain")
        }

        chooser.add(kakaoIntent)

        val instaIntent = Intent(Intent.ACTION_SEND).apply {
            setComponent(
                ComponentName(
                    "com.instagram.android",
                    "com.instagram.direct.share.handler.DirectShareHandlerActivity",
                ),
            )
            setType("text/plain")
            putExtra(Intent.EXTRA_TEXT, text)
        }

        chooser.add(instaIntent)

        val choser = Intent.createChooser(chooser.removeAt(0), "타이틀")
        choser.putExtra(Intent.EXTRA_INITIAL_INTENTS, chooser.toTypedArray())
        context.startActivity(choser)
    }

    override fun navigateToInstaStory(context: Context, file: Uri): Intent {
        val intent = Intent("com.instagram.share.ADD_TO_STORY")
            .apply {
                setDataAndType(null, "image/*")
                setPackage("com.instagram.android")
            }
        return intent.apply {
            putExtra("source_application", BuildConfig.FACEBOOK_APP_ID)
            setDataAndType(file, "image/jpeg")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    override fun navigateToWebLink(context: Context, webLink: String) {
        try {
            val webLinkIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(webLink),
            )
            webLinkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(webLinkIntent)
        } catch (e: ActivityNotFoundException) {
            Timber.e(e)
        }
    }

    override fun navigateToKakao(
        context: Context,
        title: String,
        description: String,
        imageUrl: String,
        buttonText: String,
        url: String,
    ) {
        val feed = kakaoTemplate(title, description, imageUrl, buttonText, url)
        if (ShareClient.instance.isKakaoTalkSharingAvailable(context)) {
            ShareClient.instance.shareDefault(context, feed) { sharingResult, error ->
                if (error != null) {
                    Timber.e("Error => " + error.message)
                } else if (sharingResult != null) {
                    Timber.d("Success => " + sharingResult.intent)
                    context.startActivity(sharingResult.intent)

                    Timber.w("Warning Msg: ${sharingResult.warningMsg}")
                    Timber.w("Argument Msg: ${sharingResult.argumentMsg}")
                }
            }
        } else {
            val shareUrl = WebSharerClient.instance.makeDefaultUrl(feed)

            try {
                KakaoCustomTabsClient.openWithDefault(context, shareUrl)
            } catch (e: UnsupportedOperationException) {
                Timber.e("Error => " + e.message)
            }

            try {
                KakaoCustomTabsClient.open(context, shareUrl)
            } catch (e: UnsupportedOperationException) {
                Timber.e("Error => " + e.message)
            }
        }
    }

    override fun redirectToPlayStoreForInstagram(context: Context) {
        try {
            val appStoreIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=com.instagram.android"),
            )
            appStoreIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(appStoreIntent)
        } catch (e: ActivityNotFoundException) {
            Timber.e(e)
        }
    }

    private fun kakaoTemplate(
        title: String,
        description: String,
        imageUrl: String,
        buttonText: String,
        url: String,
    ): FeedTemplate =
        FeedTemplate(
            content = Content(
                title = title,
                description = description,
                imageUrl = imageUrl,
                link = Link(
                    webUrl = url,
                    mobileWebUrl = url,
                ),
            ),
            buttons = listOf(
                Button(
                    buttonText,
                    Link(
                        webUrl = url,
                        mobileWebUrl = url,
                    ),
                ),
            ),
        )

    override fun navigateToWriteActivity(context: Context): Intent {
        val intent = context.buildIntent<WritePostActivity>()
        return intent
    }

    override fun navigateToPostDetailActivity(context: Context, postId: String): Intent {
        val intent = context.buildIntent<PostDetailActivity>()
        intent.putExtra("postId", postId)
        return intent
    }
}
