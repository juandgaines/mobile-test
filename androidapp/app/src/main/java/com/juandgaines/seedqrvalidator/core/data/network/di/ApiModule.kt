package com.juandgaines.seedqrvalidator.core.data.network.di

import com.juandgaines.seedqrvalidator.core.data.network.SeedApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    @Singleton
    fun provideSeedApi(
        retrofit: Retrofit
    ): SeedApi {
        return retrofit.create(SeedApi::class.java)
    }
}