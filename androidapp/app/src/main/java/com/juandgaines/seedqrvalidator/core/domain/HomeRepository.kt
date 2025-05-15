package com.juandgaines.seedqrvalidator.core.domain
import com.juandgaines.seedqrvalidator.core.domain.utils.DataError
import com.juandgaines.seedqrvalidator.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow

interface HomeRepository{
    fun getScannedSeeds(): Flow<List<Seed>>
    suspend fun getSeed():Result<Seed, DataError.Network>
}