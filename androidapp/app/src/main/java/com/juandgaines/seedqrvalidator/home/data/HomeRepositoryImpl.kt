package com.juandgaines.seedqrvalidator.home.data

import com.juandgaines.seedqrvalidator.core.data.database.SeedDao
import com.juandgaines.seedqrvalidator.core.data.database.toSeed
import com.juandgaines.seedqrvalidator.core.domain.Seed
import com.juandgaines.seedqrvalidator.home.domain.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val seedDao: SeedDao
) : HomeRepository {

    override fun getScannedSeeds(): Flow<List<Seed>> = seedDao.getAllSeeds().map {
        it.map { seedEntity->
            seedEntity.toSeed()
        }
    }

}