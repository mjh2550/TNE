package com.solmi.biobrainexample.common

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
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

    /**
     * 권한 설정되었는지 확인하는 함수
     * @return 권한 설정 여부
     */
    fun checkPermission(context: Context): Boolean {
        var locationPermissionCheck =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (locationPermissionCheck == PackageManager.PERMISSION_DENIED)
            return false

        locationPermissionCheck =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        if(locationPermissionCheck == PackageManager.PERMISSION_DENIED)
            return false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            var blueToothPermissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH)
            if(blueToothPermissionCheck == PackageManager.PERMISSION_DENIED)
                return false
            blueToothPermissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT)
            if(blueToothPermissionCheck == PackageManager.PERMISSION_DENIED)
                return false
            blueToothPermissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN)
            if(blueToothPermissionCheck == PackageManager.PERMISSION_DENIED)
                return false
            blueToothPermissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADVERTISE)
            if(blueToothPermissionCheck == PackageManager.PERMISSION_DENIED)
                return false

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val blueToothPermissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH)
            if(blueToothPermissionCheck == PackageManager.PERMISSION_DENIED)
                return false
        }
        return true
    }
}