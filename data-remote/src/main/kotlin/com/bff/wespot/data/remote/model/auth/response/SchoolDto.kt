package com.bff.wespot.data.remote.model.auth.response

import com.bff.wespot.model.auth.response.School
import kotlinx.serialization.Serializable

@Serializable
data class SchoolDto(
    val id: Int,
    val name: String,
    val address: String,
    val type: String,
) {
    fun toSchool(): School = School(
        id = id,
        name = name,
        address = address,
        type = type,
    )
}