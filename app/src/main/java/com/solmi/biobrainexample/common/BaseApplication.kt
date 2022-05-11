package com.solmi.biobrainexample.common

import android.Manifest
import android.app.Activity
import android.app.Application
import android.bluetooth.BluetoothDevice
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.AdapterView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.solmi.biobrainexample.DeviceListAdapter
import com.solmi.biobrainexample.R
import com.solmi.biobrainexample.Simple1ChannelGraph
import com.solmi.biobrainexample.Simple3ChannelGraph
import com.solmi.ble.BLECommManager
import com.solmi.ble.BLEDefine
import com.solmi.ble.BTScanEvent
import com.solmi.ble.BTStateEvent
import com.solmi.bluetoothlibrary.common.BTDataDefine
import com.solmi.uxprotocol.HeaderPacket
import com.solmi.uxprotocol.UxParserEvent
import com.solmi.uxprotocol.UxProtocol
import java.util.*
import java.util.concurrent.LinkedBlockingQueue

class BaseApplication {

    companion object{

        private const val PERMISSION_REQUEST_CODE :Int = 100
        /**
         * 블루투스 검색 이벤트 핸들러
         */
        private var mBTScanEventHandler: BTScanEvent? = null

        /**
         * 블루투스 상태 변화 이벤트 핸들러
         */
        private var mBTStateEventHandler: BTStateEvent? = null

        /**
         * 데이터 파싱 이벤트 핸들러
         */
        private var mUxParserEventHandler: UxParserEvent? = null

        /**
         * 디바이스 리스트 뷰 아이템 클릭 이벤트 핸들러
         */
        private var mItemClickListener: AdapterView.OnItemClickListener? = null

        /**
         * 블루투스 통신 클래스
         */
        private var mBLEManager: BLECommManager? = null

        /**
         * 검색된 디바이스 리스트 뷰 어댑터
         */
        private var mDeviceListAdapter: DeviceListAdapter? = null

        /**
         * EMG 데이터 버퍼
         */
        private var mEMGBuffer: Queue<IntArray>? = null

        /**
         * Acc 데이터 버퍼
         */
        private var mAccBuffer: Queue<IntArray>? = null

        /**
         * Gyro 데이터 버퍼
         */
        private var mGyroBuffer: Queue<IntArray>? = null

        /**
         * Magneto 데이터 버퍼
         */
        private var mMagnetoBuffer: Queue<IntArray>? = null

        /**
         * 데이터 업데이트 타이머
         */
        private var mDataUpdateTimer: Timer? = null

        /**
         * 측정 시작 시간
         */
        private var mStartTime: Long = 0

        /**
         * 수신한 EMG 데이터 수
         */
        private var mEMGCount = 0

        /**
         * 수신한 Acc 데이터 수
         */
        private var mAccCount = 0

        /**
         * 수신한 Gyro 데이터 수
         */
        private var mGyroCount = 0

        /**
         * 수신한 Magneto 데이터 수
         */
        private var mMagnetoCount = 0


        var mSGEMGGraph: Simple1ChannelGraph? = null
        var mSGAccGraph: Simple3ChannelGraph? = null
        var mSGGyroGraph: Simple3ChannelGraph? = null
        var mSGMagnetoGraph: Simple3ChannelGraph? = null
        var mRGSamplingRate: RadioGroup? = null

    }

    private fun requestPermission(activity: Activity) {

        val needPermissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        ActivityCompat.requestPermissions(activity, needPermissions, PERMISSION_REQUEST_CODE)

    }

    /**
     * 권한 설정되었는지 확인하는 함수
     * @return 권한 설정 여부
     */
    private fun checkPermission(context: Context): Boolean {
        var locationPermissionCheck =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (locationPermissionCheck == PackageManager.PERMISSION_DENIED)
            return false

        locationPermissionCheck =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        if(locationPermissionCheck == PackageManager.PERMISSION_DENIED)
            return false

        return true
    }


    /**
     * 구성 요소들 초기화하는 함수
     */
    private fun initComponent(context: Context) {

        mBLEManager = BLECommManager(context)
        mBLEManager!!.registerBTScanEventHandler(mBTScanEventHandler)
        mBLEManager!!.registerBTStateEventHandler(mBTStateEventHandler)
        mBLEManager!!.registerParserEventHandler(mUxParserEventHandler)
        val isBLESupport: Boolean = mBLEManager!!.checkIsBLESupport()
        if (isBLESupport) {
            if (mBLEManager!!.startBLEService() == false) {
                Toast.makeText(
                    context,
                    R.string.error_ble_start_service,
                    Toast.LENGTH_SHORT
                ).show()
                mBLEManager = null
                return
            }
        } else {
            Toast.makeText(context, R.string.error_not_support_ble, Toast.LENGTH_SHORT)
                .show()
        }

        mEMGBuffer = LinkedBlockingQueue<IntArray>()
        mAccBuffer = LinkedBlockingQueue<IntArray>()
        mGyroBuffer = LinkedBlockingQueue<IntArray>()
        mMagnetoBuffer = LinkedBlockingQueue<IntArray>()
    }


}