package com.enesokurterzi.minesweeperkotlin

    fun main() {
        val minefield = Minefield()
        println("How many mines do you want on the field? ")
        minefield.firstTimeCreater(readln().toInt())
}