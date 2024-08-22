package com.bff.wespot.model.common

data class KakaoContent(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String,
    val buttonText: String,
    val url: String,
) {
    companion object {
        val EMPTY = KakaoContent(
            id = 0,
            title = "",
            description = "",
            imageUrl = "",
            buttonText = "",
            url = "",
        )
    }
}
