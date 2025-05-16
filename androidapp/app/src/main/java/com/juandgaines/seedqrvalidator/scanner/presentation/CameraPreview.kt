package com.juandgaines.seedqrvalidator.scanner.presentation

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

@OptIn(ExperimentalGetImage::class)
@Composable
fun CameraPreview(
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier,
    onEvent: (ScannerIntent) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        factory = { context ->
            PreviewView(context).apply {
                val options = BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(
                        Barcode.FORMAT_QR_CODE,
                        Barcode.FORMAT_CODABAR,
                        Barcode.FORMAT_CODE_93,
                        Barcode.FORMAT_CODE_39,
                        Barcode.FORMAT_CODE_128,
                        Barcode.FORMAT_EAN_8,
                        Barcode.FORMAT_EAN_13,
                        Barcode.FORMAT_AZTEC
                    )
                    .build()
                val barcodeScanner = BarcodeScanning.getClient(options)

                controller.setImageAnalysisAnalyzer(
                    ContextCompat.getMainExecutor(context),
                    MlKitAnalyzer(
                        listOf(barcodeScanner),
                        COORDINATE_SYSTEM_VIEW_REFERENCED,
                        ContextCompat.getMainExecutor(context)
                    ) { result: MlKitAnalyzer.Result? ->

                        result?.let {
                            val barcodes = it.getValue(barcodeScanner)
                            if (barcodes?.isNotEmpty()== true) {
                                val barcode = barcodes[0]
                                val rawValue = barcode.rawValue
                                if (rawValue != null) {
                                    onEvent(ScannerIntent.BarcodeDetected(rawValue))
                                }
                            }
                        }
                    }
                )
                controller.bindToLifecycle(lifecycleOwner)
                this.controller = controller

            }
        },
        modifier = modifier
    )
}