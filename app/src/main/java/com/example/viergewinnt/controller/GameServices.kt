package com.example.viergewinnt.controller

import com.example.viergewinnt.model.Board
import com.example.viergewinnt.model.GameState
import com.example.viergewinnt.model.Move
import com.example.viergewinnt.model.Player

interface GameServices {

    // Spielsteuerung

    fun startGame() : Boolean

    fun makeMove(column: Int): Board?

    fun cancelGame()

    fun playAgain()


    // Bluetooth

    fun startBluetoothConnection()

    fun connectWithBluetooth()

    fun disconnectBluetooth()


    // Daten für UI

    fun getBoard(): Board

    fun getCurrentPlayer(): Player

    fun getGameState(): GameState

    fun getWinner(): Player?

    fun isBlueConnected (): Boolean

    fun isMyTurn(): Boolean


    // Eingehende Bluetooth-Daten

    fun receiveMove(move: Move)





}