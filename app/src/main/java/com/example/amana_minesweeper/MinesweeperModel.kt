package com.example.amana_minesweeper

import java.util.*

class MinesweeperModel(private val size: Int) {
    private val cells: MutableList<Cell> = ArrayList()

    init {
        for (i in 0 until size * size) {
            cells.add(Cell(Cell.EMPTY))
        }
    }

    fun generateGrid(totalBombs: Int) {
        var bombsPlaced = 0
        while (bombsPlaced < totalBombs) {
            val x = Random().nextInt(size)
            val y = Random().nextInt(size)

            if (cellAt(x, y)?.getValue() == Cell.EMPTY) {
                cells[toIndex(x, y)] = Cell(Cell.BOMB)
                bombsPlaced++
            }
        }

        for (x in 0 until size) {
            for (y in 0 until size) {
                if (cellAt(x, y)?.getValue() != Cell.BOMB) {
                    val adjacentCells = adjacentCells(x, y)
                    var countBombs = 0
                    for (cell in adjacentCells) {
                        if (cell.getValue() == Cell.BOMB) {
                            countBombs++
                        }
                    }
                    if (countBombs > 0) {
                        cells[toIndex(x, y)] = Cell(countBombs)
                    }
                }
            }
        }
    }

    private fun cellAt(x: Int, y: Int): Cell? {
        if (x < 0 || x >= size || y < 0 || y >= size) {
            return null
        }
        return cells[toIndex(x, y)]
    }

    fun adjacentCells(x: Int, y: Int): List<Cell> {
        val adjacentCells: MutableList<Cell> = ArrayList()

        val cellsList: MutableList<Cell?> = ArrayList()
        cellsList.add(cellAt(x - 1, y))
        cellsList.add(cellAt(x + 1, y))
        cellsList.add(cellAt(x - 1, y - 1))
        cellsList.add(cellAt(x, y - 1))
        cellsList.add(cellAt(x + 1, y - 1))
        cellsList.add(cellAt(x - 1, y + 1))
        cellsList.add(cellAt(x, y + 1))
        cellsList.add(cellAt(x + 1, y + 1))

        for (cell in cellsList) {
            if (cell != null) {
                adjacentCells.add(cell)
            }
        }

        return adjacentCells
    }

    fun toIndex(x: Int, y: Int): Int {
        return x + y * size
    }

    fun toXY(index: Int): IntArray {
        val y = index / size
        val x = index - y * size
        return intArrayOf(x, y)
    }

    fun revealAllBombs() {
        for (c in cells) {
            if (c.getValue() == Cell.BOMB) {
                c.setRevealed(true)
            }
        }
    }

    fun getCells(): List<Cell> {
        return cells
    }
}
