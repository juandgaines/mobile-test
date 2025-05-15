package com.juandgaines.seedqrvalidator.home.domain
import com.juandgaines.seedqrvalidator.core.domain.Seed
import com.juandgaines.seedqrvalidator.core.domain.utils.DataError
import com.juandgaines.seedqrvalidator.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow

interface HomeRepository{
    fun getScannedSeeds(): Flow<List<Seed>>
}