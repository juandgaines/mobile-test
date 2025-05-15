package com.juandgaines.seedqrvalidator.generator.domain
import com.juandgaines.seedqrvalidator.core.domain.Seed
import com.juandgaines.seedqrvalidator.core.domain.utils.DataError
import com.juandgaines.seedqrvalidator.core.domain.utils.Result

interface QrGeneratorRepository{
    suspend fun getSeed(seed:String?):Result<Seed, DataError>
}