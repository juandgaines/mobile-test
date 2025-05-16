package com.juandgaines.seedqrvalidator.scanner.presentation

import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

@Composable
fun CameraScreen(
    onEvent: (ScannerIntent) -> Unit,
) {
    val localContext = LocalContext.current

    val controller = remember {
        LifecycleCameraController(localContext)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CameraPreview(
            controller = controller,
            modifier = Modifier.fillMaxSize(),
            onEvent = onEvent
        )
        Box (
            modifier = Modifier.align(Alignment.Center)
                .background(
                    color = Color.Transparent
                )
                .size(300.dp)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primaryContainer
                )
        )
    }
}