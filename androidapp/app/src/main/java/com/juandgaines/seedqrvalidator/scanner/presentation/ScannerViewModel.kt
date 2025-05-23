package com.juandgaines.seedqrvalidator.scanner.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juandgaines.seedqrvalidator.R
import com.juandgaines.seedqrvalidator.core.domain.utils.DataError
import com.juandgaines.seedqrvalidator.core.domain.utils.onError
import com.juandgaines.seedqrvalidator.core.domain.utils.onSuccess
import com.juandgaines.seedqrvalidator.scanner.domain.ScannerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val scannerRepository: ScannerRepository
): ViewModel(){


    private val _eventChannel = Channel<ScannerEvents>()
    val eventsScanner = _eventChannel.receiveAsFlow()


    private val _scannerState = MutableStateFlow(ScannerState())
    private var latestScannedValue:String = ""
    private var job:Job? = null

    val scannerState = _scannerState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ScannerState()
        )

    fun onAction(intent:ScannerIntent){
        viewModelScope.launch {
            when(intent){
                is ScannerIntent.SubmitCameraPermissionInfo -> {
                    _scannerState.value = _scannerState.value.copy(
                        permissionGranted = intent.acceptedCameraPermission,
                        showCameraRationale = intent.showCameraRationale
                    )
                }
                is ScannerIntent.BarcodeDetected -> {
                    if (intent.value == latestScannedValue) {
                        job?.cancel()
                        job = viewModelScope.launch {
                            delay(3000)
                            latestScannedValue = ""
                        }
                        return@launch
                    }
                    latestScannedValue = intent.value
                    scannerRepository.validateSeed(intent.value)
                        .onSuccess {
                        _eventChannel.send(
                            ScannerEvents.ShowMessageSuccess(
                               messageRes = R.string.valid_seed
                            )
                        )
                    }.onError { error->
                        when(error){
                            DataError.Network.REQUEST_TIMEOUT,
                            DataError.Network.NO_INTERNET -> {
                                ScannerEvents.ShowMessageError(
                                    messageRes = R.string.error_internet
                                )
                            }
                            DataError.Network.NOT_FOUND -> {
                                _eventChannel.send(
                                    ScannerEvents.ShowMessageError(
                                        messageRes = R.string.invalid_seed
                                    )
                                )
                            }
                            else-> {
                                _eventChannel.send(
                                    ScannerEvents.ShowMessageError(
                                        messageRes = R.string.message_error
                                    )
                                )
                            }
                        }
                    }
                }

                is ScannerIntent.ErrorScanning -> {
                    _eventChannel.send(
                        ScannerEvents.ShowMessageError(
                            messageRes = R.string.message_error
                        )
                    )

                }
            }
        }
    }

}