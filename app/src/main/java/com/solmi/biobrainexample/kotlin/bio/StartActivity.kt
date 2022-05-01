package com.solmi.biobrainexample.kotlin.bio

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.solmi.biobrainexample.DeviceListAdapter
import com.solmi.biobrainexample.R
import com.solmi.biobrainexample.Simple1ChannelGraph
import com.solmi.biobrainexample.Simple3ChannelGraph
import com.solmi.biobrainexample.kotlin.data.MesureData
import com.solmi.ble.BLECommManager
import com.solmi.ble.BLEDefine.BluetoothState
import com.solmi.ble.BTScanEvent
import com.solmi.ble.BTStateEvent
import com.solmi.bluetoothlibrary.common.BTDataDefine
import com.solmi.uxprotocol.HeaderPacket
import com.solmi.uxprotocol.UxParserEvent
import com.solmi.uxprotocol.UxProtocol
import java.util.*
import java.util.concurrent.LinkedBlockingQueue


class StartActivity : AppCompatActivity() ,View.OnClickListener {

    lateinit var navController: NavController
    lateinit var navHostFragment: NavHostFragment

    private val PERMISSION_REQUEST_CODE :Int = 100

    lateinit var mesureData: MesureData

    /*var  mesureData.mTVLogTextView: TextView? = null
    var mBtnScan: Button? = null
    var mBtnStart: Button? = null
    var mBtnStop: Button? = null
    var mBtnDisconnect: Button? = null
    var mLVDeviceList: ListView? = null

    var mSGEMGGraph: Simple1ChannelGraph? = null
    var mSGAccGraph: Simple3ChannelGraph? = null
    var mSGGyroGraph: Simple3ChannelGraph? = null
    var mSGMagnetoGraph: Simple3ChannelGraph? = null
    var mRGSamplingRate: RadioGroup? = null

    *//**
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
    private var mItemClickListener: OnItemClickListener? = null

    /**
     * 블루투스 통신 클래스
     *//*
    private var mBLEManager: BLECommManager? = null

    *//**
     * 검색된 디바이스 리스트 뷰 어댑터
     */
    private var mDeviceListAdapter: DeviceListAdapter? = null

