package com.appsbysha.ohboy.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

data class SayingWithSentences (
    @Embedded val saying:Saying,
            @Relation(
                    parentColumn = "sayingId",
                    entityColumn = "sentenceSayingId"
            )
            val sentence: List<Sentence>
)