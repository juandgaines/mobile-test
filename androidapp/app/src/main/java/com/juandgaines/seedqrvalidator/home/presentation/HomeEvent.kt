package com.juandgaines.seedqrvalidator.home.presentation

sealed interface HomeEvent{
    data object NavigateToScan: HomeEvent
    data class NavigateToQr(val seed:String?): HomeEvent
}