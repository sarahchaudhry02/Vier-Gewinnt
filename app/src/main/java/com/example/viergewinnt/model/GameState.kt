package com.example.viergewinnt.model

import com.example.viergewinnt.model.player.Player

data class GameState(
    val board: Board,
    var currentPlayer: Player,
    val winner: Player?, // dieser Wert darf null sein
    val isGameOver: Boolean
)
