package com.juandgaines.seedqrvalidator.home.presentation

sealed interface HomeIntent{
    data object ScanQrIntent: HomeIntent
    data object GenerateQrIntent: HomeIntent
    data class OpenExistingQrIntent(val seed:String): HomeIntent
    data object ExpandFabMenu: HomeIntent
    data object CollapseFabMenu: HomeIntent
}