package com.juandgaines.seedqrvalidator.scanner.data.di

import com.juandgaines.seedqrvalidator.core.data.network.SeedApi
import com.juandgaines.seedqrvalidator.scanner.data.ScannerRepositoryImpl
import com.juandgaines.seedqrvalidator.scanner.domain.ScannerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ScannerRepositoryModule {
    @Provides
    fun provideScannerRepository(
        seedApi: SeedApi
    ): ScannerRepository {
        return ScannerRepositoryImpl(
            seedApi = seedApi
        )
    }
}