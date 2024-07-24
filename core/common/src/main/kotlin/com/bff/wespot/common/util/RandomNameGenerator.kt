package com.bff.wespot.common.util

class RandomNameGenerator {
    fun getRandomName(): String = "${colorList.random()} ${animalList.random()}"

    companion object {
        private val colorList = listOf(
            "빨간색", "주황색", "노란색", "초록색", "파란색", "남색", "보라색", "핑크색", "갈색", "연두색", "하늘색",
        )

        private val animalList = listOf(
            "얼룩말", "코알라", "기린",
        )
    }
}
