package com.juandgaines.seedqrvalidator.core.data.database.di

import com.juandgaines.seedqrvalidator.core.data.database.SeedDao
import com.juandgaines.seedqrvalidator.core.data.database.SeedDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {

    @Provides
    @Singleton
    fun provideSeedDao(
        database: SeedDataBase
    ): SeedDao {
        return database.seedDao()
    }
}