    /**
     * EMG 데이터 버퍼
     *//*
    private var mEMGBuffer: Queue<IntArray>? = null

    *//**
     * Acc 데이터 버퍼
     *//*
    private var mAccBuffer: Queue<IntArray>? = null

    *//**
     * Gyro 데이터 버퍼
     *//*
    private var mGyroBuffer: Queue<IntArray>? = null

    *//**
     * Magneto 데이터 버퍼
     *//*
    private var mMagnetoBuffer: Queue<IntArray>? = null

    *//**
     * 데이터 업데이트 타이머
     *//*
    private var mDataUpdateTimer: Timer? = null

    *//**
     * 측정 시작 시간
     *//*
    private var mStartTime: Long = 0

    *//**
     * 수신한 EMG 데이터 수
     *//*
    private var mEMGCount = 0

    *//**
     * 수신한 Acc 데이터 수
     *//*
    private var mAccCount = 0

    *//**
     * 수신한 Gyro 데이터 수
     *//*
    private var mGyroCount = 0

    *//**
     * 수신한 Magneto 데이터 수
     *//*
    private var mMagnetoCount = 0*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.bio_start_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        initHandler()
        initComponent()

        val isPermissionGranted : Boolean = checkPermission()
        if(isPermissionGranted == false){
            requestPermission()
        }
    }


    private fun requestPermission() {

        val needPermissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        ActivityCompat.requestPermissions(this, needPermissions, PERMISSION_REQUEST_CODE)

    }

    /**
     * 권한 설정되었는지 확인하는 함수
     * @return 권한 설정 여부
    */
    private fun checkPermission(): Boolean {
        var locationPermissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (locationPermissionCheck == PackageManager.PERMISSION_DENIED)
            return false

        locationPermissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if(locationPermissionCheck == PackageManager.PERMISSION_DENIED)
            return false

        return true
    }
    /**
     * 구성 요소들 초기화하는 함수
     */
    private fun initComponent() {
        mesureData = MesureData (
            mTVLogTextView    = findViewById(R.id.tv_mainLogTextView),
            mBtnScan          = findViewById(R.id.btn_mainScan),
            mBtnStart         = findViewById(R.id.btn_mainStart),
            mBtnStop          = findViewById(R.id.btn_mainStop),
            mBtnDisconnect    = findViewById(R.id.btn_mainDisconnect),
            mLVDeviceList     = findViewById(R.id.lv_mainDeviceList),
            mSGEMGGraph       = findViewById(R.id.sg_mainEMGGraph),
            mSGAccGraph       = findViewById(R.id.sg_mainAccGraph),
            mSGGyroGraph      = findViewById(R.id.sg_mainGyroGraph),
            mSGMagnetoGraph   = findViewById(R.id.sg_mainMagnetoGraph),
            mRGSamplingRate   = findViewById(R.id.rg_mainSamplingRate),
            mEMGBuffer = null,
            mAccBuffer = null,
            mGyroBuffer = null,
            mMagnetoBuffer =null,
            mBTScanEventHandler =  null,
            mBTStateEventHandler =  null,
            mUxParserEventHandler =  null,
            mItemClickListener =  null,
            mBLEManager =  null,
            mDataUpdateTimer =  null,
            mStartTime =  0,
            mEMGCount =  0,
            mAccCount =  0,
            mGyroCount =  0,
            mMagnetoCount =  0,

                )
            mesureData.mBtnScan!!.setOnClickListener(this)
            mesureData.mBtnStart!!.setOnClickListener(this)
            mesureData.mBtnDisconnect!!.setOnClickListener(this)
            mesureData.mBtnStop!!.setOnClickListener(this)

            mesureData.mBLEManager = BLECommManager(this)
            mesureData.mBLEManager!!.registerBTScanEventHandler(mBTScanEventHandler)
            mesureData.mBLEManager!!.registerBTStateEventHandler(mBTStateEventHandler)
            mesureData.mBLEManager!!.registerParserEventHandler(mUxParserEventHandler)
            val isBLESupport: Boolean = mesureData.mBLEManager!!.checkIsBLESupport()
            if (isBLESupport) {
                if (mesureData.mBLEManager!!.startBLEService() == false) {
                    Toast.makeText(
                        applicationContext,
                        R.string.error_ble_start_service,
                        Toast.LENGTH_SHORT
                    ).show()
                    mesureData.mBLEManager = null
                    return
                }
            } else {
                Toast.makeText(applicationContext, R.string.error_not_support_ble, Toast.LENGTH_SHORT)
                    .show()
            }
            mesureData.mLVDeviceList!!.setOnItemClickListener(mItemClickListener)
            mesureData.mLVDeviceList!!.setAdapter(mDeviceListAdapter)
            mesureData.mTVLogTextView!!.setMovementMethod(ScrollingMovementMethod())
            mDeviceListAdapter = DeviceListAdapter(this)
    }

   /**
     * 이벤트 핸들러들 초기화하는 함수
     */
    private fun initHandler() {
        initBTScanEventHandler()
        initBTStateEventHandler()
        initUxParserEventHandler()
        initItemClickListener()
    }

    /**
     * 디바이스 리스트 뷰 아이템 클릭 이벤트 핸들러 초기화하는 함수
     */
    private fun initItemClickListener() {
        mItemClickListener =
            OnItemClickListener { adapterView, view, index, l ->
                if ( mesureData.mBLEManager!!.bluetoothState == BluetoothState.STATE_CONNECTED) {
                    Toast.makeText(
                        applicationContext,
                        R.string.error_device_connected,
                        Toast.LENGTH_LONG
                    ).show()
                    return@OnItemClickListener
                }
                mesureData.mBLEManager!!.stopScanDevice()
                val device = mDeviceListAdapter!!.getItem(index)
                mesureData.mTVLogTextView!!.text = "device name: " + device.name
                mesureData.mBLEManager!!.setDeviceType(BTDataDefine.DeviceType.SHC_U4)
                mesureData.mBLEManager!!.setReconnectCount(3)
                mesureData.mBLEManager!!.connect(device)
            }
    }

