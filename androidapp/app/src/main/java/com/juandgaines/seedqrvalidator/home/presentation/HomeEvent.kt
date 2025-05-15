package com.juandgaines.seedqrvalidator.home.presentation

sealed interface HomeEvent{
    data object NavigateToScan: HomeEvent
    data object NavigateToQr: HomeEvent
}