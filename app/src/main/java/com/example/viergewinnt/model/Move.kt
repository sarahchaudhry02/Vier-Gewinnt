package com.example.viergewinnt.model

data class Move(
    val column: Int,
    val chip: Chip,
    val playerId: Int
)
