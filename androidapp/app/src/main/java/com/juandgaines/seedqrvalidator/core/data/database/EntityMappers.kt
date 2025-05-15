package com.juandgaines.seedqrvalidator.core.data.database

import com.juandgaines.seedqrvalidator.core.domain.Seed
import com.juandgaines.seedqrvalidator.core.domain.SeedType

fun SeedEntity.toSeed()= Seed(
    seed = seed,
    expiresAt = expirationTime,
    type = SeedType.fromInt(type)
)


fun Seed.toSeedEntity() = SeedEntity(
    seed = seed,
    expirationTime = expiresAt,
    type = type.toInt()
)
