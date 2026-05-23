package com.example.viergewinnt.controller

import com.example.viergewinnt.model.Board
import com.example.viergewinnt.model.GameState
import com.example.viergewinnt.model.Move
import com.example.viergewinnt.model.player.Player

interface GameServices {

    fun startGame()

    fun makeMove(column: Int) : Move?

    fun switchPlayer()

    fun resetGame()

    fun getCurrentPlayer(): Player

    fun getGameState(): GameState

    fun getBoard(): Board

    fun isGameOver(): Boolean

    fun sendMoveBluetooth()

    fun receiveMoveBluetooth()

}