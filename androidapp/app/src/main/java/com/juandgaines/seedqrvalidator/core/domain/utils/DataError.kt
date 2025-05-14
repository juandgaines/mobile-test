package com.juandgaines.seedqrvalidator.core.domain.utils

sealed interface DataError: Error {

    enum class Network: DataError {
        REQUEST_TIMEOUT,
        NO_INTERNET,
        SERIALIZATION,
        SERVER_ERROR,
        UNKNOWN
    }

    enum class LocalError: DataError {
        DISK_FULL,
        UNKNOWN
    }
}