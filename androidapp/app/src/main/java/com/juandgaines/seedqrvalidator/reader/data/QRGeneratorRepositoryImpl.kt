package com.juandgaines.seedqrvalidator.reader.data

import com.juandgaines.seedqrvalidator.core.data.network.SeedApi
import com.juandgaines.seedqrvalidator.core.data.network.SeedDto
import com.juandgaines.seedqrvalidator.core.data.network.safeCall
import com.juandgaines.seedqrvalidator.core.data.network.toSeed
import com.juandgaines.seedqrvalidator.core.domain.Seed
import com.juandgaines.seedqrvalidator.core.domain.utils.DataError
import com.juandgaines.seedqrvalidator.core.domain.utils.Result
import com.juandgaines.seedqrvalidator.core.domain.utils.map
import com.juandgaines.seedqrvalidator.core.domain.utils.onError
import com.juandgaines.seedqrvalidator.core.domain.utils.onSuccess
import com.juandgaines.seedqrvalidator.reader.domain.QrGeneratorRepository
import javax.inject.Inject

class QRGeneratorRepositoryImpl @Inject constructor(
    private val seedApi: SeedApi
) : QrGeneratorRepository {
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