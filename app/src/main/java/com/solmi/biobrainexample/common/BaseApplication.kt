package com.solmi.biobrainexample.common

import android.app.Application
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi

class BaseApplication : Application(){

    @RequiresApi(Build.VERSION_CODES.N)
    fun getCurrentTime(): String {
        var format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return format.format(System.currentTimeMillis())
    }
}