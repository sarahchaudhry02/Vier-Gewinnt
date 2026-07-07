package com.example.viergewinnt.integration

import com.example.viergewinnt.bluetooth.FakeBluetoothHandler
import com.example.viergewinnt.controller.GameController
import com.example.viergewinnt.model.GameEngine
import com.example.viergewinnt.model.GameState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull

class GameControllerIntegrationTest {

    @Test
    fun startGame_newGame_setsGameStateToRunning() {
        val bluetoothService = FakeBluetoothHandler()
        val gameEngine = GameEngine()
        val controller = GameController(bluetoothService, gameEngine)

        val result = controller.startGame()

        assertTrue(result)
        assertEquals(GameState.RUNNING, controller.getGameState())
    }

    @Test
    fun makeMove_validColumn_placesChipOnBoard() {
        val bluetoothService = FakeBluetoothHandler()
        val gameEngine = GameEngine()
        val controller = GameController(bluetoothService, gameEngine)

        controller.startGame()

        controller.makeMove(3)

        assertNotNull(controller.getBoard().getCell(5, 3))
    }

    @Test
    fun makeMove_switchesCurrentPlayer() {
        val bluetoothService = FakeBluetoothHandler()
        val gameEngine = GameEngine()
        val controller = GameController(bluetoothService, gameEngine)

        controller.startGame()

        val firstPlayer = controller.getCurrentPlayer()

        controller.makeMove(3)

        val secondPlayer = controller.getCurrentPlayer()

        assertNotEquals(firstPlayer, secondPlayer)
    }

    @Test
    fun makeMove_fourChipsInRow_setsGameStateToWon() {
        val gameEngine = GameEngine()

        gameEngine.startGame()

        gameEngine.makeMove(0)
        gameEngine.makeMove(0)
        gameEngine.makeMove(1)
        gameEngine.makeMove(1)
        gameEngine.makeMove(2)
        gameEngine.makeMove(2)
        gameEngine.makeMove(3)

        assertEquals(GameState.WON, gameEngine.getGameState())
    }

    @Test
    fun makeMove_fullBoardWithoutWinner_setsGameStateToDraw() {
        val gameEngine = GameEngine()

        gameEngine.startGame()

        val moves = listOf(
            5, 0, 6, 4, 6, 4, 4,
            3, 0, 5, 1, 3, 5, 3,
            3, 2, 6, 6, 2, 3, 3,
            6, 6, 4, 4, 1, 2, 0,
            0, 5, 0, 1, 5, 4, 2,
            1, 1, 2, 2, 5, 0, 1
        )

        moves.forEach { column ->
            gameEngine.makeMove(column)
        }

        assertEquals(GameState.DRAW, gameEngine.getGameState())
    }

    @Test
    fun makeMove_afterWin_noFurtherMovesPossible() {
        val gameEngine = GameEngine()

        gameEngine.startGame()

        gameEngine.makeMove(0)
        gameEngine.makeMove(0)
        gameEngine.makeMove(1)
        gameEngine.makeMove(1)
        gameEngine.makeMove(2)
        gameEngine.makeMove(2)
        gameEngine.makeMove(3)

        assertEquals(GameState.WON, gameEngine.getGameState())

        val moveAfterWin = gameEngine.makeMove(4)

        assertNull(moveAfterWin)
        assertEquals(GameState.WON, gameEngine.getGameState())
    }






}