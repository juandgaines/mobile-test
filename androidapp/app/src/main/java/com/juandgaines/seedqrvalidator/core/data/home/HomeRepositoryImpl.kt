package com.juandgaines.seedqrvalidator.core.data.home

import com.juandgaines.seedqrvalidator.core.data.network.SeedApi
import com.juandgaines.seedqrvalidator.core.data.network.SeedDto
import com.juandgaines.seedqrvalidator.core.data.network.safeCall
import com.juandgaines.seedqrvalidator.core.data.network.toSeed
import com.juandgaines.seedqrvalidator.core.domain.HomeRepository
import com.juandgaines.seedqrvalidator.core.domain.Seed
import com.juandgaines.seedqrvalidator.core.domain.utils.DataError
import com.juandgaines.seedqrvalidator.core.domain.utils.Result
import com.juandgaines.seedqrvalidator.core.domain.utils.map
import com.juandgaines.seedqrvalidator.core.domain.utils.onError
import com.juandgaines.seedqrvalidator.core.domain.utils.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val seedApi:SeedApi
) : HomeRepository {
    override fun getScannedSeeds(): Flow<List<Seed>> = emptyFlow<List<Seed>>()


    override suspend fun getSeed(): Result<Seed, DataError.Network> = safeCall<SeedDto> {
        seedApi.getSeed()
    }.map { seedDto->
        seedDto.toSeed()
    }.onSuccess {
        //TOdo: Handle success case
    }.onError { error ->
       // Handle error case
    }

}