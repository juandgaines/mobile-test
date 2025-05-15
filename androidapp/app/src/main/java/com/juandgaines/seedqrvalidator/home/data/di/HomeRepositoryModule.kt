package com.juandgaines.seedqrvalidator.home.data.di

import com.juandgaines.seedqrvalidator.core.data.database.SeedDao
import com.juandgaines.seedqrvalidator.home.data.HomeRepositoryImpl
import com.juandgaines.seedqrvalidator.core.data.network.SeedApi
import com.juandgaines.seedqrvalidator.home.domain.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class HomeRepositoryModule {
    @Provides
    fun provideHomeRepository(
        seedDao:SeedDao
    ): HomeRepository {
        return HomeRepositoryImpl(
            seedDao = seedDao
        )
    }
}