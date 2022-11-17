package com.enesokurterzi.minesweeperkotlin

import kotlin.random.Random

class Minefield(private val fieldSize: Int = 9) {
    private var field = MutableList(fieldSize) { MutableList(fieldSize) { MutableList(2) { "0" } } }
    private var foundedMineCount = 0
    private var mineCount: Int = 0
    private var steppedOnMine: Boolean = false
    private var x: Int = 0
    private var y: Int = 0
    private var isItFirstTime: Boolean = true

    private fun generateMines() {
        repeat(mineCount) {
            var row: Int
            var column: Int
            do {
                row = Random.nextInt(0, fieldSize)
                column = Random.nextInt(0, fieldSize)
            } while ((field[row][column][0] == "X") || ((row == x) && (column == y)))
            field[row][column][0] = "X"
            increaseAdjacentCellCount(row, column)
        }
        if (!isItFirstTime) {
            printField()
        }
        isItFirstTime = false
    }
    private fun printField() {
        println(" │123456789│\n" + "—│—————————│")
        for (i in 0 until field.size) {
            print("${i + 1}│")
            for (j in 0 until field[0].size) {
                if (field[i][j][1] == "0"){     //0 means do not show except the marked ones.
                    print(field[i][j][0].replace(Regex("[X012345678]"), "."))
                } else {
                    print(field[i][j][0].replace("0","/"))
                }
            }
            println("│")
        }
        println("—│—————————│")
        setDeleteMines()
    }
    private fun increaseAdjacentCellCount(row: Int, column: Int) {
        for (i in -1..1) {
            for (j in -1..1) {
                val thisRow = row + i
                val thisColumn = column + j
                if (thisRow in 0 until fieldSize && thisColumn in 0 until fieldSize) {
                    if (field[thisRow][thisColumn][0] != "X") field[thisRow][thisColumn][0] = (field[thisRow][thisColumn][0].toInt() + 1).toString()
                }
            }
        }
    }
    private fun setDeleteMines() {
        println("Set/delete mines marks (x and y coordinates):")
        val coordinates = readLine()!!.split(' ').map { it }.toMutableList()
        x = coordinates[1].toInt() - 1
        y = coordinates[0].toInt() - 1
        if (isItFirstTime) {
            generateMines()
        }
        if (coordinates[2] == "mine") {
            when (field[x][y][0]) {
                "*" -> {
                    if (field[x][y][1] == "mine") {
                        field[x][y][1] = "0"
                        field[x][y][0] = "X"
                        foundedMineCount--
                    } else {
                        field[x][y][1] = "0"
                        field[x][y][0] = "0"
                        minesAround(x, y)
                    }
                }
                "X" -> {
                    field[x][y][1] = "mine"
                    field[x][y][0] = "*"
                    foundedMineCount++
                }
                else -> {
                    field[x][y][1] = "not mine"
                    field[x][y][0] = "*"
                }
            }
        } else if (coordinates[2] == "free") {
            when (field[x][y][0]) {
                "X" -> steppedOnMine = true
                else -> {
                    if (field[x][y][0] == "0") {
                        field[x][y][1] = "show"
                        freeSearch(x, y)
                    }
                    field[x][y][1] = "show"
                }
            }
        }
        if (foundedMineCount == mineCount) {
            println("Congratulations! You found all the mines!")
        } else if (steppedOnMine) {
            println("You stepped on a mine and failed!")
        } else {
            printField()
        }
    }
    fun firstTimeCreater(mineCount: Int) {
        this.mineCount = mineCount
        printField()
    }
    private fun freeSearch(row: Int, column: Int) {
        for (i in -1..1) {
            for (j in -1..1) {
                val thisRow = row + i
                val thisColumn = column + j
                if (thisRow in 0 until fieldSize && thisColumn in 0 until fieldSize && !(row == thisRow && column == thisColumn)) {
                    if (field[thisRow][thisColumn][0] == "0" && field[thisRow][thisColumn][1] == "0") {
                        field[thisRow][thisColumn][1] = "show"
                        freeSearch(thisRow, thisColumn)
                    } else if (field[thisRow][thisColumn][1] == "not mine") {
                        field[thisRow][thisColumn][1] = "show"
                        field[thisRow][thisColumn][0] = "0"
                        minesAround(thisRow,thisColumn)
                        if (field[thisRow][thisColumn][0] == "0"){
                            freeSearch(thisRow, thisColumn)
                        }
                    }
                    else {
                        field[thisRow][thisColumn][1] = "show"
                    }
                }
            }
        }
    }
    private fun minesAround(row: Int, column: Int) {
        for (k in -1..1) {
            for (l in -1..1) {
                val thisRow = row + k
                val thisColumn = column + l
                if (thisRow in 0 until fieldSize && thisColumn in 0 until fieldSize) {
                    if (field[thisRow][thisColumn][0] == "X") {
                        field[row][column][0] = (field[row][column][0].toInt() + 1).toString()
                    }
                }
            }
        }
    }
}