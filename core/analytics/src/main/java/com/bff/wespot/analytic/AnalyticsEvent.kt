package com.bff.wespot.analytic

data class AnalyticsEvent(
    val type: String,
    val extras: List<Param> = emptyList()
) {
    object Types {
        const val SCREEN_VIEW = "screen_view" // (extras: SCREEN_NAME)
        const val SELECT_ITEM = "select_item"
        const val BUTTON_CLICK = "button_click"
        const val SUBMIT_RATING = "submit_rating"
    }

    data class Param(val key: String, val value: String)

    object ParamKeys {
        const val SCREEN_NAME = "screen_name"
        const val BUTTON_ID = "button_id"
        const val ITEM_ID = "item_id"
        const val ITEM_NAME = "item_name"
        const val RATING_TYPE = "rating_type"
        const val RATING_CONTENT = "rating_content"
    }
}