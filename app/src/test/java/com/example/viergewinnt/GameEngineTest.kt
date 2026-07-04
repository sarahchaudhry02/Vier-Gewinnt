package com.example.viergewinnt

import com.example.viergewinnt.model.Chip
import com.example.viergewinnt.model.GameEngine
import com.example.viergewinnt.model.GameState
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GameEngineTest {

    private lateinit var gameEngine: GameEngine

    @Before
    fun setup() {
        gameEngine = GameEngine()
        gameEngine.startGame()
    }

    @Test
    fun makeMove_setsChipInBottomFreeField() {
        val result = gameEngine.makeMove(0)

        Assert.assertNotNull(result)
        Assert.assertEquals(5, result?.row)
        Assert.assertEquals(0, result?.column)
    }

    @Test
    fun makeMove_rejectsFullColumn() {
        repeat(6) {
            gameEngine.makeMove(0)
        }

        val result = gameEngine.makeMove(0)

        Assert.assertNull(result)
    }

    @Test
    fun startGame_setsGameStateToRunning() {
        Assert.assertEquals(GameState.RUNNING, gameEngine.getGameState())
    }

    @Test
    fun makeMove_switchesPlayerAfterValidMove() {
        val playerBefore = gameEngine.getCurrentPlayer()

        gameEngine.makeMove(0)

        val playerAfter = gameEngine.getCurrentPlayer()

        Assert.assertNotEquals(playerBefore, playerAfter)
    }

    @Test
    fun makeMove_savesCorrectMoveData() {
        val result = gameEngine.makeMove(3)

        Assert.assertNotNull(result)
        Assert.assertEquals(5, result?.row)
        Assert.assertEquals(3, result?.column)
        Assert.assertEquals(Chip.RED, result?.chip)
    }

    @Test
    fun makeMove_detectsVerticalWin() {
        gameEngine.makeMove(0) // RED
        gameEngine.makeMove(1) // YELLOW
        gameEngine.makeMove(0) // RED
        gameEngine.makeMove(1) // YELLOW
        gameEngine.makeMove(0) // RED
        gameEngine.makeMove(1) // YELLOW
        gameEngine.makeMove(0) // RED wins vertically

        Assert.assertEquals(GameState.WON, gameEngine.getGameState())
    }

    @Test
    fun cancelGame_setsGameStateToCancelled() {
        gameEngine.cancelGame()

        Assert.assertEquals(GameState.CANCELLED, gameEngine.getGameState())
    }
}