package com.juandgaines.seedqrvalidator.scanner.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(

): ViewModel(){

    private val _currentPreviewPhoto = MutableStateFlow<ByteArray?>(null)

    private val _scannerState = MutableStateFlow(ScannerState())

    val scannerState = _scannerState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ScannerState()
        )

    fun onAction(intent:ScannerIntent){
        viewModelScope.launch {
            when(intent){
                ScannerIntent.CancelPreview -> onCancelPicturePreview()
                ScannerIntent.ProcessPhoto -> TODO()
                is ScannerIntent.SubmitCameraPermissionInfo -> TODO()
                is ScannerIntent.TakenPicture -> onPhotoForPreview(intent.data)
            }
        }
    }


    suspend fun onCancelPicturePreview() {
        _currentPreviewPhoto.value = null
    }

    private fun onPhotoForPreview(photoBytes: ByteArray) {
        _currentPreviewPhoto.value = photoBytes
    }
}