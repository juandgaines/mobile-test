package com.juandgaines.seedqrvalidator.core.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SeedApi {

    @GET("/v1/seed")
    suspend fun getSeed(): Response<SeedDto>

    @GET("/v1/seed/validate/{seed}")
    suspend fun validateSeed(
        @Path("seed")
        seed: String
    ): Response<Unit?>
}