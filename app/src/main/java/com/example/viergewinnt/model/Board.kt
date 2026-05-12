package com.example.viergewinnt.model

class Board {

    private val rows = 6
    private val cols = 7

    val grid = Array(rows) { arrayOfNulls<Chip>(cols) }

    fun placeChip (column: Int, chip: Chip): Boolean {
        return false
    }

    fun checkWin (): Boolean {
        return false
    }

    fun isBoardFull(): Boolean {
        return false
    }

    fun getCell( row: Int, col: Int): Chip? {
        return null
    }

    fun resetBoard (){

    }


}