package com.juandgaines.seedqrvalidator.core.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SeedDao{
    @Query
    ("SELECT * FROM seeds")
    fun getAllSeeds(): Flow<List<SeedEntity>>

    @Query
        ("SELECT * FROM seeds WHERE seed = :seed")
    suspend fun getSeedById(seed: String): SeedEntity?

    @Upsert
    suspend fun upsertSeed(seed: SeedEntity)

}