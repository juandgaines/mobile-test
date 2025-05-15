package com.juandgaines.seedqrvalidator.scanner.presentation

import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

@Composable
fun CameraScreen(
    onPhotoTaken: (ImageProxy) -> Unit
) {
    val localContext = LocalContext.current

    val controller = remember {
        LifecycleCameraController(localContext).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CameraPreview(
            controller = controller,
            modifier = Modifier.fillMaxSize()
        )

        IconButton(
            onClick = {
                controller.cameraSelector = if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                    CameraSelector.DEFAULT_FRONT_CAMERA
                } else {
                    CameraSelector.DEFAULT_BACK_CAMERA
                }
            },
            modifier = Modifier.offset(16.dp, 36.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Cameraswitch,
                contentDescription = "Switch camera"
            )
        }

        IconButton(
            onClick = {
                takePhoto(
                    context = localContext,
                    controller = controller,
                    onPhotoTaken = onPhotoTaken
                )
            },
            modifier = Modifier.align(
                alignment = Alignment.BottomCenter
            )
        ) {
            Icon(
                imageVector = Icons.Filled.PhotoCamera,
                contentDescription = "Take photo"
            )
        }
    }
}
private fun takePhoto(
    context: Context,
    controller: LifecycleCameraController,
    onPhotoTaken: (ImageProxy) -> Unit
) {
    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                onPhotoTaken(image)
                image.close()
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("CameraScreen", "Error taking picture", exception)
            }
        }
    )
}