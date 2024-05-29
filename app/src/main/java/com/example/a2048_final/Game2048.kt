package com.example.a2048_final

import kotlin.random.Random

// Clase que representa el juego 2048
class Game2048(private val size: Int = 4) {
    // Tablero del juego, representado por una matriz bidimensional de enteros
    val board: Array<Array<Int>> = Array(size) { Array(size) { 0 } }
    private var score = 0

    // Inicializador que agrega dos fichas aleatorias al tablero al comenzar el juego
    init {
        addRandomTile()
        addRandomTile()
    }

    // Método para obtener el puntaje actual del juego
    fun getScore(): Int {
        return score
    }

    // Método para reiniciar el juego, limpiando el tablero y agregando dos fichas aleatorias
    fun resetGame() {
        for (i in 0 until size) {
            for (j in 0 until size) {
                board[i][j] = 0
            }
        }
        score = 0
        addRandomTile()
        addRandomTile()
    }

    // Método para agregar una nueva ficha en una posición aleatoria vacía del tablero
    private fun addRandomTile() {
        // Lista de todas las posiciones vacías
        val emptyTiles = mutableListOf<Pair<Int, Int>>()
        for (i in 0 until size) {
            for (j in 0 until size) {
                if (board[i][j] == 0) {
                    emptyTiles.add(Pair(i, j))
                }
            }
        }

        // Si hay posiciones vacías, agrega una ficha con valor 2 o 4 en una de ellas
        if (emptyTiles.isNotEmpty()) {
            val (row, col) = emptyTiles[Random.nextInt(emptyTiles.size)]
            board[row][col] = if (Random.nextInt(100) < 90) 2 else 4
        }
    }

    // Método para rotar el tablero en sentido horario
    private fun rotateBoardClockwise() {
        val newBoard = Array(size) { Array(size) { 0 } }
        for (i in 0 until size) {
            for (j in 0 until size) {
                newBoard[j][size - 1 - i] = board[i][j]
            }
        }
        for (i in 0 until size) {
            for (j in 0 until size) {
                board[i][j] = newBoard[i][j]
            }
        }
    }

    // Método para mover todas las fichas a la izquierda, combinando las fichas iguales
    private fun moveLeft(): Boolean {
        var moved = false
        for (i in 0 until size) {
            val newRow = mutableListOf<Int>()
            // Filtra las fichas no vacías en la fila actual
            for (j in 0 until size) {
                if (board[i][j] != 0) {
                    newRow.add(board[i][j])
                }
            }
            // Combina las fichas iguales adyacentes
            for (j in 0 until newRow.size - 1) {
                if (newRow[j] == newRow[j + 1]) {
                    newRow[j] *= 2
                    score += newRow[j]
                    newRow[j + 1] = 0
                }
            }
            // Compacta la fila y la rellena con ceros al final
            val newRowCompact = newRow.filter { it != 0 }.toMutableList()
            while (newRowCompact.size < size) {
                newRowCompact.add(0)
            }
            // Actualiza el tablero y marca si hubo algún movimiento
            for (j in 0 until size) {
                if (board[i][j] != newRowCompact[j]) {
                    moved = true
                    board[i][j] = newRowCompact[j]
                }
            }
        }
        return moved
    }

    // Método para mover las fichas en la dirección especificada
    fun move(direction: String): Boolean {
        var moved = false
        when (direction) {
            "LEFT" -> moved = moveLeft()
            "RIGHT" -> {
                rotateBoardClockwise()
                rotateBoardClockwise()
                moved = moveLeft()
                rotateBoardClockwise()
                rotateBoardClockwise()
            }
            "UP" -> {
                rotateBoardClockwise()
                rotateBoardClockwise()
                rotateBoardClockwise()
                moved = moveLeft()
                rotateBoardClockwise()
            }
            "DOWN" -> {
                rotateBoardClockwise()
                moved = moveLeft()
                rotateBoardClockwise()
                rotateBoardClockwise()
                rotateBoardClockwise()
            }
        }
        // Si hubo algún movimiento, agrega una nueva ficha aleatoria
        if (moved) {
            addRandomTile()
        }
        return moved
    }

    // Método para verificar si el juego ha terminado
    fun isGameOver(): Boolean {
        // Verifica si hay algún movimiento posible
        for (i in 0 until size) {
            for (j in 0 until size) {
                if (board[i][j] == 0) return false
                if (i < size - 1 && board[i][j] == board[i + 1][j]) return false
                if (j < size - 1 && board[i][j] == board[i][j + 1]) return false
            }
        }
        return true
    }

    // Método para verificar si el jugador ha ganado
    fun hasWon(): Boolean {
        for (i in 0 until size) {
            for (j in 0 until size) {
                if (board[i][j] == 2048) return true
            }
        }
        return false
    }
}