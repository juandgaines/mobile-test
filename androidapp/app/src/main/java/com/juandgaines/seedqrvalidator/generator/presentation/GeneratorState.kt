package com.juandgaines.seedqrvalidator.generator.presentation

data class GeneratorState(
    val seed: String= "",
    val remainingTime: String = "",
    val hasExpired:Boolean =false,
    val isLoading: Boolean = true,
    val error: Int? = null,
)