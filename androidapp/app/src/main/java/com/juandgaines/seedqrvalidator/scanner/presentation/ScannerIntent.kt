package com.juandgaines.seedqrvalidator.scanner.presentation

sealed interface ScannerIntent {
    data class TakenPicture(val data:ByteArray) : ScannerIntent {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as TakenPicture

            return data.contentEquals(other.data)
        }

        override fun hashCode(): Int {
            return data.contentHashCode()
        }
    }

    data class SubmitCameraPermissionInfo(
        val acceptedCameraPermission: Boolean,
        val showCameraRationale: Boolean
    ) : ScannerIntent

    data object ProcessPhoto: ScannerIntent
    data object CancelPreview: ScannerIntent
}