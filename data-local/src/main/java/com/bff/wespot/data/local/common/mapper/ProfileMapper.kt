package com.bff.wespot.data.local.common.mapper

import com.bff.wespot.data.local.ProfilePreference
import com.bff.wespot.model.user.response.Profile
import com.bff.wespot.model.user.response.ProfileCharacter

fun ProfilePreference.toProfile(): Profile = Profile(
    id = this.id,
    name = this.name,
    schoolName = this.schoolName,
    grade = this.grade,
    classNumber = this.classNumber,
    gender = this.gender,
    introduction = this.introduction,
    profileCharacter = ProfileCharacter(
        this.profileCharacter.iconUrl,
        this.profileCharacter.backgroundColor,
    ),
)
