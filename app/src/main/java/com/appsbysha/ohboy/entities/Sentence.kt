package com.appsbysha.ohboy.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sentences_table")
data class Sentence (
        @PrimaryKey(autoGenerate = true)
        val sentenceId: Int,
    var description: String,
    val sentenceSayingId: Int,
    var isBracket: Boolean)

{

}


