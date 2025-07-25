package com.bff.wespot.main.model

enum class BarType {
    DEFAULT,
    ENTIRE,
    MESSAGE,
    NONE,
    COMMUNITY,
    ;

    fun shouldShowMainLogo(): Boolean =
        this in listOf(DEFAULT, MESSAGE)
}
