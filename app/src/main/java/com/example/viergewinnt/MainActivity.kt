package com.example.viergewinnt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.viergewinnt.ui.theme.VierGewinntTheme
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.geometry.Offset
import com.example.viergewinnt.view.BluetoothConnectView
import com.example.viergewinnt.view.BoardView
import com.example.viergewinnt.controller.GameController
import com.example.viergewinnt.controller.GameServices
import com.example.viergewinnt.model.GameEngine
import com.example.viergewinnt.bluetooth.FakeBluetoothHandler

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VierGewinntTheme {
                val gameServices: GameServices = remember {
                    GameController(
                        gameEngine = GameEngine(),
                        bluetoothService = FakeBluetoothHandler()
                    )
                }
                var currentScreen by remember { mutableStateOf("start") }
                var connectionMode by remember { mutableStateOf("host") }

                when (currentScreen) {
                    "start" -> StartScreen(
                        onStartConnection = {
                            connectionMode = "host"
                            gameServices.startBluetoothConnection()
                            currentScreen = "bluetooth"
                        },
                        onConnect = {
                            connectionMode = "guest"
                            gameServices.connectWithBluetooth()
                            currentScreen = "bluetooth"
                        }
                    )

                    "bluetooth" -> BluetoothConnectView(
                        gameServices = gameServices,
                        connectionMode = connectionMode,
                        onStartClick = {
                            currentScreen = "board"
                        }
                    )

                    "board" -> BoardView(
                        gameServices = gameServices,
                        onBackToHome = {
                            currentScreen = "start"
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    onStartConnection: () -> Unit,
    onConnect: () -> Unit
) {
    Column(
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
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Vier\nGewinnt",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                shadow = Shadow(
                    color = Color(0xFF7D70A5),
                    offset = Offset(3f, 3f),
                    blurRadius = 4f
                )
            ),
            color = Color(0xFF4B3F72)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 18.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFD9CFF0)
            )
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val text1 = "Willkommen bei Vier Gewinnt!"

                val text2 = "Um das Spiel zu spielen, wird auf beiden Endgeräten eine Bluetooth-Verbindung vorausgesetzt. Ein Spieler erstellt als Host das Spiel, der andere Spieler tritt als Gast bei."

                val text3 = "Mit 'Verbindung starten' hosten Sie ein Spiel, mit 'verbinden' treten Sie einem Spiel bei."

                Text(
                    text = text1,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF5F537F)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = text2,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF5F537F)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = text3,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF5F537F)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(onClick = { onStartConnection() }) {
                    Text(text = "Verbindung starten")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { onConnect() }) {
                    Text(text = "Verbinden")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    VierGewinntTheme {
        StartScreen(
            onStartConnection = {},
            onConnect = {}
        )
    }
}