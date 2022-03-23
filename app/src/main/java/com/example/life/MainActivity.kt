package com.example.life

import Game
import GameField
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.life.databinding.ActivityMainBinding
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var game: Game? = null
    private var executor = Executors.newSingleThreadScheduledExecutor()
    private val timeout = 100L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.game.setOnTileTouchListener { x: Int, y: Int ->
            binding.game.field[x, y] = !binding.game.field[x, y]
            binding.game.postInvalidate()
        }

        binding.startStop.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                runGame()
            } else {
                stopGame()
            }
        }

    }

    override fun onPause() {
        binding.startStop.isChecked = false
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable("gameField", binding.game.field)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        val field: GameField? = savedInstanceState.getParcelable("gameField")
        if(field != null) {
            binding.game.field = field
        }
        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun stopGame() {
        executor.shutdownNow()
        game = null
        executor = Executors.newSingleThreadScheduledExecutor()
    }

    private fun runGame() {
        game = Game(binding.game.field)
        executor.scheduleWithFixedDelay({
            game!!.nextGeneration()
            binding.game.field = game!!.field
        }, 0, timeout, TimeUnit.MILLISECONDS)
    }
}