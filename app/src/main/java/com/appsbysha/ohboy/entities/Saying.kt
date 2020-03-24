package com.appsbysha.ohboy.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saying_table")
class Saying(var childId: Int, var title: String?, var date: String?, var photo: ByteArray?) {
    @PrimaryKey(autoGenerate = true)
    var sayingId = 0
    var age: String? = null

}