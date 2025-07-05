package com.bff.wespot.message.state.room

sealed interface RoomSideEffect {
    data object NavigateToMessageWriteScreen : RoomSideEffect
    data object NavigateUp : RoomSideEffect
    data object ShowMessageDeleteConfirmModal : RoomSideEffect
    data object CloseMessageDeleteConfirmModal : RoomSideEffect
}
