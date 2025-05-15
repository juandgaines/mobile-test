package com.juandgaines.seedqrvalidator.core.data.network

import com.juandgaines.seedqrvalidator.core.domain.Seed

fun SeedDto.toSeed(): Seed {
    return Seed(
        seed = seed,
        expiresAt = expiresAt
    )
}

fun Seed.toSeedDto(): SeedDto {
    return SeedDto(
        seed = seed,
        expiresAt = expiresAt
    )
}