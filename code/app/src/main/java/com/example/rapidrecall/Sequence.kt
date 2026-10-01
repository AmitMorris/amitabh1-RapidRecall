package com.example.rapidrecall


/**
 * Input: paramLength (length of the sequence)
 * This class creates and stores a sequence of length integers
 */
class Sequence(var paramLength:Int) {
    // makes sure the length is between 1 and 10
    private val length: Int = paramLength.coerceIn(1, 10)

    // stores the sequence
    private val sequenceValue = ArrayList<Int>()

    // initializes the sequence
    init {
        for (i in 1..length) {
            sequenceValue.add((0..9).random())
        }
    }

    /**
     * Returns the sequence as a string
     */
    fun getSequence():String {
        return sequenceValue.joinToString(separator = "")
    }
}