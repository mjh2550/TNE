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

}