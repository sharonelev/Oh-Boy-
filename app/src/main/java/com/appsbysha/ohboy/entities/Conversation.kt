package com.appsbysha.ohboy.entities

import java.text.SimpleDateFormat
import java.util.*

class Conversation(val child: Child, var lineList: MutableList<Any>, val sayingDate: String, var childPhoto: Byte, val id: Int) {
    var sdf = SimpleDateFormat("dd/MM/yyyy")
    var age: String = ""

    init {

        try {
            val date = sdf.parse(sayingDate)
            val childDob = sdf.parse(child.dob)
            val cal = Calendar.getInstance()
            cal.time = date
            val calDob = Calendar.getInstance()
            calDob.time = childDob
            val msdiff = cal.timeInMillis - calDob.timeInMillis
            cal.timeInMillis = msdiff
            val diffYears = (cal[Calendar.YEAR] - 1970).toString()
            val diffMonths = cal[Calendar.MONTH].toString()
            val diffDays = cal[Calendar.DAY_OF_MONTH].toString()
            age = ("$diffYears years, $diffMonths months and $diffDays days old")

        } catch (e: java.text.ParseException) {
            e.printStackTrace()
        }
    }
}