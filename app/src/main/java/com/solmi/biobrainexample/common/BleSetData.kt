package com.solmi.biobrainexample.common

import android.widget.AdapterView
import android.widget.RadioGroup
import com.solmi.biobrainexample.DeviceListAdapter
import com.solmi.biobrainexample.Simple1ChannelGraph
import com.solmi.biobrainexample.Simple3ChannelGraph
import com.solmi.ble.BLECommManager
import com.solmi.ble.BTScanEvent
import com.solmi.ble.BTStateEvent
import com.solmi.uxprotocol.UxParserEvent
import java.util.*

data class BleSetData(var mBTScanEventHandler: BTScanEvent? = null,
                      var mBTStateEventHandler: BTStateEvent? = null,
                      var mUxParserEventHandler: UxParserEvent? = null,
                      var mItemClickListener: AdapterView.OnItemClickListener? = null,
                      var mBLEManager: BLECommManager? = null,
                      var mDeviceListAdapter: DeviceListAdapter? = null,
                      var mEMGBuffer: Queue<IntArray>? = null,
                      var mAccBuffer: Queue<IntArray>? = null,
                      var mGyroBuffer: Queue<IntArray>? = null,
                      var mMagnetoBuffer: Queue<IntArray>? = null,
                      var mDataUpdateTimer: Timer? = null,
                      var mStartTime: Long = 0,
                      var mEMGCount: Int = 0,
                      var mAccCount: Int = 0,
                      var mGyroCount: Int = 0,
                      var mMagnetoCount: Int =0,
                      var mSGEMGGraph: Simple1ChannelGraph? = null,
                      var mSGAccGraph: Simple3ChannelGraph? = null,
                      var mSGGyroGraph: Simple3ChannelGraph? = null,
                      var mSGMagnetoGraph: Simple3ChannelGraph? = null,
                      var mRGSamplingRate: RadioGroup? = null)