    /**
     * 데이터 파싱 이벤트 핸들러 초기화하는 함수
     */
    private fun initUxParserEventHandler() {
        mUxParserEventHandler = object : UxParserEvent {
            override fun onParserHeaderPacket(headerPacket: HeaderPacket) {
                when (headerPacket.packetType) {
                    UxProtocol.RES_DAQ_STOP -> runOnUiThread {
                        mesureData.mTVLogTextView!!.append("\nRES_DAQ_STOP")
                        stopDataUpdateTimer()
                    }
                    UxProtocol.RES_DAQ -> runOnUiThread {
                        mesureData.mTVLogTextView!!.append("\nRES_DAQ")
                        startDataUpdateTimer()
                    }
                    UxProtocol.RES_SCALE_SET -> runOnUiThread {
                        var log = ""
                        when (headerPacket.ecgSignalScale) {
                            UxProtocol.SCALE_1X -> log =
                                String.format("\nRES_SCALE_SET: Scale: 1X")
                            UxProtocol.SCALE_2X -> log =
                                String.format("\nRES_SCALE_SET: Scale: 2X")
                            UxProtocol.SCALE_4X -> log =
                                String.format("\nRES_SCALE_SET: Scale: 4X")
                        }
                        mesureData.mTVLogTextView!!.append(log)
                    }
                    UxProtocol.RES_BATT_INFO -> runOnUiThread {
                        val batteryInfo = headerPacket.batteryInfo
                        val log = String.format(
                            "\nRES_BATT_INFO: Max: %.1f Cur: %.1f",
                            batteryInfo.maximumVoltage,
                            batteryInfo.currentVoltage
                        )
                        mesureData.mTVLogTextView!!.append(log)
                    }
                    UxProtocol.RES_FIRM_INFO -> runOnUiThread {
                        val deviceInfo = headerPacket.deviceInfo
                        val log = String.format(
                            "\nRES_FIRM_INFO: Major: %d Minor: %d Build: %d",
                            deviceInfo.majorVersion,
                            deviceInfo.minorVersion,
                            deviceInfo.buildVersion
                        )
                         mesureData.mTVLogTextView!!.append(log)
                    }
                }
            }

            override fun onParserSpecialPacket(type: Byte, value: Byte) {
                runOnUiThread {
                    val log = String.format("SpecialPacket %02X %02X", type, value)
                     mesureData.mTVLogTextView!!.append(log)
                }
            }

            override fun onParserECG(channels: IntArray) {
                mesureData.mEMGCount++
                mesureData.mEMGBuffer!!.offer(channels.clone())
            }

            override fun onParserACC(channels: IntArray) {
                mesureData.mAccCount++
                mesureData.mAccBuffer!!.offer(channels.clone())
            }

            override fun onParserGYRO(channels: IntArray) {
                mesureData.mGyroCount++
                mesureData.mGyroBuffer!!.offer(channels.clone())
            }

            override fun onParserMAGNETO(channels: IntArray) {
                mesureData.mMagnetoCount++
                mesureData.mMagnetoBuffer!!.offer(channels.clone())
            }

            override fun onParserTEMP(temperature: Int) {
                //Not support
            }

            override fun onParserPPG(ppg: Int) {
                //Not support
            }

            override fun onParserStep(step: Int) {
                //Not support
            }

            override fun onError(error: String) {
                Log.e(TAG, error)
            }
        }
    }

    /**
     * 데이터 업데이트 타이머 시작하는 함수
     */
    private fun startDataUpdateTimer() {
        if (mesureData.mDataUpdateTimer == null) {
            mesureData.mStartTime = System.currentTimeMillis()
            mesureData.mDataUpdateTimer = Timer("Data update Timer")
            mesureData.mDataUpdateTimer!!.schedule(getDataUpdateTimerTask(), 0, 25)
        }
    }

