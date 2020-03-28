package com.appsbysha.ohboy.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

data class SayingWithLines (
    @Embedded val saying:Saying,
            @Relation(
                    parentColumn = "sayingId",
                    entityColumn = "lineSayingId"
            )
            val line: List<Line>
)