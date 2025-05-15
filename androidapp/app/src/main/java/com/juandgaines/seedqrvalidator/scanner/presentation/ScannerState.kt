package com.juandgaines.seedqrvalidator.scanner.presentation

data class ScannerState(
    val isInPreviewMode: Boolean = false,
    val photoToBeProcessed: ByteArray? = null,
    val showCameraRationale: Boolean = false,
    val permissionGranted: Boolean = false,
    val isLoading: Boolean = false,
    val isSeedFromBackend: Boolean = false,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ScannerState

        if (isInPreviewMode != other.isInPreviewMode) return false
        if (showCameraRationale != other.showCameraRationale) return false
        if (permissionGranted != other.permissionGranted) return false
        if (isLoading != other.isLoading) return false
        if (isSeedFromBackend != other.isSeedFromBackend) return false
        if (photoToBeProcessed != null) {
            if (other.photoToBeProcessed == null) return false
            if (!photoToBeProcessed.contentEquals(other.photoToBeProcessed)) return false
        } else if (other.photoToBeProcessed != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isInPreviewMode.hashCode()
        result = 31 * result + showCameraRationale.hashCode()
        result = 31 * result + permissionGranted.hashCode()
        result = 31 * result + isLoading.hashCode()
        result = 31 * result + isSeedFromBackend.hashCode()
        result = 31 * result + (photoToBeProcessed?.contentHashCode() ?: 0)
        return result
    }
}