package com.juandgaines.seedqrvalidator.scanner.presentation

interface ScannerEvents {
    data class ShowMessageSuccess(val messageRes: Int) : ScannerEvents
    data class ShowMessageError(val messageRes: Int) : ScannerEvents
}