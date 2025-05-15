package com.juandgaines.seedqrvalidator.generator.data

import android.database.sqlite.SQLiteException
import com.juandgaines.seedqrvalidator.core.data.database.SeedDao
import com.juandgaines.seedqrvalidator.core.data.database.toSeed
import com.juandgaines.seedqrvalidator.core.data.database.toSeedEntity
import com.juandgaines.seedqrvalidator.core.data.network.SeedApi
import com.juandgaines.seedqrvalidator.core.data.network.SeedDto
import com.juandgaines.seedqrvalidator.core.data.network.safeCall
import com.juandgaines.seedqrvalidator.core.data.network.toSeed
import com.juandgaines.seedqrvalidator.core.domain.Seed
import com.juandgaines.seedqrvalidator.core.domain.utils.DataError
import com.juandgaines.seedqrvalidator.core.domain.utils.Result
import com.juandgaines.seedqrvalidator.core.domain.utils.map
import com.juandgaines.seedqrvalidator.core.domain.utils.onSuccess
import com.juandgaines.seedqrvalidator.generator.domain.QrGeneratorRepository
import javax.inject.Inject

class QRGeneratorRepositoryImpl @Inject constructor(
    private val seedApi: SeedApi,
    private val seedDao: SeedDao
) : QrGeneratorRepository {
    override suspend fun getSeed(seed:String?): Result<Seed, DataError> {
        val response = seed?.let {
            return try {
                val localSeed= seedDao.getSeedById(it)?.toSeed()!!
                Result.Success(localSeed)
            }catch (e:SQLiteException){
                Result.Error(DataError.LocalError.UNKNOWN)
            }
        } ?: safeCall<SeedDto> {
            seedApi.getSeed()
        }.map { seedDto->
            seedDto.toSeed()
        }.onSuccess { seed->
            seedDao.upsertSeed(seed.toSeedEntity())
        }
        return response
    }
}