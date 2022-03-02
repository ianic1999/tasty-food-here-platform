package com.example.tfhmobile.dto

class TableDTO constructor(private val id: Long,
                           private val ordinalNumber: Int,
                            private val nrOfSPots: Int) {
    fun getId(): Long {
        return id
    }

    fun getOrdinalNumber(): Int {
        return ordinalNumber
    }

    fun getNrOfSpots(): Int {
        return nrOfSPots
    }
}