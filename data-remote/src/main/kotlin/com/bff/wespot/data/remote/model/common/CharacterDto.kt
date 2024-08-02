package com.bff.wespot.data.remote.model.common

import com.bff.wespot.model.common.Character
import com.bff.wespot.model.common.CharacterList
import kotlinx.serialization.Serializable

@Serializable
data class CharacterDto(
    val id: Int,
    val name: String,
    val iconUrl: String,
) {
    fun toCharacter() = Character(
        id = id,
        name = name,
        iconUrl = iconUrl,
    )
}

@Serializable
data class CharacterListDto(
    val characters: List<CharacterDto>
) {
    fun toCharacterList() = CharacterList(
        characters = characters.map { it.toCharacter() }
    )
}