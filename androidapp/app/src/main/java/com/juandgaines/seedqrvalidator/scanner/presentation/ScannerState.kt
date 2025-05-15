package com.juandgaines.seedqrvalidator.scanner.presentation

data class ScannerState(
    val showCameraRationale: Boolean = false,
    val permissionGranted: Boolean = false,
    val isLoading: Boolean = false,
)