package com.teakave.pinscreen.ui.pin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teakave.pinscreen.ui.main.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PinViewModel : ViewModel() {

    private val _loginUiState = MutableStateFlow<PinUiState>(PinUiState.Empty)
    val loginUiState: StateFlow<PinUiState> = _loginUiState

    private var pinInput: String = ""
    private var showPin = false

    fun removePinClicked() {
        pinInput = pinInput.dropLast(1)
        updateUiState(PinUiState.RemovedPin(pinInput, showPin))
    }

    fun showPinClicked() {
        showPin = !showPin
        updateUiState(
            if (showPin) {
                PinUiState.ShowPin(pinInput)
            } else {
                PinUiState.HidePin(pinInput)
            }
        )
    }

    fun numberClicked(number: Int?) =
        number?.let {
            pinInput += it
            if (pinInput.length == Constants.PIN_LENGTH) {
                updateUiState(PinUiState.Loading(pinInput, showPin))
                validatePin()
            } else {
                updateUiState(PinUiState.AddedPin(pinInput, showPin))
            }
        }

    private fun validatePin() =
        viewModelScope.launch {
            if (isPinValid()) {
                pinInput = ""
                updateUiState(PinUiState.LoginSuccess(showPin))
            } else {
                pinInput = ""
                updateUiState(PinUiState.LoginError(showPin))
            }
        }

    private suspend fun isPinValid(): Boolean {
        // todo: implement isPinValid() functionality
        delay(500L)
        return pinInput == "1234"
    }

    private fun updateUiState(uiState: PinUiState) =
        viewModelScope.launch {
            _loginUiState.emit(uiState)
        }

}
