package com.solmi.biobrainexample.common

import android.app.Application
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import com.solmi.biobrainexample.bio.data.BioRepository
import com.solmi.biobrainexample.bio.data.BioRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class BaseApplication : Application(){
    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { BioRoomDatabase.getDatabase(this,applicationScope) }

    val repository by lazy { BioRepository(database.bioDao()) }

   /* @RequiresApi(Build.VERSION_CODES.N)
    fun getCurrentTime(): String {
        var format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return format.format(System.currentTimeMillis())
    }*/
}