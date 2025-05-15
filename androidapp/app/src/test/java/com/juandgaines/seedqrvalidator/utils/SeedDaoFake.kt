package com.juandgaines.seedqrvalidator.utils

import com.juandgaines.seedqrvalidator.core.data.database.SeedDao
import com.juandgaines.seedqrvalidator.core.data.database.SeedEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class SeedDaoFake : SeedDao {
    
    private val seeds = mutableListOf<SeedEntity>()
    private val seedsFlow = MutableStateFlow<List<SeedEntity>>(emptyList())
    
    override fun getAllSeeds(): Flow<List<SeedEntity>> = seedsFlow

    override suspend fun getSeedById(seed: String): SeedEntity? {
        return seeds.find { it.seed == seed }
    }

    override suspend fun upsertSeed(seed: SeedEntity) {
        val index = seeds.indexOfFirst { it.seed == seed.seed }
        if (index != -1) {
            seeds[index] = seed
        } else {
            seeds.add(seed)
        }
        seedsFlow.update { seeds.toList() }
    }

    fun clear() {
        seeds.clear()
        seedsFlow.update { emptyList() }
    }
}