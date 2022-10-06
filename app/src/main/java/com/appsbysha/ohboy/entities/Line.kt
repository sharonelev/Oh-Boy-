package com.appsbysha.ohboy.entities

import androidx.room.Entity

@Entity(tableName = "lines_table", primaryKeys = ["lineId", "lineSayingId"])
data class Line(
        val lineId: String,
        val lineSayingId: String,
        var position: Int,
        var description: String,
        var type: Int,
        var other_person: String?,
        var childPic: ByteArray?
) {
        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as Line

                if (childPic != null) {
                        if (other.childPic == null) return false
                        if (!childPic.contentEquals(other.childPic)) return false
                } else if (other.childPic != null) return false

                return true
        }

        override fun hashCode(): Int {
                return childPic?.contentHashCode() ?: 0
        }

}


