package com.bff.wespot.auth.state

import androidx.paging.PagingData
import com.bff.wespot.model.auth.response.School
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID

data class AuthUiState(
    val schoolName: String = "",
    val schoolList: Flow<PagingData<School>> = flow { },
    val selectedSchool: School? = null,
    val grade: Int = -1,
    val gradeBottomSheet: Boolean = true,
    val classNumber: Int = -1,
    val gender: String = "",
    val name: String = "",
    val loading: Boolean = false,
    val consents: List<Boolean> = listOf(false, false, false, false),
    val hasProfanity: Boolean = false,
    val playStoreLink: String,
    val termsOfServiceLink: String,
    val privacyPolicyLink: String,
    val schoolForm: String,
    val marketingLink: String,
    val uuid: String = UUID.randomUUID().toString(),
)