    /**
     * 데이터 업데이트 타이머 태스크 반환하는 함수
     * @return 타이머 태스크
     */
    private fun getDataUpdateTimerTask(): TimerTask? {
        return object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    val emgSize = mesureData.mEMGBuffer!!.size
                    for (count in 0 until emgSize) {
                        val channels = mesureData.mEMGBuffer!!.poll()
                        if (channels != null) {
                            val value = channels[0] / 2047f * 7.4f
                            mesureData.mSGEMGGraph!!.putValue(value)
                        }
                    }
                    val accSize = mesureData.mAccBuffer!!.size
                    for (count in 0 until accSize) {
                        val channels = mesureData.mAccBuffer!!.poll()
                        if (channels != null) {
                            val valueArray = FloatArray(3)
                            for (index in 0..2) {
                                valueArray[index] = channels[index] / 1023f * 3f
                            }
                            mesureData.mSGAccGraph!!.putValueArray(valueArray)
                        }
                    }
                    val gyroSize =mesureData.mGyroBuffer!!.size
                    for (count in 0 until gyroSize) {
                        val channels = mesureData.mGyroBuffer!!.poll()
                        if (channels != null) {
                            val valueArray = FloatArray(3)
                            for (index in 0..2) {
                                valueArray[index] = channels[index] / 1023f * 3f
                            }
                            mesureData.mSGGyroGraph!!.putValueArray(valueArray)
                        }
                    }
                    val magnetoSize = mesureData.mMagnetoBuffer!!.size
                    for (count in 0 until magnetoSize) {
                        val channels = mesureData.mMagnetoBuffer!!.poll()
                        if (channels != null) {
                            val valueArray = FloatArray(3)
                            for (index in 0..2) {
                                valueArray[index] = channels[index] / 1023f * 3f
                            }
                            mesureData.mSGMagnetoGraph!!.putValueArray(valueArray)
                        }
                    }
                }
            }
        }
    }

    /**
     * 데이터 업데이트 타이머 종료하는 함수
     */
    private fun stopDataUpdateTimer() {
        if (mesureData.mDataUpdateTimer == null) {
            return
        }
         mesureData.mTVLogTextView!!.append(
            String.format(
                "\nRun time: %.3f(s)",
                (System.currentTimeMillis() - mesureData.mStartTime) / 1000f
            )
        )
         mesureData.mTVLogTextView!!.append(
            String.format(
                "\nEMG: %d Acc: %d Gyro: %d Magneto: %d",
                mesureData.mEMGCount,
                mesureData.mAccCount,
                mesureData.mGyroCount,
                mesureData.mMagnetoCount
            )
        )
        mesureData.mDataUpdateTimer!!.cancel()
        mesureData.mDataUpdateTimer = null
    }

    /**
     * 블루투스 상태 변화 이벤트 핸들러 초기화하는 함수
     */
    private fun initBTStateEventHandler() {
        mBTStateEventHandler = object : BTStateEvent {
            override fun onStateChanged(bluetoothState: BluetoothState) {
                when (bluetoothState) {
                    BluetoothState.STATE_DISCONNECTED -> runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            "onStateChanged: STATE_DISCONNECTED",
                            Toast.LENGTH_SHORT
                        ).show()
                         mesureData.mTVLogTextView!!.append("\nonStateChanged: STATE_DISCONNECTED")
                        mesureData.mBtnDisconnect!!.isEnabled = false
                        stopDataUpdateTimer()
                    }
                    BluetoothState.STATE_CONNECTING -> runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            "onStateChanged: STATE_CONNECTING",
                            Toast.LENGTH_SHORT
                        ).show()
                         mesureData.mTVLogTextView!!.append("\nonStateChanged: STATE_CONNECTING")
                    }
                    BluetoothState.STATE_CONNECTED -> runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            "onStateChanged: STATE_CONNECTED",
                            Toast.LENGTH_SHORT
                        ).show()
                         mesureData.mTVLogTextView!!.append("\nonStateChanged: STATE_CONNECTED")
                        mesureData.mBtnDisconnect!!.isEnabled = true
                    }
                    BluetoothState.STATE_CONNECT_FAIL -> runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            "onStateChanged: STATE_CONNECT_FAIL",
                            Toast.LENGTH_SHORT
                        ).show()
                         mesureData.mTVLogTextView!!.append("\nonStateChanged: STATE_CONNECT_FAIL")
                    }
                    BluetoothState.STATE_CONNECTION_LOST -> runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            "onStateChanged: STATE_CONNECTION_LOST",
                            Toast.LENGTH_SHORT
                        ).show()
                         mesureData.mTVLogTextView!!.append("\nonStateChanged: STATE_CONNECTION_LOST")
                    }
                }
            }

            override fun onReconnect(reconnectCount: Int) {
                runOnUiThread {
                    Toast.makeText(
                        applicationContext,
                        String.format("onReconnect: %d", reconnectCount),
                        Toast.LENGTH_SHORT
                    ).show()
                     mesureData.mTVLogTextView!!.append(String.format("\nonReconnect: %d", reconnectCount))
                }
            }

            override fun onReconnectStop() {
                runOnUiThread {
                    Toast.makeText(applicationContext, "onReconnectStop: ", Toast.LENGTH_SHORT)
                        .show()
                     mesureData.mTVLogTextView!!.append("\nonReconnectStop: ")
                    mesureData.mBtnDisconnect!!.isEnabled = false
                    stopDataUpdateTimer()
                }
            }

            override fun onUpdateRSSI(rssi: Int) {}
        }
    }

    /**
     * 블루투스 검색 이벤트 핸들러 초기화하는 함수
     */
    private fun initBTScanEventHandler() {
        mBTScanEventHandler = object : BTScanEvent {
            override fun onScanDevice(bluetoothDevice: BluetoothDevice) {
                if (bluetoothDevice == null) {
                    return
                }
                val name = bluetoothDevice.name ?: return
                if (name.contains("SHC") || name.contains("i8")) {
                    mDeviceListAdapter!!.addItem(bluetoothDevice)
                }
            }

            override fun onScanDeviceList(bluetoothDeviceList: List<BluetoothDevice>) {
                for (bluetoothDevice in bluetoothDeviceList) {
                    if (bluetoothDevice == null) {
                        continue
                    }
                    val name = bluetoothDevice.name ?: continue
                    if (name.contains("SHC")) {
                        mDeviceListAdapter!!.addItem(bluetoothDevice)
                    }
                }
            }

            override fun onScanFinished() {
                runOnUiThread {
                    mesureData.mBtnScan!!.isEnabled = true
                    mesureData.mBtnScan!!.setText(R.string.button_scan)
                    Toast.makeText(applicationContext, R.string.scan_complete, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mesureData.mBLEManager != null) {
            mesureData.mBLEManager!!.stopScanDevice()
            mesureData.mBLEManager!!.stop()
            mesureData.mBLEManager!!.disconnect()
            mesureData.mBLEManager!!.stopBLEService()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.size == 1) {
                var isPermissionGranted = false
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isPermissionGranted = true
                }
                if (isPermissionGranted == false) {
                    requestPermission()
                }
            } else {
                requestPermission()
            }
        }
    }

    private fun onClickScan() {
        mDeviceListAdapter!!.reset()
        mesureData.mBtnScan!!.setText(R.string.button_scanning)
        mesureData.mBtnScan!!.isEnabled = false
        mesureData.mBLEManager!!.startScanDevice(20 * 1000)
    }

    private fun onClickStart() {
        resetComponent()
        if (mesureData.mRGSamplingRate!!.checkedRadioButtonId == R.id.rb_mainSamplingRate250) {
            mesureData.mSGEMGGraph!!.setSamplingRate(250f)
            mesureData.mSGAccGraph!!.setSamplingRate(31.25f)
            mesureData.mSGGyroGraph!!.setSamplingRate(31.25f)
            mesureData.mSGMagnetoGraph!!.setSamplingRate(31.25f)
            mesureData.mTVLogTextView!!.append("\nonClickStart: Send start command 250 SPS")
            mesureData.mBLEManager!!.start(
                UxProtocol.DAQ_ECG_ACC_GYRO_MAGNETO_SET,
                UxProtocol.SAMPLINGRATE_250
            )
        } else if (mesureData.mRGSamplingRate!!.checkedRadioButtonId == R.id.rb_mainSamplingRate500) {
            mesureData.mSGEMGGraph!!.setSamplingRate(500f)
            mesureData.mSGAccGraph!!.setSamplingRate(62.5f)
            mesureData.mSGGyroGraph!!.setSamplingRate(62.5f)
            mesureData.mSGMagnetoGraph!!.setSamplingRate(62.5f)
             mesureData.mTVLogTextView!!.append("\nonClickStart: Send start command 500 SPS")
            mesureData.mBLEManager!!.start(
                UxProtocol.DAQ_ECG_ACC_GYRO_MAGNETO_SET,
                UxProtocol.SAMPLINGRATE_500
            )
        }
    }

    /**
     * 구성요소 초기화하는 함수
     */
    private fun resetComponent() {
        mesureData.mEMGBuffer!!.clear()
        mesureData.mAccBuffer!!.clear()
        mesureData.mGyroBuffer!!.clear()
        mesureData.mMagnetoBuffer!!.clear()
        mesureData.mStartTime = 0
        mesureData.mEMGCount = 0
        mesureData.mAccCount = 0
        mesureData.mGyroCount = 0
        mesureData.mMagnetoCount = 0
    }

    private fun onClickStop() {
         mesureData.mTVLogTextView!!.append("\nonClickStop: Send stop command")
        mesureData. mBLEManager!!.stop()
    }

    private fun onClickDisconnect() {
        if (mesureData.mBLEManager!!.bluetoothState == BluetoothState.STATE_CONNECTED) {
            mesureData.mBLEManager!!.stop()
            mesureData.mBLEManager!!.disconnect()
        } else {
            mesureData.mBLEManager!!.stop()
            mesureData.mBLEManager!!.disconnect()
            mesureData.mBtnDisconnect!!.isEnabled = false
            stopDataUpdateTimer()
        }
    }

    override fun onClick(v: View?) {

        when(v?.id){
            R.id.btn_mainScan -> onClickBleBtn(v)
            R.id.btn_mainStart -> onClickBleBtn(v)
            R.id.btn_mainStop -> onClickBleBtn(v)
            R.id.btn_mainDisconnect -> onClickBleBtn(v)
        }
    }

     fun onClickBleBtn (v:View?){
        when(v?.id){
            R.id.btn_mainScan -> onClickScan()
            R.id.btn_mainStart -> onClickStart()
            R.id.btn_mainStop -> onClickStop()
            R.id.btn_mainDisconnect -> onClickDisconnect()
        }

    }
}