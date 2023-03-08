package com.example.amana_minesweeper


class Cell(private val value: Int) {
    companion object {
        const val BOMB = -1
        const val EMPTY = 0
    }

    private var isRevealed: Boolean = false
    private var isFlagged: Boolean = false


    fun getValue(): Int {
        return value
    }

    fun isRevealed(): Boolean {
        return isRevealed
    }

    fun setRevealed(revealed: Boolean) {
        isRevealed = revealed
    }

    fun isFlagged(): Boolean {
        return isFlagged
    }

    fun setFlagged(flagged: Boolean) {
        isFlagged = flagged
    }
}