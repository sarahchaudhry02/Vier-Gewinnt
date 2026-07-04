package com.example.viergewinnt.model

class GameEngine : IGameEngine {

    private val board = Board()
    private val player1 = Player("Spieler 1", Chip.RED)
    private val player2 = Player("Spieler 2", Chip.YELLOW)

    private var currentPlayer: Player = player1
    private var gameState: GameState = GameState.WAITING_FOR_CONNECTION
    private var winner: Player? = null

    override fun startGame() {
        board.resetBoard()
        gameState = GameState.RUNNING
        currentPlayer = player1
        winner = null
    }

    override fun makeMove(column: Int) : Move? {
        if (gameState != GameState.RUNNING) {
            return null
        }

        val chip = currentPlayer.getChip()
        val row = board.placeChip(column, chip) ?: return null

        val move = Move(row, column, chip)

        if (board.checkWin()) {
            winner = currentPlayer
            gameState = GameState.WON
        }

        if (board.isBoardFull()) {
            gameState = GameState.DRAW
        }

        switchPlayer()
        return move
    }

    override fun cancelGame() {
        gameState = GameState.CANCELLED
    }

    override fun resetGame() {
        board.resetBoard()
        currentPlayer = player1
        winner = null
        gameState = GameState.WAITING_FOR_CONNECTION
    }

    override fun playAgain() {
        board.resetBoard()
        currentPlayer = player1
        winner = null
        gameState = GameState.RUNNING
    }

    override fun switchPlayer() {
        currentPlayer = if (currentPlayer == player1) {
            player2
        } else {
            player1
        }
    }

    override fun getCurrentPlayer(): Player {
        return currentPlayer
    }

    override fun getGameState(): GameState {
        return gameState
    }

    override fun getBoard(): Board {
        return board
    }
}