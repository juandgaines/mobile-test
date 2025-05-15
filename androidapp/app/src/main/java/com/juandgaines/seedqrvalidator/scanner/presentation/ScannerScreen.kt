@file:OptIn(ExperimentalMaterial3Api::class)

package com.juandgaines.seedqrvalidator.scanner.presentation

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.juandgaines.seedqrvalidator.R
import com.juandgaines.seedqrvalidator.core.presentation.navigation.Destinations
import com.juandgaines.seedqrvalidator.core.presentation.utils.hasCameraPermission
import com.juandgaines.seedqrvalidator.core.presentation.utils.shouldShowCameraPermissionRationale
import com.juandgaines.seedqrvalidator.core.presentation.utils.toByteArray

@Composable
fun ScannerScreenRoot(
    scannerScannerViewModel: ScannerViewModel,
    navigateToDestination: (Destinations) -> Unit,
    navigateBack: () -> Unit,
) {


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
                acceptedCameraPermission = activity.hasCameraPermission(),
                showCameraRationale = showCameraRationale
            )
        )

        if (!showCameraRationale) {
            permissionLauncherCamera.requestCameraScreenPermissions(context)
        }
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text("Scanner")
                }
            )
        },
    ){ paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {

            when{
                state.isInPreviewMode && state.photoToBeProcessed != null->{
                    PhotoPreviewScreen(
                        photoBytes = state.photoToBeProcessed,
                        onAction = onEvent

                    )
                }
                state.permissionGranted ->{
                    CameraScreen(
                        onPhotoTaken = { imageProxy ->
                            val matrix = Matrix().apply {
                                postRotate(
                                    imageProxy.imageInfo.rotationDegrees.toFloat()
                                )
                            }
                            val rotatedBitmap = imageProxy.toBitmap().let {
                                Bitmap.createBitmap(it, 0, 0, it.width, it.height, matrix, true)
                            }
                            onEvent(
                                ScannerIntent.TakenPicture(
                                    rotatedBitmap.toByteArray()
                                )
                            )
                        }
                    )
                }
                else->{
                    Text(
                        text = stringResource(id = R.string.camera_permission_denied),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            PermissionRationaleDialogs(
                showCameraRationale = state.showCameraRationale,
                onAccept = {
                    permissionLauncherCamera.requestCameraScreenPermissions(context)
                },
                onDismiss = {
                    onEvent(
                        ScannerIntent.SubmitCameraPermissionInfo(
                            acceptedCameraPermission = activity.hasCameraPermission(),
                            showCameraRationale = false
                        )
                    )
                }
            )
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

