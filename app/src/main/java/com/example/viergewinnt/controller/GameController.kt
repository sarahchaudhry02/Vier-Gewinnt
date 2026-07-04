package com.example.viergewinnt.controller

import com.example.viergewinnt.model.IGameEngine
import com.example.viergewinnt.bluetooth.BluetoothService
import com.example.viergewinnt.model.Board
import com.example.viergewinnt.model.GameState
import com.example.viergewinnt.model.Move
import com.example.viergewinnt.model.Player


class GameController (
    private val bluetoothService: BluetoothService,
    private val gameEngine: IGameEngine
) : GameServices {

    private var isHost: Boolean = true
    private var isMyTurn: Boolean = true

    init {
        bluetoothService.setOnMoveReceived { column ->
            handleIncomingBluetoothMove(column)
        }
    }

    override fun startGame() : Boolean {
        gameEngine.startGame()
        isMyTurn = isHost

        if (!isHost && bluetoothService.isConnected()) {
            val pendingColumn = bluetoothService.receiveInformation()

            if (pendingColumn != null) {
                handleIncomingBluetoothMove(pendingColumn)
            }
        }

        return true
    }

    override fun makeMove(column: Int): Board? {
        val gameState = gameEngine.getGameState()

        if (gameState != GameState.RUNNING) {
            return null
        }

        if (!isMyTurn) {
            return null
        }

        val move = gameEngine.makeMove(column)

        if (move != null) {
            isMyTurn = false

            if (bluetoothService.isConnected()) {
                bluetoothService.sendInformation(column)
            }

            return gameEngine.getBoard()
        }

        return null
    }

    private fun handleIncomingBluetoothMove(column: Int) {
        if (gameEngine.getGameState() != GameState.RUNNING) {
            return
        }

        if (isMyTurn) {
            return
        }

        val receivedMove = gameEngine.makeMove(column)

        if (receivedMove != null) {
            isMyTurn = true
        }
    }

    override fun cancelGame() {
        gameEngine.cancelGame()
        if (bluetoothService.isConnected()) {
            bluetoothService.disconnect()
        }
    }

    override fun playAgain() {
        gameEngine.playAgain()
        isMyTurn = isHost
    }

    override fun startBluetoothConnection() {
        isHost = true
        isMyTurn = true
        bluetoothService.startConnection()
    }

    override fun connectWithBluetooth() {
        isHost = false
        isMyTurn = false
        bluetoothService.connect("fake")
    }

    override fun disconnectBluetooth() {
        bluetoothService.disconnect()
    }

    override fun getBoard(): Board {
        return gameEngine.getBoard()
    }

    override fun getCurrentPlayer(): Player {
        return gameEngine.getCurrentPlayer()
    }

    override fun getGameState(): GameState {
        return gameEngine.getGameState()
    }

    override fun getWinner(): Player? {
        TODO("Not yet implemented")
    }

    override fun isBlueConnected(): Boolean {
        return bluetoothService.isConnected()
    }

    override fun isMyTurn(): Boolean {
        return isMyTurn
    }

    override fun receiveMove(move: Move) {
        handleIncomingBluetoothMove(move.column)
    }

}