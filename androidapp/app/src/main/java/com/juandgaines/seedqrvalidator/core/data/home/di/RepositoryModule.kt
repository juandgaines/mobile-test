package com.juandgaines.seedqrvalidator.core.data.home.di

import com.juandgaines.seedqrvalidator.core.data.home.HomeRepositoryImpl
import com.juandgaines.seedqrvalidator.core.data.network.SeedApi
import com.juandgaines.seedqrvalidator.core.domain.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    fun provideHomeRepository(
        seedApi: SeedApi
    ): HomeRepository {
        return HomeRepositoryImpl(
        seedApi = seedApi
        )
    }
}