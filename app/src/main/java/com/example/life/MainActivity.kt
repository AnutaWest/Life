package com.example.life

import Game
import GameField
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.life.databinding.ActivityMainBinding
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var game: Game? = null
    private var executor = Executors.newSingleThreadScheduledExecutor()
    private var timeout = 100L

    private val settingsVm: SettingsViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingsVm.height.value = binding.game.field.height
        settingsVm.width.value = binding.game.field.width


        binding.game.setOnTileTouchListener { x: Int, y: Int ->
            binding.game.field[x, y] = !binding.game.field[x, y]
            binding.game.postInvalidate()
        }

        binding.toggleStartStop.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                runGame()
            } else {
                stopGame()
            }
        }

        binding.buttonSettings.setOnClickListener {
            onPause()
            val dialog = SettingsDialog()
            dialog.show(supportFragmentManager, "SettingsDialog")
        }



        settingsVm.height.observe(this) {
            if(it != binding.game.field.height) {
                binding.game.field = GameField(binding.game.field.width, it)
            }
        }
        settingsVm.width.observe(this) {
            if(it != binding.game.field.width) {
                binding.game.field = GameField(it, binding.game.field.height)
            }
        }
        settingsVm.speed.observe(this) {
            if(it < 1) {
                timeout = 600L
            } else {
                timeout = 50L + 500L / it
            }
        }

    }

    override fun onPause() {
        binding.toggleStartStop.isChecked = false
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