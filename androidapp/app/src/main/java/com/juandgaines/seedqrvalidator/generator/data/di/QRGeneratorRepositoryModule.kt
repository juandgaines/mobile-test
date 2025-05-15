package com.juandgaines.seedqrvalidator.generator.data.di

import com.juandgaines.seedqrvalidator.core.data.network.SeedApi
import com.juandgaines.seedqrvalidator.generator.data.QRGeneratorRepositoryImpl
import com.juandgaines.seedqrvalidator.generator.domain.QrGeneratorRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class QRGeneratorRepositoryModule {
    @Provides
    fun provideHomeRepository(
        seedApi: SeedApi
    ): QrGeneratorRepository {
        return QRGeneratorRepositoryImpl(
            seedApi = seedApi
        )
    }
}