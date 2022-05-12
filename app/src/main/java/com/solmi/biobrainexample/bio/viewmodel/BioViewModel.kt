package com.solmi.biobrainexample.bio.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.solmi.ble.BLEDefine

class BioViewModel : ViewModel() {

    private var bleStateLiveData : MutableLiveData<BLEDefine.BluetoothState>? = null
        fun getBleStateLiveData() : MutableLiveData<BLEDefine.BluetoothState>?{
            if(bleStateLiveData == null){
                bleStateLiveData = MutableLiveData()
            }
            return bleStateLiveData
        }

    fun updateValue(bleState: BLEDefine.BluetoothState){
        when(bleState){

        }
    }


}