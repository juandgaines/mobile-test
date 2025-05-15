package com.juandgaines.seedqrvalidator.home.presentation

sealed interface HomeIntent{
    data object ScanQrIntent: HomeIntent
    data object GenerateQrIntent: HomeIntent
}