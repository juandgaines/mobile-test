package com.juandgaines.seedqrvalidator.scanner.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.common.Barcode

@Composable
fun ScannerScreenRoot() {
    // Scanner screen implementation

    Column {
        Button(onClick = {
            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                    Barcode.FORMAT_QR_CODE
                )
                .build()
        }) {
            Text("Scan")
        }
    }
}

