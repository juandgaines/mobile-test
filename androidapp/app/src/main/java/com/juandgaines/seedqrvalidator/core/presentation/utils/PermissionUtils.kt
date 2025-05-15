package com.juandgaines.seedqrvalidator.core.presentation.utils

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat

fun ComponentActivity.shouldShowCameraPermissionRationale(): Boolean {
    return shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)
}

private fun Context.hasPermission(permission:String):Boolean{
    return ContextCompat.checkSelfPermission(
        this,
        permission
    ) == android.content.pm.PackageManager.PERMISSION_GRANTED
}


fun Context.hasCameraPermission(): Boolean {
    return hasPermission(android.Manifest.permission.CAMERA)
}