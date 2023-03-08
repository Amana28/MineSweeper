package com.example.amana_minesweeper

import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.amana_minesweeper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ListenerInterface {

    companion object {
        const val TIMER_END: Long = 999000L
        const val NUM_BOMBS = 10
        const val GRID_SIZE = 10
    }

    private lateinit var binding: ActivityMainBinding


    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var smiley: TextView
    private lateinit var timer: TextView
    private lateinit var flag: TextView
    private lateinit var flagsCounter: TextView
    private lateinit var mineSweeperGame: MinesweeperView
    private lateinit var countDownTimer: CountDownTimer
    private var secondsElapsed = 0
    private var timerStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val grid = binding.gridView
        grid.layoutManager = GridLayoutManager(this, 10)

        timer = binding.timerView
        timerStarted = false
        countDownTimer = object : CountDownTimer(TIMER_END, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                secondsElapsed += 1
                timer.text = String.format("%03d", secondsElapsed)
            }

            override fun onFinish() {
                mineSweeperGame.outOfTime()
                Toast.makeText(applicationContext, "Game Over: Timer Expired", Toast.LENGTH_SHORT)
                    .show()
                mineSweeperGame.getMineGrid().revealAllBombs()
                recyclerViewAdapter.setCells(mineSweeperGame.getMineGrid().getCells())
            }
        }

        flagsCounter = binding.flagCounterView

        mineSweeperGame = MinesweeperView(GRID_SIZE, NUM_BOMBS)
        flagsCounter.text =
            String.format("%03d", mineSweeperGame.getNumberBombs() - mineSweeperGame.getFlagCount())
        recyclerViewAdapter = RecyclerViewAdapter(mineSweeperGame.getMineGrid().getCells(), this)
        grid.adapter = recyclerViewAdapter

        smiley = binding.smileyView
        smiley.setOnClickListener {
            mineSweeperGame = MinesweeperView(GRID_SIZE, NUM_BOMBS)
            recyclerViewAdapter.setCells(mineSweeperGame.getMineGrid().getCells())
            timerStarted = false
            countDownTimer.cancel()
            secondsElapsed = 0
            timer.setText(R.string.counter)
            flagsCounter.text =
                String.format("%03d", mineSweeperGame.getNumberBombs() - mineSweeperGame.getFlagCount())
        }

        flag = binding.flagButton
        flag.setOnClickListener {
            mineSweeperGame.toggleMode()
            val border1 = GradientDrawable()
            border1.setColor(0xFFFFFFFF.toInt())
            border1.setStroke(1, 0xFF000000.toInt())
            val border2 = GradientDrawable()
            border2.setColor(0x000000.toInt())
            border2.setStroke(1, 0xFF000000.toInt())
            if(mineSweeperGame.isClearMode()) {
                flag.background = border1
            }
            else {
                flag.background = border2
            }
        }
    }

     override fun clickCell(cell: Cell) {
        mineSweeperGame.handleCellClick(cell)

        flagsCounter.text =
            String.format("%03d", mineSweeperGame.getNumberBombs() - mineSweeperGame.getFlagCount())

        if (!timerStarted) {
            countDownTimer.start()
            timerStarted = true
        }

        if (mineSweeperGame.isGameOver()) {
            countDownTimer.cancel()
            Toast.makeText(applicationContext, "Game Over", Toast.LENGTH_SHORT).show()
            mineSweeperGame.getMineGrid().revealAllBombs()
        }

        if (mineSweeperGame.isGameWon()) {
            countDownTimer.cancel()
            Toast.makeText(applicationContext, "Game Won", Toast.LENGTH_SHORT).show()
            mineSweeperGame.getMineGrid().revealAllBombs()
        }

        recyclerViewAdapter.setCells(mineSweeperGame.getMineGrid().getCells())
    }

}