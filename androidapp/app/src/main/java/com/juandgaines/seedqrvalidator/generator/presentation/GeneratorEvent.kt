package com.juandgaines.seedqrvalidator.generator.presentation

sealed interface GeneratorEvent {
    data object NavigateBack:GeneratorEvent
}