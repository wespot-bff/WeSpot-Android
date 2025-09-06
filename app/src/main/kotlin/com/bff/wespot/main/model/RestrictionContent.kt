package com.bff.wespot.main.model

import androidx.annotation.StringRes

internal enum class RestrictionContent(
    @StringRes val title: Int,
    @StringRes val body1: Int,
    @StringRes val body2: Int,
    @StringRes val body3: Int,
    @StringRes val body4: Int? = null,
    val buttonNumber: Int = 1,
) {
    TYPE1(
        com.bff.wespot.R.string.restriction_title,
        com.bff.wespot.R.string.restriction_type1_body1,
        com.bff.wespot.R.string.restriction_type1_body2,
        com.bff.wespot.R.string.restriction_type1_body3,
        com.bff.wespot.R.string.restriction_type1_body4,
    ),

    TYPE2(
        com.bff.wespot.R.string.restriction_title,
        com.bff.wespot.R.string.restriction_type1_body1,
        com.bff.wespot.R.string.restriction_type2_body2,
        com.bff.wespot.R.string.restriction_type2_body3,
    ),

    TYPE3(
        com.bff.wespot.R.string.restriction_title,
        com.bff.wespot.R.string.restriction_type1_body1,
        com.bff.wespot.R.string.restriction_type3_body2,
        com.bff.wespot.R.string.restriction_type3_body3,
        null,
        2,
    ),
}
