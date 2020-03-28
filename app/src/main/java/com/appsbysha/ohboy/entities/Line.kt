package com.appsbysha.ohboy.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lines_table", primaryKeys = ["lineId", "lineSayingId"])
data class Line(
        val lineId: String,
        val lineSayingId: String,
        var position: Int,
        var description: String,
        var type: Int,
        var other_person: String?
) {

}


