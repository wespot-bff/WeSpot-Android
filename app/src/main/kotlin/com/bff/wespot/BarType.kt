package com.bff.wespot

enum class BarType {
    DEFAULT,
    ENTIRE,
    MESSAGE,
    NONE;

    fun shouldShowMainLogo(): Boolean =
        this in listOf(DEFAULT, MESSAGE)
}
