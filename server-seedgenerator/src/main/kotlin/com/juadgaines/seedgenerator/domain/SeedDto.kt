package com.juadgaines.seedgenerator.domain

import java.time.ZonedDateTime

data class SeedDto(
    val seed: String,
    val expires_at: ZonedDateTime
)