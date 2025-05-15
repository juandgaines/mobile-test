package com.juandgaines.seedqrvalidator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.juandgaines.seedqrvalidator.core.presentation.navigation.Destinations
import com.juandgaines.seedqrvalidator.core.presentation.ui.theme.SeedQRValidatorTheme
import com.juandgaines.seedqrvalidator.core.presentation.utils.toBitmap
import com.juandgaines.seedqrvalidator.generator.presentation.QRGeneratorViewModel
import com.juandgaines.seedqrvalidator.home.presentation.HomeScreenRoot
import com.juandgaines.seedqrvalidator.home.presentation.HomeViewModel
import com.juandgaines.seedqrvalidator.generator.presentation.QrGeneratorScreenRoot
import com.juandgaines.seedqrvalidator.scanner.presentation.ScannerScreenRoot
import dagger.hilt.android.AndroidEntryPoint
import qrcode.QRCode
import qrcode.color.Colors

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SeedQRValidatorTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Destinations.HomeNav,
                    modifier = Modifier.fillMaxSize(),
                ){

                    composable<Destinations.HomeNav> {
                        val homeViewmodel = hiltViewModel<HomeViewModel>()
                        HomeScreenRoot(
                            homeViewModel = homeViewmodel,
                            navigateToDestination = { destination ->
                                navController.navigate(destination)
                            }
                        )
                    }

                    composable<Destinations.ScannerNav> {
                        ScannerScreenRoot()
                    }

                    composable<Destinations.GeneratorQRNav> {
                        val qrGeneratorViewmodel = hiltViewModel<QRGeneratorViewModel> ()
                        QrGeneratorScreenRoot(
                            qrGeneratorViewModel = qrGeneratorViewmodel,
                            navigateBack = {
                                navController.navigateUp()
                            }
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SeedQRValidatorTheme {
        Greeting("Android")
    }
}