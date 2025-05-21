package com.example.task1_11345.logic

import kotlin.random.Random

class GameManager(private val lifeCount: Int = 3) {

    val ROWS: Int
        get() = 7

    val COLS: Int
        get() = 3

    private val spaceshipBoard = Array(COLS) { 0 }

    var spaceshipPos = 1

    private var rockPos: Int = 0

    val rockBoard = Array(ROWS) { Array(COLS) { 0 } }

    init { spaceshipBoard[spaceshipPos] = 1 }

    var hits: Int = 0
        private set

    val isGameOver: Boolean
        get() = hits == lifeCount

    fun checkMove(direction: Int) {
        spaceshipBoard[spaceshipPos] = 0

        if ((direction == -1 && spaceshipPos > 0) || (direction ==  1 && spaceshipPos < COLS - 1)) {
            spaceshipPos += direction
        }

        spaceshipBoard[spaceshipPos] = 1
    }

    fun rockFalls() {
        for (i in (ROWS - 1) downTo 1){
            for (j in 0 until COLS){
                rockBoard[i][j] = rockBoard[i - 1][j]
            }
        }

        for (i in 0 until COLS)
            rockBoard[0][i] = 0

        rockPos = Random.nextInt(0, COLS)

        rockBoard[0][rockPos] = 1
    }

    fun checkHit(): Boolean {
        for (i in 0 until COLS){
            if (rockBoard[ROWS - 1][i] == 1) {
                rockPos = i
                break
            }
        }

        if (spaceshipPos == rockPos && rockBoard[ROWS - 1][rockPos] == 1){
            hits++
            return true
        }

        return false
    }

    fun resetBoards(){
        for (i in 0 until ROWS){
            for (j in 0 until COLS){
                rockBoard[i][j] = 0
            }
        }

        for (i in 0 until COLS){
            spaceshipBoard[i] = 0
        }
        spaceshipPos = COLS / 2
        spaceshipBoard[spaceshipPos] = 1

        hits = 0
    }

}