package com.juadgaines.seedgenerator.controller

import com.juadgaines.seedgenerator.domain.SeedDto
import com.juadgaines.seedgenerator.services.SeedService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import sun.security.ec.ECDSAOperations

@RestController
@RequestMapping
class SeedController(private val seedService: SeedService) {

    @GetMapping("/seed")
    fun getSeed(): SeedDto {
        return seedService.generateSeed()
    }

    @GetMapping("/validate/{seed}")
    fun validateSeed(
        @PathVariable("seed") seed: String
    ): ResponseEntity<String> {
        return if (seedService.validateSeed(seed)) {
            ResponseEntity.ok("Seed is valid")
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid or expired seed")
        }
    }
}