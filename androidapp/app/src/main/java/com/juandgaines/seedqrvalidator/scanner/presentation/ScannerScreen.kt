package com.juandgaines.seedqrvalidator.scanner.presentation

import android.Manifest
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.juandgaines.seedqrvalidator.core.presentation.navigation.Destinations
import com.juandgaines.seedqrvalidator.core.presentation.utils.hasCameraPermission
import com.juandgaines.seedqrvalidator.core.presentation.utils.shouldShowCameraPermissionRationale

@Composable
fun ScannerScreenRoot(
    scannerScannerViewModel: ScannerViewModel,
    navigateToDestination: (Destinations) -> Unit,
    navigateUp: () -> Unit,
) {
    // Scanner screen implementation

    val scannerState by scannerScannerViewModel.scannerState.collectAsStateWithLifecycle()

    ScannerScreen(
        state = scannerState,
        onEvent = scannerScannerViewModel::onAction
    )
}

@Composable
fun ScannerScreen(
    state:ScannerState,
    onEvent: (ScannerIntent) -> Unit,
) {
    val context = LocalContext.current
    val activity = context as ComponentActivity
    val permissionLauncherCamera = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    )
    { _->

        val cameraRationale = activity.shouldShowCameraPermissionRationale()
        onEvent(
            ScannerIntent.SubmitCameraPermissionInfo(
                acceptedCameraPermission = activity.hasCameraPermission(),
                showCameraRationale = cameraRationale
            )
        )
    }

    LaunchedEffect(key1 = true) {
        val showCameraRationale = activity.shouldShowCameraPermissionRationale()

        onEvent(
            ScannerIntent.SubmitCameraPermissionInfo(
                acceptedCameraPermission = activity.shouldShowCameraPermissionRationale(),
                showCameraRationale = showCameraRationale
            )
        )

        if (!showCameraRationale) {
            permissionLauncherCamera.requestCameraScreenPermissions(context)
        }
    }
}

private fun ManagedActivityResultLauncher<String, Boolean>.requestCameraScreenPermissions(
    context: Context
) {
    val hasCameraPermission = context.hasCameraPermission()
    if (!hasCameraPermission) {
        launch(Manifest.permission.CAMERA)
    }
}

