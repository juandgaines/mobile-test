package com.juandgaines.seedqrvalidator.home.data

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.juandgaines.seedqrvalidator.core.data.database.SeedEntity
import com.juandgaines.seedqrvalidator.core.domain.Seed
import com.juandgaines.seedqrvalidator.utils.SeedDaoFake
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class HomeRepositoryImplTest {
    
    private lateinit var seedDao: SeedDaoFake
    private lateinit var repository: HomeRepositoryImpl

    @Before
    fun setup() {
        seedDao = SeedDaoFake()
        repository = HomeRepositoryImpl(seedDao)
    }

    @Test
    fun `getScannedSeeds emits empty list when database is empty`() = runTest {
        seedDao.clear()
        repository.getScannedSeeds().test {
            val emission = awaitItem()
            assertThat(emission).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getScannedSeeds emits seeds in correct order when database has items`() = runTest {
        // Arrange - Add seeds in specific order
        seedDao.clear()
        val seed1 = SeedEntity(
            id = 1,
            seed = "seed1",
            type = 1,
            expirationTime = "2024-03-15T10:00:00Z"
        )
        val seed2 = SeedEntity(
            id = 2,
            seed = "seed2",
            type = 1,
            expirationTime = "2024-03-16T10:00:00Z"
        )
        val seed3 = SeedEntity(
            id = 3,
            seed = "seed3",
            type = 1,
            expirationTime = "2024-03-17T10:00:00Z"
        )

        repository.getScannedSeeds().test {

            val emission1 = awaitItem()
            assertThat(emission1).isEmpty()

            seedDao.upsertSeed(seed2)
            seedDao.upsertSeed(seed3)
            seedDao.upsertSeed(seed1)

            val emission2 = awaitItem()
            assertThat(emission2).isNotEmpty()
            assertThat(emission2).hasSize(1)

            val emission3 = awaitItem()
            assertThat(emission3).isNotEmpty()
            assertThat(emission3).hasSize(2)

            val emission4 = awaitItem()
            assertThat(emission4).isNotEmpty()
            assertThat(emission4).hasSize(3)

            assertThat(emission4).containsExactly(
                Seed(seed2.seed, seed2.expirationTime),
                Seed(seed3.seed, seed3.expirationTime),
                Seed(seed1.seed, seed1.expirationTime)
            ).inOrder()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getScannedSeeds emits updated list when new seed is added`() = runTest {
        seedDao.clear()
        repository.getScannedSeeds().test {

            assertThat(awaitItem()).isEmpty()

            val seed1 = SeedEntity(
                id = 1,
                seed = "seed1",
                type = 1,
                expirationTime = "2024-03-15T10:00:00Z"
            )
            seedDao.upsertSeed(seed1)

            val updatedEmission = awaitItem()
            assertThat(updatedEmission).hasSize(1)
            assertThat(updatedEmission.first()).isEqualTo(
                Seed(seed1.seed, seed1.expirationTime)
            )

            val seed2 = SeedEntity(
                id = 2,
                seed = "seed2",
                type = 1,
                expirationTime = "2024-03-16T10:00:00Z"
            )
            seedDao.upsertSeed(seed2)

            val finalEmission = awaitItem()
            assertThat(finalEmission).hasSize(2)
            assertThat(finalEmission).containsExactly(
                Seed(seed1.seed, seed1.expirationTime),
                Seed(seed2.seed, seed2.expirationTime)
            ).inOrder()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getScannedSeeds updates when seed is modified`() = runTest {
        seedDao.clear()
        val initialSeed = SeedEntity(
            id = 1,
            seed = "seed1",
            type = 1,
            expirationTime = "2024-03-15T10:00:00Z"
        )


        repository.getScannedSeeds().test {
            awaitItem()
            seedDao.upsertSeed(initialSeed)
            val firstEmission = awaitItem()
            assertThat(firstEmission).hasSize(1)
            assertThat(firstEmission.first().seed).isEqualTo("seed1")


            val updatedSeed = initialSeed.copy(
                expirationTime = "2024-03-16T10:00:00Z"
            )
            seedDao.upsertSeed(updatedSeed)

            val updatedEmission = awaitItem()
            assertThat(updatedEmission).hasSize(1)
            assertThat(updatedEmission.first().expiresAt)
                .isEqualTo("2024-03-16T10:00:00Z")

            cancelAndIgnoreRemainingEvents()
        }
    }
}