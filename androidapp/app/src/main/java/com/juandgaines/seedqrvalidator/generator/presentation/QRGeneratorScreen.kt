package com.juandgaines.seedqrvalidator.generator.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
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

@Composable
fun QrGeneratorScreen(
    state:GeneratorState,
    onAction:(GeneratorIntent)-> Unit
) {

    Scaffold (
        modifier = Modifier.fillMaxSize()
    ){ paddingValues ->

        Column (modifier =  Modifier.fillMaxSize().padding(paddingValues)){
            if(state.seed.isNotEmpty()){
                val helloWorld = QRCode.ofSquares()
                    .withColor(Colors.DEEP_SKY_BLUE) // Default is Colors.BLACK
                    .withSize(10) // Default is 25
                    .build(state.seed)

                val byteArray = helloWorld.renderToBytes().toBitmap()

                Image(
                    bitmap = byteArray.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(16.dp)
                        .aspectRatio(1f)
                )

                Text(
                    text = "Expires in: ${state.remainingTime}",
                    color = MaterialTheme.colorScheme.primary
                )
            }

        }
    }
}