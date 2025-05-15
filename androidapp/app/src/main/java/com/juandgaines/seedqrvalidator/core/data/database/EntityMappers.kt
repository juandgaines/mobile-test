package com.juandgaines.seedqrvalidator.core.data.database

import com.juandgaines.seedqrvalidator.core.domain.Seed

fun SeedEntity.toSeed():Seed{
    return Seed(
        seed = seed,
        expiresAt = expirationTime
    )
}

fun Seed.toSeedEntity():SeedEntity{
    return SeedEntity(
        seed = seed,
        expirationTime = expiresAt
    )
}