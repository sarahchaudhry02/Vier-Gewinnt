package com.example.viergewinnt

import com.example.viergewinnt.bluetooth.BluetoothService
import com.example.viergewinnt.bluetooth.FakeBluetoothHandler
import com.example.viergewinnt.controller.GameController
import com.example.viergewinnt.model.GameEngine
import com.example.viergewinnt.model.GameState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GameControllerTests {

    @Test

    fun spielzugWirdAnGameEngineWeitergeleitet() {

        val bluetoothService = TestBluetoothService()

        val gameEngine = GameEngine()

        val controller = GameController(bluetoothService, gameEngine)

        controller.startGame()

        val board = controller.makeMove(3)

        assertNotNull(board)

        assertNotNull(gameEngine.getBoard().getCell(5, 3))

    }
    @Test

    fun spielzugWirdUeberBluetoothGesendetUndEmpfangen() {

        val bluetoothService = TestBluetoothService()

        val gameEngine = GameEngine()

        val controller = GameController(bluetoothService, gameEngine)

        controller.startBluetoothConnection()

        controller.startGame()

        controller.makeMove(2)

        assertEquals(2, bluetoothService.sentColumn)

        assertFalse(controller.isMyTurn())

        bluetoothService.simulateIncomingMove(4)

        assertNotNull(controller.getBoard().getCell(5, 4))

        assertTrue(controller.isMyTurn())

    }

    @Test

    fun spielzugAktualisiertSpielzustandUndSpieler() {

        val bluetoothService = TestBluetoothService()

        val gameEngine = GameEngine()

        val controller = GameController(bluetoothService, gameEngine)

        controller.startGame()

        val playerBeforeMove = controller.getCurrentPlayer()

        controller.makeMove(1)

        assertEquals(GameState.RUNNING, controller.getGameState())

        assertNotEquals(playerBeforeMove, controller.getCurrentPlayer())

    }

    private class TestBluetoothService : BluetoothService {

        private var connected = false
        private var listener: ((Int) -> Unit)? = null

        var sentColumn: Int? = null
            private set

        override fun startConnection() {
            connected = true
        }

        override fun connect(deviceAddress: String): Boolean {
            connected = true
            return true
        }

        override fun disconnect() {
            connected = false
        }

        override fun sendInformation(column: Int) {
            sentColumn = column
        }

        override fun receiveInformation(): Int? = null

        override fun isConnected(): Boolean = connected

        override fun setOnMoveReceived(listener: (Int) -> Unit) {
            this.listener = listener
        }

        fun simulateIncomingMove(column: Int) {
            listener?.invoke(column)
        }
    }



}