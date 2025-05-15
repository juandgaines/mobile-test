package com.juandgaines.seedqrvalidator.scanner.presentation

interface ScannerEvents {
    data class ShowMessage(val messageRes: Int) : ScannerEvents
}