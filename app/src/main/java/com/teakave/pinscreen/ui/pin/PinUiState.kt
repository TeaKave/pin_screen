package com.teakave.pinscreen.ui.pin

sealed class PinUiState {

    object Empty : PinUiState()
    data class Loading(val pin: String, val showPin: Boolean) : PinUiState()
    data class AddedPin(val pin: String, val showPin: Boolean) : PinUiState()
    data class RemovedPin(val pin: String, val showPin: Boolean) : PinUiState()
    data class LoginSuccess(val showPin: Boolean) : PinUiState()
    data class LoginError(val showPin: Boolean) : PinUiState()
    data class ShowPin(val pin: String) : PinUiState()
    data class HidePin(val pin: String) : PinUiState()

}
