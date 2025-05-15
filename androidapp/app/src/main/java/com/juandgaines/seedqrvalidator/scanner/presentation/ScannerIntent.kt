package com.juandgaines.seedqrvalidator.scanner.presentation

sealed interface ScannerIntent {

    data class SubmitCameraPermissionInfo(
        val acceptedCameraPermission: Boolean,
        val showCameraRationale: Boolean
    ) : ScannerIntent

    data class BarcodeDetected(val value: String) : ScannerIntent
    data object ErrorScanning : ScannerIntent
}