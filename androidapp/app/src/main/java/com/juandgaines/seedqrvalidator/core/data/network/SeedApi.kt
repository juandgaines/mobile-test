package com.juandgaines.seedqrvalidator.core.data.network

import retrofit2.Response
import retrofit2.http.GET

interface SeedApi {

    @GET("/v1/seed")
    suspend fun getSeed(): Response<SeedDto>
}