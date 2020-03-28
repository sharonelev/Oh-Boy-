package com.appsbysha.ohboy.interfaces

import com.appsbysha.ohboy.entities.Line

interface DataReadyListener {
    fun onSuccess(list: List<Line>){

    }
}