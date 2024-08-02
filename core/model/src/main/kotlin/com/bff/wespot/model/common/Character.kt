package com.bff.wespot.model.common

data class Character(
    val id: Int,
    val name: String,
    val iconUrl: String,
)

data class CharacterList(
    val characters: List<Character>,
)
