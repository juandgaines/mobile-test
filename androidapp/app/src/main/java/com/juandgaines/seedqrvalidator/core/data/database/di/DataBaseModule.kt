package com.juandgaines.seedqrvalidator.core.data.database.di

import android.content.Context
import androidx.room.Room
import com.juandgaines.seedqrvalidator.core.data.database.SeedDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(
        @ApplicationContext context: Context,
    ): SeedDataBase {
        return Room.databaseBuilder(
            context,
            SeedDataBase::class.java,
            "seeds.db"
        ).build()
    }
}