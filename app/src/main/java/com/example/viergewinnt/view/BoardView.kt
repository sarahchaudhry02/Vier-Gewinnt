package com.example.viergewinnt.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.viergewinnt.controller.GameServices

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.viergewinnt.model.Board
import com.example.viergewinnt.model.Chip
import com.example.viergewinnt.model.GameState

@Composable
fun BoardView(
    gameServices: GameServices,
    onBackToHome: () -> Unit
) {

    var board: Board? by remember { mutableStateOf(gameServices.getBoard()) }
    var boardVersion by remember { mutableIntStateOf(0) }
    var statusText by remember {
        mutableStateOf(
            if (gameServices.isMyTurn()) {
                "Du bist dran"
            } else {
                "Auf anderen Spieler warten"
            }
        )
    }
    val coroutineScope = rememberCoroutineScope()

    fun updateStatusAfterMove(localPlayerMoved: Boolean) {
        statusText = when (gameServices.getGameState()) {
            GameState.WON -> {
                if (localPlayerMoved) {
                    "Du hast gewonnen!"
                } else {
                    "Du hast verloren!"
                }
            }
            GameState.DRAW -> "Unentschieden"
            GameState.CANCELLED -> "Spiel Verlassen"
            else -> "Du bist dran"
        }
    }

    LaunchedEffect(Unit) {
        while (!gameServices.isMyTurn()) {
            delay(200)
        }

        board = gameServices.getBoard()
        boardVersion++
        updateStatusAfterMove(localPlayerMoved = false)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFEFE8FA)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = statusText,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3F2A56),
                modifier = Modifier.padding(top = 60.dp, bottom = 70.dp)
            )
            Spacer(modifier = Modifier.padding(top = 20.dp))
            ConnectFourBoard(
                board = board,
                boardVersion = boardVersion,
                onColumnClick = { column ->
                    val move = gameServices.makeMove(column)
                    if (move != null) {
                        board = gameServices.getBoard()
                        boardVersion++

                        if (gameServices.getGameState() == GameState.WON) {
                            updateStatusAfterMove(localPlayerMoved = true)
                        } else {
                            statusText = "Auf anderen Spieler warten"

                            coroutineScope.launch {
                                while (!gameServices.isMyTurn()) {
                                    delay(200)
                                }

                                board = gameServices.getBoard()
                                boardVersion++

                                updateStatusAfterMove(localPlayerMoved = false)
                            }
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    gameServices.cancelGame()
                    onBackToHome()
                },
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Spiel verlassen", fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun ConnectFourBoard(
    board: Board?,
    boardVersion: Int = 0,
    onColumnClick: (Int) -> Unit
) {
    val currentBoardVersion = boardVersion
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("game_board")
            .background(
                color = Color(0xFF1565C0),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (row in 0 until 6) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (column in 0 until 7) {
                    ChipSlot(
                        chip = board?.getCell(row, column),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("cell_${row}_${column}")
                            .clickable {
                                onColumnClick(column)
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun ChipSlot(
    chip: Chip?,
    modifier: Modifier = Modifier
) {
    val chipColor = when (chip) {
        Chip.RED -> Color(0xFFE57373)
        Chip.YELLOW -> Color(0xFFFFF176)
        Chip.EMPTY -> Color.White
        null -> Color.White
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(chipColor)
    )
}