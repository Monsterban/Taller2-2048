package com.example.a2048_final

import android.os.Bundle
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var game: Game2048
    private lateinit var scoreTextView: TextView
    private lateinit var gridLayout: GridLayout
    private val tileViews: Array<Array<TextView?>> = Array(4) { arrayOfNulls(4) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        game = Game2048()
        scoreTextView = findViewById(R.id.score)
        gridLayout = findViewById(R.id.gameGrid)

        // Inicializa las vistas de las fichas en el GridLayout
        for (i in 0 until gridLayout.childCount) {
            val row = i / 4
            val col = i % 4
            tileViews[row][col] = gridLayout.getChildAt(i) as TextView
        }

        // Muestra el estado inicial del tablero
        updateBoard()

        // Manejo de los gestos de deslizar
        val swipeListener = object : OnSwipeTouchListener(this) {
            override fun onSwipeLeft() {
                if (game.move("LEFT")) onMove()
            }

            override fun onSwipeRight() {
                if (game.move("RIGHT")) onMove()
            }

            override fun onSwipeUp() {
                if (game.move("UP")) onMove()
            }

            override fun onSwipeDown() {
                if (game.move("DOWN")) onMove()
            }
        }
        gridLayout.setOnTouchListener(swipeListener)
    }

    // Método para actualizar el tablero y el puntaje en la interfaz de usuario
    private fun updateBoard() {
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                val value = game.board[i][j]
                tileViews[i][j]?.text = if (value == 0) "" else value.toString()
                tileViews[i][j]?.setBackgroundResource(getTileBackground(value))
            }
        }
        scoreTextView.text = "Score: ${game.getScore()}"
    }

    // Método para obtener el fondo adecuado para cada valor de ficha
    private fun getTileBackground(value: Int): Int {
        return when (value) {
            0 -> R.drawable.tile_background
            2 -> R.drawable.tile_background_2
            4 -> R.drawable.tile_background_4
            8 -> R.drawable.tile_background_8
            16 -> R.drawable.tile_background_16
            32 -> R.drawable.tile_background_32
            64 -> R.drawable.tile_background_64
            128 -> R.drawable.tile_background_128
            256 -> R.drawable.tile_background_256
            512 -> R.drawable.tile_background_512
            1024 -> R.drawable.tile_background_1024
            2048 -> R.drawable.tile_background_2048
            else -> R.drawable.tile_background
        }
    }

    // Método llamado después de cada movimiento
    private fun onMove() {
        updateBoard()
        if (game.hasWon()) {
            Toast.makeText(this, "You won!", Toast.LENGTH_SHORT).show()
        }
        if (game.isGameOver()) {
            Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT).show()
        }
    }
}