@file:OptIn(ExperimentalMaterial3Api::class)

package com.juandgaines.seedqrvalidator.home.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontVariation.weight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                    navigateToDestination(Destinations.GeneratorQRNav(event.seed))
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
        topBar = {
            TopAppBar(
                title = {
                    Text("Home")
                }
            )
        },
        floatingActionButton = {
            Column (
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){

                AnimatedVisibility(
                    state.isMenuVisible
                ){
                    Column (
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        FloatingActionButton(
                            onClick = {
                                onIntent(HomeIntent.GenerateQrIntent)
                            }
                        ) {
                            Icon(
                                imageVector =Icons.Default.QrCode,
                                contentDescription = "Generate QR Code",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        FloatingActionButton(
                            onClick = {
                                onIntent(HomeIntent.ScanQrIntent)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = "Scan QR Code",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
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
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

        }
    ){ paddingValues ->
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ){
            items(
                items = state.seedList,
                key = { it.seed }
            ){
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .wrapContentHeight()
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(
                                16.dp
                            )
                        )
                        .clickable {
                            onIntent(HomeIntent.OpenExistingQrIntent(it.seed))
                        }
                ) {
                    Text(
                        text = it.seed,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                    Text(
                        text = it.type.name,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }

            }

        }
    }
}