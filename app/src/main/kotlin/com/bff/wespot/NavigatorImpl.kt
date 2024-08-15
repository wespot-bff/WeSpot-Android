package com.bff.wespot

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import com.bff.wespot.auth.AuthActivity
import com.bff.wespot.navigation.Navigator
import com.bff.wespot.navigation.util.WebLink
import com.bff.wespot.navigation.util.buildIntent
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


class NavigatorImpl @Inject constructor() : Navigator {
    private val sharingName = listOf(
        "instagram",
        "kakao",
    )

    override fun navigateToMain(
        context: Context,
        targetId: Pair<String, Int>,
        date: Pair<String, String>,
        type: Pair<String, String>,
    ): Intent {
        val intent = context.buildIntent<MainActivity>(targetId, date, type)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        return intent
    }

    override fun navigateToAuth(context: Context): Intent {
        val intent = context.buildIntent<AuthActivity>()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        return intent
    }

    override fun navigateToSharing(context: Context) {
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
                )
            )
            putExtra(Intent.EXTRA_SUBJECT, "메시지 제목")
            putExtra(Intent.EXTRA_TEXT, "메시지 내용")
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
            putExtra(Intent.EXTRA_SUBJECT, "메시지 제목")
            putExtra(Intent.EXTRA_TEXT, "메시지 내용")
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
            }
        if (intent.resolveActivity(context.packageManager) == null) {
            redirectToPlayStoreForInstagram(context)
        }

        intent.apply {
            putExtra("source_application", BuildConfig.FACEBOOK_APP_ID)
            setDataAndType(file, "image/jpeg")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        return intent
    }

    override fun navigateToWebLink(context: Context, webLink: WebLink) {
        val webLinkIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(context.getString(webLink.url)),
        )
        webLinkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(webLinkIntent)
    }

    override fun navigateToKakao(
        context: Context,
        title: String,
        description: String,
        buttonText: String,
        url: String,
    ) {
        val feed = kakaoTemplate(title, description, buttonText, url)
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

    private fun redirectToPlayStoreForInstagram(context: Context) {
        val appStoreIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/details?id=com.instagram.android"),
        )
        appStoreIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(appStoreIntent)
    }

    private fun getImageUri(drawableId: Int, context: Context): Uri {
        var bitmap: Bitmap?
        bitmap = context.resources.getDrawable(drawableId, null).toBitmap()

        val imagesFolder: File = File(context.cacheDir, "images")
        var contentUri: Uri? = null
        try {
            imagesFolder.mkdirs()
            val file = File(imagesFolder, "shared_image.png")
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()
            contentUri = FileProvider.getUriForFile(context, "com.bff.wespot.fileProvider", file)
        } catch (e: Exception) {
            Timber.e("Error => " + e.message)
        }
        return contentUri!!
    }

    private fun kakaoTemplate(
        title: String,
        description: String,
        buttonText: String,
        url: String
    ): FeedTemplate =
        FeedTemplate(
            content = Content(
                title = title,
                description = description,
                imageUrl = "http://k.kakaocdn.net/dn/bTYZpF/btqCmI8nJ4K/3kU1p3kUvKQKUkKkTgKQSK/img_640x640.jpg",
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
}
