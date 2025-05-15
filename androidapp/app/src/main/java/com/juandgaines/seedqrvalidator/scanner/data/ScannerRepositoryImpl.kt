package com.juandgaines.seedqrvalidator.scanner.data

import com.juandgaines.seedqrvalidator.core.data.network.SeedApi
import com.juandgaines.seedqrvalidator.core.data.network.safeCall
import com.juandgaines.seedqrvalidator.core.domain.utils.DataError
import com.juandgaines.seedqrvalidator.core.domain.utils.Result
import com.juandgaines.seedqrvalidator.scanner.domain.ScannerRepository
import javax.inject.Inject

class ScannerRepositoryImpl @Inject constructor(
    private val seedApi: SeedApi
) : ScannerRepository {
    override suspend fun validateSeed(seed: String): Result<Unit, DataError.Network> = safeCall {
        seedApi.validateSeed(seed)
    }
}