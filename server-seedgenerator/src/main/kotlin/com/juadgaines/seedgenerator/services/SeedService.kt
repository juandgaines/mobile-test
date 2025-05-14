package com.juadgaines.seedgenerator.services

import com.juadgaines.seedgenerator.domain.SeedDto
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Service
class SeedService {
    private val seedStorage: MutableMap<String, ZonedDateTime> = ConcurrentHashMap()

    fun generateSeed(): SeedDto {
        val seedValue = UUID.randomUUID().toString().replace("-", "")
        val expiration = ZonedDateTime.now().plusMinutes(5)
        seedStorage[seedValue] = expiration
        return SeedDto(seed = seedValue, expires_at = expiration)
    }

    fun validateSeed(seed: String): Boolean {
        val generatedSeedByServer = seedStorage[seed]
        return generatedSeedByServer != null && ZonedDateTime.now().isBefore(generatedSeedByServer)
    }

    fun clearSeeds(){
        seedStorage.clear()
    }

}