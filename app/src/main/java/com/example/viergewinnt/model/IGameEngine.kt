package com.example.viergewinnt.model

interface IGameEngine {

    fun startGame()

    fun makeMove(column: Int): Move?

    fun cancelGame()

    fun resetGame()

    fun playAgain()

    fun switchPlayer()

    fun getCurrentPlayer(): Player

    fun getGameState(): GameState

    fun getBoard(): Board

}