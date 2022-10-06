package com.appsbysha.ohboy

import android.widget.Toast

class UiUtils {

    companion object{

        fun dateFormatter(dd :String, MM: String, yyyy: String): String?{
            var ddString = dd
            var MMString = MM
            var yyyyString = yyyy

            if (ddString.length == 1) ddString = "0$ddString"

            if (MMString.length == 1) MMString = "0$MMString"

            if (ddString.isEmpty() || MMString.isEmpty() ||yyyyString.isEmpty() || ddString.toInt() > 31 || ddString.toInt() == 0 || MMString.toInt() > 12 || MMString.toInt() == 0 || yyyyString.length < 4
            ) {
                return null
            }

           return "$ddString/$MMString/$yyyyString"
        }

    }
}