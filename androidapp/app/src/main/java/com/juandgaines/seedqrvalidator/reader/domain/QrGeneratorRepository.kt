package com.juandgaines.seedqrvalidator.reader.domain
import com.juandgaines.seedqrvalidator.core.domain.Seed
import com.juandgaines.seedqrvalidator.core.domain.utils.DataError
import com.juandgaines.seedqrvalidator.core.domain.utils.Result

interface QrGeneratorRepository{
    suspend fun getSeed():Result<Seed, DataError.Network>
}