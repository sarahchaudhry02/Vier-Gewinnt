package com.example.viergewinnt.view

import com.example.viergewinnt.controller.GameServices
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height

import androidx.compose.ui.unit.sp

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import androidx.compose.ui.text.style.TextAlign

@Composable
fun BluetoothConnectView(
    modifier: Modifier = Modifier,
    gameServices: GameServices,
    connectionMode: String,
    onStartClick: () -> Unit
) {
    val waitingText = if (connectionMode == "host") {
        "Warten bis Gast-Spieler sich verbindet"
    } else {
        "Verbindung zum Host-Spieler wird hergestellt"
    }

    val connectedText = if (connectionMode == "host") {
        "Gast-Spieler verbunden"
    } else {
        "Mit Host-Spieler verbunden"
    }

    var isConnected by remember { mutableStateOf(gameServices.isBlueConnected()) }

    LaunchedEffect(Unit) {
        while (!isConnected) {
            delay(500)
            isConnected = gameServices.isBlueConnected()
        }
    }


    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFAFA2D6),
                        Color(0xFF9F90CC),
                        Color(0xFF8C7AB8)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 220.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!isConnected) {
                Text(
                    text = waitingText,
                    fontSize = 20.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            } else {
                Text(
                    text = connectedText,
                    fontSize = 20.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        gameServices.startGame()
                        onStartClick()
                    }
                ) {
                    Text("Start")
                }
            }
        }
    }
}