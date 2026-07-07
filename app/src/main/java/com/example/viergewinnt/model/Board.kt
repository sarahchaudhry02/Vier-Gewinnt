package com.example.viergewinnt.model

class Board {

    private val rows = 6
    private val cols = 7

    val grid = Array(rows) { arrayOfNulls<Chip>(cols) }


    fun placeChip(column: Int, chip: Chip): Int? {
        if (column !in 0 until cols) {
            return null
        }

        for (row in rows - 1 downTo 0) {
            if (grid[row][column] == null) {
                grid[row][column] = chip
                return row
            }
        }

        return null
    }

    fun checkWin(): Boolean {
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                val chip = grid[row][col] ?: continue

                // horizontal:
                if (col + 3 < cols &&
                    grid[row][col + 1] == chip &&
                    grid[row][col + 2] == chip &&
                    grid[row][col + 3] == chip
                ) {
                    return true
                }

                // vertical:
                if (row + 3 < rows &&
                    grid[row + 1][col] == chip &&
                    grid[row + 2][col] == chip &&
                    grid[row + 3][col] == chip
                ) {
                    return true
                }

                // diagonal:
                if (row + 3 < rows && col + 3 < cols &&
                    grid[row + 1][col + 1] == chip &&
                    grid[row + 2][col + 2] == chip &&
                    grid[row + 3][col + 3] == chip
                ) {
                    return true
                }

                // diagonal:
                if (row + 3 < rows && col - 3 >= 0 &&
                    grid[row + 1][col - 1] == chip &&
                    grid[row + 2][col - 2] == chip &&
                    grid[row + 3][col - 3] == chip
                ) {
                    return true
                }
            }
        }

        return false
    }

    fun isBoardFull(): Boolean {
        for (col in 0 until cols) {
            if (grid[0][col] == null) {
                return false
            }
        }
        return true
    }

    fun getCell(row: Int, col: Int): Chip? {
        if (row !in 0 until rows || col !in 0 until cols) {
            return null
        }
        return grid[row][col]
    }

    fun resetBoard() {
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                grid[row][col] = null
            }
        }
    }


}