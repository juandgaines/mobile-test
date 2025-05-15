package com.juandgaines.seedqrvalidator.scanner.domain

import com.juandgaines.seedqrvalidator.core.domain.utils.DataError
import com.juandgaines.seedqrvalidator.core.domain.utils.Error
import com.juandgaines.seedqrvalidator.core.domain.utils.Result

interface ScannerRepository{
    suspend fun validateSeed(seed:String):Result<Unit,DataError.Network>
}