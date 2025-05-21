package com.example.task1_11345

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.task1_11345.logic.GameManager
import com.example.task1_11345.utilities.SignalManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {

    private lateinit var main_IMG_hearts: Array<AppCompatImageView>

    private lateinit var main_IMG_spaceships: Array<AppCompatImageView>

    private lateinit var main_IMG_rocks: Array<Array<AppCompatImageView>>

    private lateinit var main_FAB_left: FloatingActionButton

    private lateinit var main_FAB_right: FloatingActionButton

    private lateinit var timer: Timer

    private var timerStarted = false

    private lateinit var gameManager: GameManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        SignalManager.init(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViews()
        gameManager = GameManager(main_IMG_hearts.size)
        initViews()

        startTimer()
    }

    private fun findViews() {
        main_FAB_left = findViewById(R.id.main_FAB_left)
        main_FAB_right = findViewById(R.id.main_FAB_right)

        main_IMG_hearts = arrayOf(
            findViewById(R.id.main_IMG_heart0),
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2)
        )

        main_IMG_spaceships = arrayOf(
            findViewById(R.id.main_IMG_rocket0),
            findViewById(R.id.main_IMG_rocket1),
            findViewById(R.id.main_IMG_rocket2)
        )

        main_IMG_rocks = arrayOf(
            arrayOf(findViewById(R.id.main_IMG_rock00), findViewById(R.id.main_IMG_rock01), findViewById(R.id.main_IMG_rock02)),
            arrayOf(findViewById(R.id.main_IMG_rock10), findViewById(R.id.main_IMG_rock11), findViewById(R.id.main_IMG_rock12)),
            arrayOf(findViewById(R.id.main_IMG_rock20), findViewById(R.id.main_IMG_rock21), findViewById(R.id.main_IMG_rock22)),
            arrayOf(findViewById(R.id.main_IMG_rock30), findViewById(R.id.main_IMG_rock31), findViewById(R.id.main_IMG_rock32)),
            arrayOf(findViewById(R.id.main_IMG_rock40), findViewById(R.id.main_IMG_rock41), findViewById(R.id.main_IMG_rock42)),
            arrayOf(findViewById(R.id.main_IMG_rock50), findViewById(R.id.main_IMG_rock51), findViewById(R.id.main_IMG_rock52)),
            arrayOf(findViewById(R.id.main_IMG_rock60), findViewById(R.id.main_IMG_rock61), findViewById(R.id.main_IMG_rock62))
        )
    }

    private fun initViews() {
        main_FAB_right.setOnClickListener { view: View -> move(1) }
        main_FAB_left.setOnClickListener { view: View -> move(-1) }
        refreshUI()
    }

    private fun move(direction: Int) {
        gameManager.checkMove(direction)
        refreshUI()
    }

    private fun refreshUI() {
        if (gameManager.hits != 0) {
            main_IMG_hearts[main_IMG_hearts.size - gameManager.hits]
                .visibility = View.INVISIBLE
        }

        for (i in main_IMG_spaceships.indices) {
            if (i == gameManager.spaceshipPos) {
                main_IMG_spaceships[i].visibility = View.VISIBLE
            } else {
                main_IMG_spaceships[i].visibility = View.INVISIBLE
            }
        }

        for (i in 0 until gameManager.ROWS) {
            for (j in 0 until gameManager.COLS) {
                if (gameManager.rockBoard[i][j] == 1) {
                    main_IMG_rocks[i][j].visibility = View.VISIBLE
                } else {
                    main_IMG_rocks[i][j].visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun startTimer() {
        if (!timerStarted) {
            timerStarted = true
            timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    runOnUiThread {
                        game()
                    }
                }
            }, 0, 1200)
        }
    }

    private fun game(){
        gameManager.rockFalls()

        if (gameManager.checkHit()) { vibrateAndToast() }
        refreshUI()

        if (gameManager.isGameOver) {
            reset()
        }
    }

    private fun vibrateAndToast(){
        SignalManager.getInstance().toast("collision! \uD83D\uDCA5")
        SignalManager.getInstance().vibrate()
    }

    private fun reset(){
        gameManager.resetBoards()

        for (i in main_IMG_hearts.indices){
            main_IMG_hearts[i].visibility = View.VISIBLE
        }
        refreshUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::timer.isInitialized) {
            timer.cancel()
        }
    }
}