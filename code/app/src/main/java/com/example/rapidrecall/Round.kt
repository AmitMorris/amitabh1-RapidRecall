package com.example.rapidrecall

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Input: paramLength (length of the sequence)
 * Stores all the information about an attempt
 */
class Round(val paramLength: Int) {
    // makes sure the length is between 1 and 10
    private val length: Int = paramLength.coerceIn(1, 10)
    // stores the sequence for this attempt
    private val sequence = Sequence(length)
    // stores the guess the user gives it
    private var guess: Long = 0

    // stores the timestamp
    val timestamp:Long = System.currentTimeMillis()

    /**
     * Input currGuess:Long (what the user thinks the sequence is)
     * sets guess
     * Problem: since the guess is stored as a long but sequence is
     * stored as a string, leading 0s don't show up for the guess
     * but do show up for the original sequence
     */
    fun setGuess(currGuess:Long) {
        guess = currGuess.coerceAtLeast(0L)
    }

    /**
     * returns guess as a long
     * Problem: the original sequence is displayed as a string
     */
    fun getGuess():Long {
        return guess
    }

    /**
     * Gets the timestamp based on the amount of milliseconds that have
     * happened since 1970
     * Problem: it's off by 6 hours but so is the phone
     */
    fun getTimestamp(): String {
        val time = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
        return time.format(Date(timestamp))
    }

    /**
     * returns if the guess is the same as the original sequence
     */
    fun compare(): Boolean {
        return guess == sequence.getSequence().toLong()
    }

    /**
     * returns the sequence
     * Problem: has the same name as the Sequence class but that's
     * just because the name makes sense for both
     */
    fun getSequence():Sequence {
        return sequence
    }

    /**
     * returns the attempts information as a string
     * problem, attempts are supposed to line up with eachother,
     * but they don't because different chars take up different amounts
     * of space
     */
    override fun toString():String {
        val status = if(compare()) "WIN" else "LOSE"
        val time = getTimestamp().padEnd(12)
        val strLen = "Len: $length".padEnd(8)
        val strSequence = "Original: ${sequence.getSequence()}".padEnd(20)
        val strGuess = "Guess: ${guess.toString()}".padEnd(18)
        val result = status.padStart(5)

        return "$time|$strLen|$strSequence|$strGuess|$result"

    }
}