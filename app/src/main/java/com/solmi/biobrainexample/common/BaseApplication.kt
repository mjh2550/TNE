package com.solmi.biobrainexample.common

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatDialog
import androidx.core.content.ContextCompat
import com.solmi.biobrainexample.R
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

    companion object{
        fun progressON(context: Context){
            var progressDialog = AppCompatDialog(context)
            progressDialog.setCancelable(false)
            progressDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progressDialog.setContentView(R.layout.dialog_progress)
            progressDialog.show()
        }
        fun progressOFF(context: Context){
            var progressDialog = AppCompatDialog(context)
            if(progressDialog != null && progressDialog.isShowing()){
                progressDialog.dismiss()
            }
        }
    }

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

    /**
     * 2022.07.18
     * Moving Average (Int)
     */

    fun movingAverage(totalScore : Int,provideList : ArrayList<Int>) : ArrayList<Int>{
        val m: Int = totalScore
        val list: ArrayList<Int> = provideList
        val n = list.size
        val resultList = ArrayList<Int>()
        
        var sum = 0

        for (i in 0 until m - 1) {
            sum += list[i] //m-1까지의 부분sum
        }

        for (i in m - 1 until n) {
            sum += list[i]
            resultList.add(sum / m)
            sum -= list[i - m + 1]
        }

        for(i in resultList){
            println("average is $i")
        }
        return  resultList
    }

}