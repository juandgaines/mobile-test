package com.juandgaines.seedqrvalidator.core.presentation.navigation

sealed interface Destinations {
    @kotlinx.serialization.Serializable
    data object HomeNav : Destinations

    @kotlinx.serialization.Serializable
    data object GeneratorQRNav : Destinations

    @kotlinx.serialization.Serializable
    data object ScannerNav : Destinations
}