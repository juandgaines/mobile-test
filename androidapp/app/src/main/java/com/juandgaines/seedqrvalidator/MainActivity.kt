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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.juandgaines.seedqrvalidator.core.presentation.navigation.Destinations
import com.juandgaines.seedqrvalidator.core.presentation.utils.toBitmap
import com.juandgaines.seedqrvalidator.core.presentation.ui.theme.SeedQRValidatorTheme
import com.juandgaines.seedqrvalidator.home.presentation.HomeScreenRoot
import com.juandgaines.seedqrvalidator.reader.presentation.ReaderScreenRoot
import com.juandgaines.seedqrvalidator.scanner.presentation.ScannerScreenRoot
import qrcode.QRCode
import qrcode.color.Colors

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SeedQRValidatorTheme {

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Destinations.HomeNav,
                        modifier = Modifier.padding(innerPadding),
                    ){
                        composable<Destinations.HomeNav> {
                            HomeScreenRoot()
                        }

                        composable<Destinations.ScannerNav> {
                            ScannerScreenRoot()
                        }

                        composable<Destinations.ReaderNav> {
                            ReaderScreenRoot()
                        }

                    }
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