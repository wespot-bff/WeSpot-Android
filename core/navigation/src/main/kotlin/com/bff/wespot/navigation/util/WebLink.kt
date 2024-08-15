package com.bff.wespot.navigation.util

import com.bff.wespot.navigation.R

enum class WebLink {
    PLAY_STORE,
    VOTE_QUESTION_GOOGLE_FORM,
    WESPOT_KAKAKO_CHANNEL,
    WESPOT_INSTARGRAM,
    USER_OPINION_GOOGLE_FORM,
    RESEARCH_PARTICIPATION_GOOGLE_FORM,
    WESPOT_MAKERS,
    PROFILE_CHANGE_GOOGLE_FROM,
    PRIVACY_POLICY,
    TERMS_OF_SERVICE;

    val url
        get() = when (this) {
            PLAY_STORE -> R.string.play_store_url
            VOTE_QUESTION_GOOGLE_FORM -> R.string.vote_question_google_form_url
            WESPOT_KAKAKO_CHANNEL -> R.string.wespot_kakao_channel_url
            WESPOT_INSTARGRAM -> R.string.wespot_instagram_url
            USER_OPINION_GOOGLE_FORM -> R.string.user_opinion_google_form_url
            RESEARCH_PARTICIPATION_GOOGLE_FORM -> R.string.research_participation_google_form_url
            WESPOT_MAKERS -> R.string.wespot_makers_url
            PROFILE_CHANGE_GOOGLE_FROM -> R.string.profile_change_google_form_url
            PRIVACY_POLICY -> R.string.privacy_policy_url
            TERMS_OF_SERVICE -> R.string.terms_of_service_url
        }
}
