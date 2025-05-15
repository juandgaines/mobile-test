@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.juandgaines.seedqrvalidator.scanner.presentation

import android.Manifest
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
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
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.juandgaines.seedqrvalidator.R
import com.juandgaines.seedqrvalidator.core.presentation.navigation.Destinations
import com.juandgaines.seedqrvalidator.core.presentation.utils.hasCameraPermission
import com.juandgaines.seedqrvalidator.core.presentation.utils.shouldShowCameraPermissionRationale

@ExperimentalGetImage
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

@ExperimentalGetImage
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
                state.permissionGranted ->{
                    CameraScreen(
                        onPhotoTaken = { imageProxy ->
                            scanForQrCode(imageProxy, onEvent)
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


@androidx.annotation.OptIn(ExperimentalGetImage::class)
private fun scanForQrCode(
    imageProxy: ImageProxy,
    onEvent: (ScannerIntent) -> Unit
) {
    val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE,
            Barcode.FORMAT_AZTEC
        )
        .build()

    val scanner = BarcodeScanning.getClient(options)

    try {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(
                mediaImage,
                imageProxy.imageInfo.rotationDegrees
            )
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    val barcode = barcodes.firstOrNull()
                    barcode?.rawValue?.let {
                        onEvent(
                            ScannerIntent.BarcodeDetected(it)
                        )
                    }
                }
                .addOnFailureListener { e ->
                    onEvent(
                        ScannerIntent.ErrorScanning
                    )
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    } catch (e: Exception) {
        onEvent(
            ScannerIntent.ErrorScanning
        )

        imageProxy.close()
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

