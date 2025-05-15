package com.juandgaines.seedqrvalidator.core.data.network

import com.juandgaines.seedqrvalidator.core.domain.Seed

fun SeedDto.toSeed() =Seed(
    seed = seed,
    expiresAt = expiresAt
)

fun Seed.toSeedDto()= SeedDto(
    seed = seed,
    expiresAt = expiresAt
)
