package com.appsbysha.ohboy.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saying_table")
class Saying(@PrimaryKey
             var sayingId: String, var childId: Int, var title: String?, var date: String?, var photo: ByteArray?) {

    constructor(sayingId: String, childId: Int) : this(sayingId, childId, null, null, null)

    var age: String? = null

}