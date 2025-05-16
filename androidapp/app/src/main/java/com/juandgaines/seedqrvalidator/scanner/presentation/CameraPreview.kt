package com.juandgaines.seedqrvalidator.scanner.presentation

import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

@OptIn(ExperimentalGetImage::class)
@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
    onEvent: (ScannerIntent) -> Unit
){

    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        factory = { context->
            val previewView = PreviewView(context)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().apply {
                    surfaceProvider = previewView.surfaceProvider
                }

                val scanner = BarcodeScanning.getClient()
                val analysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                analysis.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                    val mediaImage = imageProxy.image
                    if (mediaImage != null) {
                        val inputImage = InputImage.fromMediaImage(
                            mediaImage,
                            imageProxy.imageInfo.rotationDegrees
                        )

                        scanner.process(inputImage)
                            .addOnSuccessListener { barcodes ->
                                if (barcodes.isNotEmpty()){
                                    val barcode = barcodes.firstOrNull()
                                    barcode?.rawValue?.let {
                                        onEvent(ScannerIntent.BarcodeDetected(it))
                                    }

                                }
                            }
                            .addOnFailureListener { e ->
                                onEvent(ScannerIntent.ErrorScanning)
                            }
                            .addOnCompleteListener {
                                imageProxy.close()
                            }
                    } else {
                        onEvent(ScannerIntent.ErrorScanning)
                        imageProxy.close()
                    }
                }

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    analysis
                )
            }, ContextCompat.getMainExecutor(context))

            previewView
        },
        modifier = modifier
    )
}