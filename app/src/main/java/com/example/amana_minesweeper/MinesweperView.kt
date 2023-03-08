package com.example.amana_minesweeper

class MinesweeperView (private val size: Int, private val numberBombs: Int){

    private val mineGrid = MinesweeperModel(size)
    private var gameOver = false
    private var flagMode = false
    private var clearMode = true
    private var flagCount = 0
    private var timeExpired = false

    init {
        mineGrid.generateGrid(numberBombs)
    }

    fun handleCellClick(cell: Cell) {
        if (!gameOver && !isGameWon() && !timeExpired && !cell.isRevealed()) {
            if (clearMode) {
                clear(cell)
            } else if (flagMode) {
                flag(cell)
            }
        }
    }

    fun clear(cell: Cell) {
        val index = mineGrid.getCells().indexOf(cell)
        mineGrid.getCells()[index].setRevealed(true)

        if (cell.getValue() == Cell.BOMB) {
            gameOver = true
        } else if (cell.getValue() == Cell.EMPTY) {
            val toClear = mutableListOf<Cell>()
            val toCheckAdjacents = mutableListOf<Cell>()

            toCheckAdjacents.add(cell)

            while (toCheckAdjacents.isNotEmpty()) {
                val c = toCheckAdjacents[0]
                val cellIndex = mineGrid.getCells().indexOf(c)
                val cellPos = mineGrid.toXY(cellIndex)
                for (adjacent in mineGrid.adjacentCells(cellPos[0], cellPos[1])) {
                    if (adjacent.getValue() == Cell.EMPTY) {
                        if (!toClear.contains(adjacent)) {
                            if (!toCheckAdjacents.contains(adjacent)) {
                                toCheckAdjacents.add(adjacent)
                            }
                        }
                    } else {
                        if (!toClear.contains(adjacent)) {
                            toClear.add(adjacent)
                        }
                    }
                }
                toCheckAdjacents.remove(c)
                toClear.add(c)
            }

            for (c in toClear) {
                c.setRevealed(true)
            }
        }
    }

    fun flag(cell: Cell) {
        cell.setFlagged(!cell.isFlagged())
        flagCount = mineGrid.getCells().count { it.isFlagged() }
    }

    fun isGameWon(): Boolean {
        val numbersUnrevealed = mineGrid.getCells().count { c ->
            c.getValue() != Cell.BOMB && c.getValue() != Cell.EMPTY && !c.isRevealed()
        }
        return numbersUnrevealed == 0
    }

    fun toggleMode() {
        clearMode = !clearMode
        flagMode = !flagMode
    }

    fun outOfTime() {
        timeExpired = true
    }

    fun isGameOver(): Boolean {
        return gameOver
    }

    fun isFlagMode(): Boolean {
        return flagMode
    }

    fun isClearMode(): Boolean {
        return clearMode
    }

    fun getFlagCount(): Int {
        return flagCount
    }

    fun getNumberBombs(): Int {
        return numberBombs
    }


    fun getMineGrid(): MinesweeperModel {
        return mineGrid
    }
}