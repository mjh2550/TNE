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


class StartActivity : AppCompatActivity() {

    lateinit var navController: NavController
    lateinit var navHostFragment: NavHostFragment

    private val PERMISSION_REQUEST_CODE :Int = 100

    var mTVLogTextView: TextView? = null
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
    private var mItemClickListener: OnItemClickListener? = null

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.bio_start_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

    /*    initHandler()
        initComponent()

        val isPermissionGranted : Boolean = checkPermission()
        if(isPermissionGranted == false){
            requestPermission()
        }
*/
    }


    /*private fun requestPermission() {

        val needPermissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        ActivityCompat.requestPermissions(this, needPermissions, PERMISSION_REQUEST_CODE)

    }

    *//**
     * 권한 설정되었는지 확인하는 함수
     * @return 권한 설정 여부
     *//*
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

    *//**
     * 구성 요소들 초기화하는 함수
     *//*
    private fun initComponent() {

          mTVLogTextView    = findViewById(R.id.tv_mainLogTextView)
          mBtnScan          = findViewById(R.id.btn_mainScan)
          mBtnStart         = findViewById(R.id.btn_mainStart)
          mBtnStop          = findViewById(R.id.btn_Stop)
          mBtnDisconnect    = findViewById(R.id.btn_mainDisconnect)
          mLVDeviceList     = findViewById(R.id.lv_mainDeviceList)
          mSGEMGGraph       = findViewById(R.id.sg_mainEMGGraph)
          mSGAccGraph       = findViewById(R.id.sg_mainAccGraph)
          mSGGyroGraph      = findViewById(R.id.sg_mainGyroGraph)
          mSGMagnetoGraph   = findViewById(R.id.sg_mainMagnetoGraph)
          mRGSamplingRate   = findViewById(R.id.rg_mainSamplingRate)
          mBtnScan!!.setOnClickListener(this)
          mBtnStart!!.setOnClickListener(this)
          mBtnDisconnect!!.setOnClickListener(this)
          mBtnStop!!.setOnClickListener(this)

        mBLEManager = BLECommManager(this)
        mBLEManager!!.registerBTScanEventHandler(mBTScanEventHandler)
        mBLEManager!!.registerBTStateEventHandler(mBTStateEventHandler)
        mBLEManager!!.registerParserEventHandler(mUxParserEventHandler)
        val isBLESupport: Boolean = mBLEManager!!.checkIsBLESupport()
        if (isBLESupport) {
            if (mBLEManager!!.startBLEService() == false) {
                Toast.makeText(
                    applicationContext,
                    R.string.error_ble_start_service,
                    Toast.LENGTH_SHORT
                ).show()
                mBLEManager = null
                return
            }
        } else {
            Toast.makeText(applicationContext, R.string.error_not_support_ble, Toast.LENGTH_SHORT)
                .show()
        }
        mLVDeviceList!!.setOnItemClickListener(mItemClickListener)
        mDeviceListAdapter = DeviceListAdapter(this)
        mLVDeviceList!!.setAdapter(mDeviceListAdapter)
        mTVLogTextView!!.setMovementMethod(ScrollingMovementMethod())
        mEMGBuffer = LinkedBlockingQueue<IntArray>()
        mAccBuffer = LinkedBlockingQueue<IntArray>()
        mGyroBuffer = LinkedBlockingQueue<IntArray>()
        mMagnetoBuffer = LinkedBlockingQueue<IntArray>()
    }

    *//**
     * 이벤트 핸들러들 초기화하는 함수
     *//*
    private fun initHandler() {
        initBTScanEventHandler()
        initBTStateEventHandler()
        initUxParserEventHandler()
        initItemClickListener()
    }

    *//**
     * 디바이스 리스트 뷰 아이템 클릭 이벤트 핸들러 초기화하는 함수
     *//*
    private fun initItemClickListener() {
        mItemClickListener =
            OnItemClickListener { adapterView, view, index, l ->
                if (mBLEManager!!.bluetoothState == BluetoothState.STATE_CONNECTED) {
                    Toast.makeText(
                        applicationContext,
                        R.string.error_device_connected,
                        Toast.LENGTH_LONG
                    ).show()
                    return@OnItemClickListener
                }
                mBLEManager!!.stopScanDevice()
                val device = mDeviceListAdapter!!.getItem(index)
                mTVLogTextView!!.text = "device name: " + device.name
                mBLEManager!!.setDeviceType(BTDataDefine.DeviceType.SHC_U4)
                mBLEManager!!.setReconnectCount(3)
                mBLEManager!!.connect(device)
            }
    }

    *//**
     * 데이터 파싱 이벤트 핸들러 초기화하는 함수
     *//*
    private fun initUxParserEventHandler() {
        mUxParserEventHandler = object : UxParserEvent {
            override fun onParserHeaderPacket(headerPacket: HeaderPacket) {
                when (headerPacket.packetType) {
                    UxProtocol.RES_DAQ_STOP -> runOnUiThread {
                        mTVLogTextView!!.append("\nRES_DAQ_STOP")
                        stopDataUpdateTimer()
                    }
                    UxProtocol.RES_DAQ -> runOnUiThread {
                        mTVLogTextView!!.append("\nRES_DAQ")
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
                        mTVLogTextView!!.append(log)
                    }
                    UxProtocol.RES_BATT_INFO -> runOnUiThread {
                        val batteryInfo = headerPacket.batteryInfo
                        val log = String.format(
                            "\nRES_BATT_INFO: Max: %.1f Cur: %.1f",
                            batteryInfo.maximumVoltage,
                            batteryInfo.currentVoltage
                        )
                        mTVLogTextView!!.append(log)
                    }
                    UxProtocol.RES_FIRM_INFO -> runOnUiThread {
                        val deviceInfo = headerPacket.deviceInfo
                        val log = String.format(
                            "\nRES_FIRM_INFO: Major: %d Minor: %d Build: %d",
                            deviceInfo.majorVersion,
                            deviceInfo.minorVersion,
                            deviceInfo.buildVersion
                        )
                        mTVLogTextView!!.append(log)
                    }
                }
            }

            override fun onParserSpecialPacket(type: Byte, value: Byte) {
                runOnUiThread {
                    val log = String.format("SpecialPacket %02X %02X", type, value)
                    mTVLogTextView!!.append(log)
                }
            }

            override fun onParserECG(channels: IntArray) {
                mEMGCount++
                mEMGBuffer!!.offer(channels.clone())
            }

            override fun onParserACC(channels: IntArray) {
                mAccCount++
                mAccBuffer!!.offer(channels.clone())
            }

            override fun onParserGYRO(channels: IntArray) {
                mGyroCount++
                mGyroBuffer!!.offer(channels.clone())
            }

            override fun onParserMAGNETO(channels: IntArray) {
                mMagnetoCount++
                mMagnetoBuffer!!.offer(channels.clone())
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

    *//**
     * 데이터 업데이트 타이머 시작하는 함수
     *//*
    private fun startDataUpdateTimer() {
        if (mDataUpdateTimer == null) {
            mStartTime = System.currentTimeMillis()
            mDataUpdateTimer = Timer("Data update Timer")
            mDataUpdateTimer!!.schedule(getDataUpdateTimerTask(), 0, 25)
        }
    }

    *//**
     * 데이터 업데이트 타이머 태스크 반환하는 함수
     * @return 타이머 태스크
     *//*
    private fun getDataUpdateTimerTask(): TimerTask? {
        return object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    val emgSize = mEMGBuffer!!.size
                    for (count in 0 until emgSize) {
                        val channels = mEMGBuffer!!.poll()
                        if (channels != null) {
                            val value = channels[0] / 2047f * 7.4f
                            mSGEMGGraph!!.putValue(value)
                        }
                    }
                    val accSize = mAccBuffer!!.size
                    for (count in 0 until accSize) {
                        val channels = mAccBuffer!!.poll()
                        if (channels != null) {
                            val valueArray = FloatArray(3)
                            for (index in 0..2) {
                                valueArray[index] = channels[index] / 1023f * 3f
                            }
                            mSGAccGraph!!.putValueArray(valueArray)
                        }
                    }
                    val gyroSize = mGyroBuffer!!.size
                    for (count in 0 until gyroSize) {
                        val channels = mGyroBuffer!!.poll()
                        if (channels != null) {
                            val valueArray = FloatArray(3)
                            for (index in 0..2) {
                                valueArray[index] = channels[index] / 1023f * 3f
                            }
                            mSGGyroGraph!!.putValueArray(valueArray)
                        }
                    }
                    val magnetoSize = mMagnetoBuffer!!.size
                    for (count in 0 until magnetoSize) {
                        val channels = mMagnetoBuffer!!.poll()
                        if (channels != null) {
                            val valueArray = FloatArray(3)
                            for (index in 0..2) {
                                valueArray[index] = channels[index] / 1023f * 3f
                            }
                            mSGMagnetoGraph!!.putValueArray(valueArray)
                        }
                    }
                }
            }
        }
    }

    *//**
     * 데이터 업데이트 타이머 종료하는 함수
     *//*
    private fun stopDataUpdateTimer() {
        if (mDataUpdateTimer == null) {
            return
        }
        mTVLogTextView!!.append(
            String.format(
                "\nRun time: %.3f(s)",
                (System.currentTimeMillis() - mStartTime) / 1000f
            )
        )
        mTVLogTextView!!.append(
            String.format(
                "\nEMG: %d Acc: %d Gyro: %d Magneto: %d",
                mEMGCount,
                mAccCount,
                mGyroCount,
                mMagnetoCount
            )
        )
        mDataUpdateTimer!!.cancel()
        mDataUpdateTimer = null
    }

    *//**
     * 블루투스 상태 변화 이벤트 핸들러 초기화하는 함수
     *//*
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
                        mTVLogTextView!!.append("\nonStateChanged: STATE_DISCONNECTED")
                        mBtnDisconnect!!.isEnabled = false
                        stopDataUpdateTimer()
                    }
                    BluetoothState.STATE_CONNECTING -> runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            "onStateChanged: STATE_CONNECTING",
                            Toast.LENGTH_SHORT
                        ).show()
                        mTVLogTextView!!.append("\nonStateChanged: STATE_CONNECTING")
                    }
                    BluetoothState.STATE_CONNECTED -> runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            "onStateChanged: STATE_CONNECTED",
                            Toast.LENGTH_SHORT
                        ).show()
                        mTVLogTextView!!.append("\nonStateChanged: STATE_CONNECTED")
                        mBtnDisconnect!!.isEnabled = true
                    }
                    BluetoothState.STATE_CONNECT_FAIL -> runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            "onStateChanged: STATE_CONNECT_FAIL",
                            Toast.LENGTH_SHORT
                        ).show()
                        mTVLogTextView!!.append("\nonStateChanged: STATE_CONNECT_FAIL")
                    }
                    BluetoothState.STATE_CONNECTION_LOST -> runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            "onStateChanged: STATE_CONNECTION_LOST",
                            Toast.LENGTH_SHORT
                        ).show()
                        mTVLogTextView!!.append("\nonStateChanged: STATE_CONNECTION_LOST")
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
                    mTVLogTextView!!.append(String.format("\nonReconnect: %d", reconnectCount))
                }
            }

            override fun onReconnectStop() {
                runOnUiThread {
                    Toast.makeText(applicationContext, "onReconnectStop: ", Toast.LENGTH_SHORT)
                        .show()
                    mTVLogTextView!!.append("\nonReconnectStop: ")
                    mBtnDisconnect!!.isEnabled = false
                    stopDataUpdateTimer()
                }
            }

            override fun onUpdateRSSI(rssi: Int) {}
        }
    }

    *//**
     * 블루투스 검색 이벤트 핸들러 초기화하는 함수
     *//*
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
                    mBtnScan!!.isEnabled = true
                    mBtnScan!!.setText(R.string.button_scan)
                    Toast.makeText(applicationContext, R.string.scan_complete, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mBLEManager != null) {
            mBLEManager!!.stopScanDevice()
            mBLEManager!!.stop()
            mBLEManager!!.disconnect()
            mBLEManager!!.stopBLEService()
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
        mBtnScan!!.setText(R.string.button_scanning)
        mBtnScan!!.isEnabled = false
        mBLEManager!!.startScanDevice(20 * 1000)
    }

    private fun onClickStart() {
        resetComponent()
        if (mRGSamplingRate!!.checkedRadioButtonId == R.id.rb_mainSamplingRate250) {
            mSGEMGGraph!!.setSamplingRate(250f)
            mSGAccGraph!!.setSamplingRate(31.25f)
            mSGGyroGraph!!.setSamplingRate(31.25f)
            mSGMagnetoGraph!!.setSamplingRate(31.25f)
            mTVLogTextView!!.append("\nonClickStart: Send start command 250 SPS")
            mBLEManager!!.start(
                UxProtocol.DAQ_ECG_ACC_GYRO_MAGNETO_SET,
                UxProtocol.SAMPLINGRATE_250
            )
        } else if (mRGSamplingRate!!.checkedRadioButtonId == R.id.rb_mainSamplingRate500) {
            mSGEMGGraph!!.setSamplingRate(500f)
            mSGAccGraph!!.setSamplingRate(62.5f)
            mSGGyroGraph!!.setSamplingRate(62.5f)
            mSGMagnetoGraph!!.setSamplingRate(62.5f)
            mTVLogTextView!!.append("\nonClickStart: Send start command 500 SPS")
            mBLEManager!!.start(
                UxProtocol.DAQ_ECG_ACC_GYRO_MAGNETO_SET,
                UxProtocol.SAMPLINGRATE_500
            )
        }
    }

    *//**
     * 구성요소 초기화하는 함수
     *//*
    private fun resetComponent() {
        mEMGBuffer!!.clear()
        mAccBuffer!!.clear()
        mGyroBuffer!!.clear()
        mMagnetoBuffer!!.clear()
        mStartTime = 0
        mEMGCount = 0
        mAccCount = 0
        mGyroCount = 0
        mMagnetoCount = 0
    }

    private fun onClickStop() {
        mTVLogTextView!!.append("\nonClickStop: Send stop command")
        mBLEManager!!.stop()
    }

    private fun onClickDisconnect() {
        if (mBLEManager!!.bluetoothState == BluetoothState.STATE_CONNECTED) {
            mBLEManager!!.stop()
            mBLEManager!!.disconnect()
        } else {
            mBLEManager!!.stop()
            mBLEManager!!.disconnect()
            mBtnDisconnect!!.isEnabled = false
            stopDataUpdateTimer()
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_mainScan -> onClickScan()
            R.id.btn_mainStart -> onClickStart()
            R.id.btn_mainStop -> onClickStop()
            R.id.btn_mainDisconnect -> onClickDisconnect()
        }
    }*/
}