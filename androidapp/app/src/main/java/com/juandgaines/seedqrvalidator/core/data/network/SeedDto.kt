package com.juandgaines.seedqrvalidator.core.data.network

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class SeedDto(
    val seed: String,
    @JsonNames("expires_at")
    val expiresAt: String
)