package com.solmi.biobrainexample.bio.viewmodel

import androidx.lifecycle.*
import com.solmi.biobrainexample.bio.data.Bio
import com.solmi.biobrainexample.bio.data.BioRepository
import com.solmi.biobrainexample.common.BaseApplication
import com.solmi.ble.BLEDefine
import kotlinx.coroutines.launch

class BioViewModel(private val repository: BioRepository) : ViewModel() {

    val allDatas : LiveData<List<Bio>> = repository.allDatas.asLiveData()

    fun insert(bio : Bio) = viewModelScope.launch {
        repository.insert(bio)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }


//    private var bleStateLiveData : MutableLiveData<BLEDefine.BluetoothState>? = null
//        fun getBleStateLiveData() : MutableLiveData<BLEDefine.BluetoothState>?{
//            if(bleStateLiveData == null){
//                bleStateLiveData = MutableLiveData()
//            }
//            return bleStateLiveData
//        }
//
//    fun updateValue(bleState: BLEDefine.BluetoothState){
//        when(bleState){
//
//        }
//    }


}

class BioViewModelFactory(private val repository: BioRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BioViewModel::class.java)){
            return BioViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}