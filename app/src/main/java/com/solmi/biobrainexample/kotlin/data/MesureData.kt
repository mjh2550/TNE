package com.solmi.biobrainexample.kotlin.data

import android.widget.*
import com.solmi.biobrainexample.Simple1ChannelGraph
import com.solmi.biobrainexample.Simple3ChannelGraph
import com.solmi.ble.BLECommManager
import com.solmi.ble.BTScanEvent
import com.solmi.ble.BTStateEvent
import com.solmi.uxprotocol.UxParserEvent
import java.util.*

data class MesureData(
    val mTVLogTextView: TextView?,
    val mBtnScan: Button?,
    val mBtnStart: Button?,
    val mBtnStop: Button?,
    val mBtnDisconnect: Button?,
    var mLVDeviceList: ListView?,
    val mSGEMGGraph: Simple1ChannelGraph?,
    val mSGAccGraph: Simple3ChannelGraph?,
    val mSGGyroGraph: Simple3ChannelGraph?,
    val mSGMagnetoGraph: Simple3ChannelGraph?,
    val mRGSamplingRate: RadioGroup?,
    val mBTScanEventHandler: BTScanEvent?,
    val mBTStateEventHandler: BTStateEvent?,
    val mUxParserEventHandler: UxParserEvent?,
    val mItemClickListener: AdapterView.OnItemClickListener?,
    var mBLEManager: BLECommManager?,
    val mEMGBuffer: Queue<IntArray>?,
    val mAccBuffer: Queue<IntArray>?,
    val mGyroBuffer: Queue<IntArray>?,
    val mMagnetoBuffer: Queue<IntArray>?,
    var mDataUpdateTimer: Timer?,
    var mStartTime: Long =0,
    var mEMGCount: Int =0,
    var mAccCount: Int =0,
    var mGyroCount: Int =0,
    var mMagnetoCount: Int =0
                      )
