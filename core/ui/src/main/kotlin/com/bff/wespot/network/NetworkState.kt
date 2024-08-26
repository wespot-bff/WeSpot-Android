package com.bff.wespot.network

sealed class NetworkState {
    data object None: NetworkState()
    data object Connected: NetworkState()
    data object NotConnected: NetworkState()
}