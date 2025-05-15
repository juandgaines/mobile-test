package com.juandgaines.seedqrvalidator.home.presentation

import com.juandgaines.seedqrvalidator.core.domain.Seed

data class HomeState(
    val seedList:List<Seed> = emptyList(),
    val isMenuVisible:Boolean = false,
)