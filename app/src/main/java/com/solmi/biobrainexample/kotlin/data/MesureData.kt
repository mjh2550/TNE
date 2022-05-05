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
    var mTVLogTextView: TextView?,
    var mBtnScan: Button?,
    var mBtnStart: Button?,
    var mBtnStop: Button?,
    var mBtnDisconnect: Button?,
    var mLVDeviceList: ListView?,
    var mSGEMGGraph: Simple1ChannelGraph?,
    var mSGAccGraph: Simple3ChannelGraph?,
    var mSGGyroGraph: Simple3ChannelGraph?,
    var mSGMagnetoGraph: Simple3ChannelGraph?,
    var mRGSamplingRate: RadioGroup?,
    var mBTScanEventHandler: BTScanEvent?,
    var mBTStateEventHandler: BTStateEvent?,
    var mUxParserEventHandler: UxParserEvent?,
    var mItemClickListener: AdapterView.OnItemClickListener?,
    var mBLEManager: BLECommManager?,
    var mEMGBuffer: Queue<IntArray>?,
    var mAccBuffer: Queue<IntArray>?,
    var mGyroBuffer: Queue<IntArray>?,
    var mMagnetoBuffer: Queue<IntArray>?,
    var mDataUpdateTimer: Timer?,
    var mStartTime: Long =0,
    var mEMGCount: Int =0,
    var mAccCount: Int =0,
    var mGyroCount: Int =0,
    var mMagnetoCount: Int =0
                      ){


    fun setClearData(){
        mTVLogTextView         = null
        mBtnScan               = null
        mBtnStart              = null
        mBtnStop               = null
        mBtnDisconnect         = null
        mLVDeviceList          = null
        mSGEMGGraph            = null
        mSGAccGraph            = null
        mSGGyroGraph           = null
        mSGMagnetoGraph        = null
        mRGSamplingRate        = null
        mBTScanEventHandler    = null
        mBTStateEventHandler   = null
        mUxParserEventHandler  = null
        mItemClickListener     = null
        mBLEManager            = null
        mEMGBuffer             = null
        mAccBuffer             = null
        mGyroBuffer            = null
        mMagnetoBuffer         = null
        mDataUpdateTimer       = null
        mStartTime             =0
        mEMGCount              =0
        mAccCount              =0
        mGyroCount             =0
        mMagnetoCount          =0
    }
}
