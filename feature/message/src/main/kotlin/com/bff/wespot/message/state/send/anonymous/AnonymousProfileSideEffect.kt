package com.bff.wespot.message.state.send.anonymous

sealed interface AnonymousProfileSideEffect {
    data object OpenPicker : AnonymousProfileSideEffect
    data object ShowProfileOptionBottomSheet : AnonymousProfileSideEffect
    data object DismissProfileOptionBottomSheet : AnonymousProfileSideEffect
}
