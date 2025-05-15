package com.juandgaines.seedqrvalidator.generator.presentation

sealed interface GeneratorIntent {
    data object NavigateBack : GeneratorIntent
    data object Retry : GeneratorIntent
}