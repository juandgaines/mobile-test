package com.juandgaines.seedqrvalidator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juandgaines.seedqrvalidator.core.presentation.utils.toBitmap
import com.juandgaines.seedqrvalidator.ui.theme.SeedQRValidatorTheme
import qrcode.QRCode
import qrcode.color.Colors

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SeedQRValidatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val helloWorld = QRCode.ofSquares()
        .withColor(Colors.DEEP_SKY_BLUE) // Default is Colors.BLACK
        .withSize(10) // Default is 25
        .build("49423161e3844d42991763bbae6dc1f6")

    val byteArray = helloWorld.renderToBytes().toBitmap()

    Image(
        bitmap = byteArray.asImageBitmap(),
        contentDescription = null,
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    )
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SeedQRValidatorTheme {
        Greeting("Android")
    }
}