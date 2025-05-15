package com.juandgaines.seedqrvalidator.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        SeedEntity::class,
    ], version = 1)
abstract class SeedDataBase : RoomDatabase() {
    abstract fun seedDao(): SeedDao
}