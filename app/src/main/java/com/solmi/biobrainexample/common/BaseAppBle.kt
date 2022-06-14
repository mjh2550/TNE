package com.solmi.biobrainexample.common

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import java.util.*

/**
 * BlueTooth Setting View Interface
 * 2022.05.11
 */
interface BaseAppBle {
    companion object{
        private var bleSetData : BleSetData? = null
        private lateinit var context: Context
        fun getInstance(_context : Context):BleSetData{
            return bleSetData ?: synchronized(this){
                bleSetData ?: BleSetData().also {
                    context = _context
                    bleSetData = it
                }
            }
        }
    }
    /**
     * 이벤트 핸들러들 초기화하는 함수
     */
    fun initHandler() {
        initBTScanEventHandler()
        initBTStateEventHandler()
        initUxParserEventHandler()
        initItemClickListener()
    }

    /**
     * 디바이스 리스트 뷰 아이템 클릭 이벤트 핸들러 초기화하는 함수
     */
    fun initItemClickListener()

    /**
     * 데이터 파싱 이벤트 핸들러 초기화하는 함수
     */
    fun initUxParserEventHandler()

    /**
     * 데이터 업데이트 타이머 시작하는 함수
     */
    fun startDataUpdateTimer()

    /**
     * 데이터 업데이트 타이머 태스크 반환하는 함수
     * @return 타이머 태스크
     */
    fun getDataUpdateTimerTask() : TimerTask?

    /**
     * 데이터 업데이트 타이머 종료하는 함수
     */
    fun stopDataUpdateTimer()

    /**
     * 블루투스 상태 변화 이벤트 핸들러 초기화하는 함수
     */
    fun initBTStateEventHandler()

    /**
     * 블루투스 검색 이벤트 핸들러 초기화하는 함수
     */
    fun initBTScanEventHandler()

    /**
     * 구성요소 초기화하는 함수
     */
    fun resetComponent() {
        bleSetData?.mEMGBuffer!!.clear()
        bleSetData?.mAccBuffer!!.clear()
        bleSetData?.mGyroBuffer!!.clear()
        bleSetData?.mMagnetoBuffer!!.clear()
        bleSetData?.mStartTime = 0
        bleSetData?.mEMGCount = 0
        bleSetData?.mAccCount = 0
        bleSetData?.mGyroCount = 0
        bleSetData?.mMagnetoCount = 0
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
}