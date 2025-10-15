package com.bff.wespot.community.write.state

sealed interface WritePostSideEffect {
    data object ClosePage : WritePostSideEffect
}
