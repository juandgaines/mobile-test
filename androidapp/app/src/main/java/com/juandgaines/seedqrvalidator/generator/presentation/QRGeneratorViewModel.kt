package com.juandgaines.seedqrvalidator.generator.presentation

import com.juandgaines.seedqrvalidator.home.domain.HomeRepository
import javax.inject.Inject

class QRGeneratorViewModel @Inject constructor(
    private val seedRepository: HomeRepository
){
}