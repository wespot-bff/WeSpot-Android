package com.bff.wespot.community.state

sealed interface CommunitySideEffect {
    data object NavigateToWriteActivity : CommunitySideEffect
}
