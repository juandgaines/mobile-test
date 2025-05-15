package com.juandgaines.seedqrvalidator.core.data.network

import com.juandgaines.seedqrvalidator.core.domain.Seed
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class SeedDto(
    val seed: String,
    @JsonNames("expires_at")
    val expiresAt: String
)