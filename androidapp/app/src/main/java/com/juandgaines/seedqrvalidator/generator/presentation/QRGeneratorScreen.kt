package com.juandgaines.seedqrvalidator.generator.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.juandgaines.seedqrvalidator.R
import com.juandgaines.seedqrvalidator.core.presentation.utils.toBitmap
import qrcode.QRCode
import qrcode.color.Colors

@Composable
fun QrGeneratorScreenRoot(
    qrGeneratorViewModel: QRGeneratorViewModel,
    navigateBack:()-> Unit
){

    val events = qrGeneratorViewModel.eventsGenerator
    val state by qrGeneratorViewModel.generatorState.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        events.collect{qrEvents->
            when(qrEvents){
                GeneratorEvent.NavigateBack -> {
                    navigateBack()
                }
            }
        }
    }
    QrGeneratorScreen(
        state = state,
        onAction = qrGeneratorViewModel::onAction
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrGeneratorScreen(
    state:GeneratorState,
    onAction:(GeneratorIntent)-> Unit
) {

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("QR Code Generator")
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier.clickable {
                            onAction(GeneratorIntent.NavigateBack)
                        },
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            )
        },
    ){ paddingValues ->


        Column (
            modifier =  Modifier.fillMaxSize().padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            val colorQr = MaterialTheme.colorScheme.onBackground
            when{
                state.seed.isNotEmpty()->{
                    val bitmap by remember {
                        derivedStateOf {
                            QRCode.ofSquares()
                                .withColor(
                                    colorQr.toArgb()
                                ) // Default is Colors.BLACK
                                .withSize(10) // Default is 25
                                .build(state.seed)
                                .renderToBytes().toBitmap()
                        }
                    }


                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(16.dp)
                            .aspectRatio(1f)
                    )


                    Text(
                        text = if (!state.hasExpired)"Expires in: ${state.remainingTime}" else "Seed Expired",
                        color = if(!state.hasExpired)MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.error
                    )
                }
                state.error != null ->{

                    Text(
                        text = stringResource(state.error),
                        color = MaterialTheme.colorScheme.error
                    )
                    Button(
                        onClick = {
                            onAction(GeneratorIntent.Retry)
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.retry),
                        )
                    }

                }

                state.isLoading->{
                    CircularProgressIndicator()
                }

            }

        }
    }
}