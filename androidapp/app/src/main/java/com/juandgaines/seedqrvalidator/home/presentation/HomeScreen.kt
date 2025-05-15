package com.juandgaines.seedqrvalidator.home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.juandgaines.seedqrvalidator.core.presentation.navigation.Destinations

@Composable
fun HomeScreenRoot(
    homeViewModel: HomeViewModel,
    navigateToDestination: (Destinations) -> Unit,
){
    val state by homeViewModel.homeState.collectAsStateWithLifecycle()
    val event =  homeViewModel.eventsHome

    LaunchedEffect(true) {
        event.collect { event ->
            when (event) {
                is HomeEvent.NavigateToScan -> {
                    navigateToDestination(Destinations.ScannerNav)
                }
                is HomeEvent.NavigateToQr -> {
                    navigateToDestination(Destinations.GeneratorQRNav)
                }
            }
        }
    }

    HomeScreen(
        state = state,
        onIntent = homeViewModel::onIntent
    )

}
@Composable
fun HomeScreen(
    state: HomeState,
    onIntent: (HomeIntent) -> Unit,
){

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            Column {

                if (state.isMenuVisible){
                    FloatingActionButton(
                        onClick = {
                            onIntent(HomeIntent.GenerateQrIntent)
                        }
                    ) {
                        Icon(
                            imageVector =Icons.Default.QrCode,
                            contentDescription = "Generate QR Code",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    FloatingActionButton(
                        onClick = {
                            onIntent(HomeIntent.GenerateQrIntent)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = "Scan QR Code",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                FloatingActionButton(
                    onClick = {
                        val intent = if (state.isMenuVisible) {
                            HomeIntent.CollapseFabMenu
                        } else {
                            HomeIntent.ExpandFabMenu
                        }
                        onIntent(intent)
                    }
                ) {
                    Icon(
                        imageVector = if (state.isMenuVisible) Icons.Default.Close
                        else Icons.Default.Add,
                        contentDescription = "Expand/Close Menu",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

        }
    ){ paddingValues ->
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            items(
                items = state.seedList,
                key = { it.seed }
            ){
                Text(
                    text = it.seed,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            }

        }
    }
}