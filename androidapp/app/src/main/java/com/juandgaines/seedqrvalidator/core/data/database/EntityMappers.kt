package com.juandgaines.seedqrvalidator.core.data.database

import com.juandgaines.seedqrvalidator.core.domain.Seed

fun SeedEntity.toSeed()= Seed(
    seed = seed,
    expiresAt = expirationTime
)


fun Seed.toSeedEntity() = SeedEntity(
    seed = seed,
    expirationTime = expiresAt
)
