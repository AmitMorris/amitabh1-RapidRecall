package com.example.rapidrecall

import androidx.compose.runtime.mutableStateListOf
import kotlin.math.roundToInt

/**
 * contains a list of rounds to store the info about each attempt
 */
class Summary {
    // List of attempts, tried to make a getter for it but it didnt work
    val rounds = mutableStateListOf<Round>()

    /**
     * returns the total amount of attempts using the size of the
     * rounds List
     */
    fun totalAttempts():Int{
        return rounds.size
    }

    /**
     * returns the total amount of successful attempts by looping
     * through the list and incrementing a counter whenever it
     * finds a successful attempt
     */
    fun successfulAttempts():Int{
        var successes = 0
        for(round in rounds) {
            if (round.compare()) {
                successes++
            }
        }
        return successes
    }

    /**
     * returns the percentage of attempts where the user gave the
     * correct sequence
     * does this as an int because I could not figure out how to
     * limit the amount of digits it would put after the decimal
     */
    fun accuracyPercentage(): Int{
        if (totalAttempts() > 0) {
            return (100.0 * successfulAttempts().toDouble() / totalAttempts().toDouble()).roundToInt()
        }
        return 0
    }

    /**
     * adds a round to the list
     */
    fun addRound(round: Round) {
        rounds.add(round)
    }
}