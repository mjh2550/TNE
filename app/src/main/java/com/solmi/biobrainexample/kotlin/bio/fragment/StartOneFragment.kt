package com.solmi.biobrainexample.kotlin.bio.fragment

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.solmi.biobrainexample.R
import com.solmi.biobrainexample.Simple1ChannelGraph
import com.solmi.biobrainexample.Simple3ChannelGraph
import com.solmi.biobrainexample.kotlin.bio.StartActivity
import com.solmi.biobrainexample.kotlin.data.MesureData
import com.solmi.biobrainexample.process.BioStartActivity
import com.solmi.uxprotocol.UxParserEvent
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [StartOneFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StartOneFragment : Fragment(),View.OnClickListener{

    lateinit var navController : NavController
    lateinit var mesureData : MesureData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_one, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        mesureData = StartActivity.mesureData

        mesureData.mTVLogTextView = view.findViewById(R.id.tv_fragLogTextView)
        mesureData.mTVLogTextView!!.setMovementMethod(
            ScrollingMovementMethod()
        )
        //initBinding(view)
    }

    private fun initBinding(view: View) {
        /*mesureData = MesureData (
            mTVLogTextView = view.findViewById(R.id.tv_fragLogTextView),
            mBtnScan = view.findViewById(R.id.btnScanFrag),
            mBtnStart = view.findViewById(R.id.btnStartFrag),
            mBtnStop = view.findViewById(R.id.btnStopFrag),
            mBtnDisconnect = view.findViewById(R.id.btnDisconnectFrag),
            mLVDeviceList = view.findViewById(R.id.lv_fragDeviceList),
            mSGEMGGraph = view.findViewById(R.id.sg_mainEMGGraph),
            mSGAccGraph = view.findViewById(R.id.sg_mainAccGraph),
            mSGGyroGraph = view.findViewById(R.id.sg_mainGyroGraph),
            mSGMagnetoGraph = view.findViewById(R.id.sg_mainMagnetoGraph),
            mRGSamplingRate = view.findViewById(R.id.rg_mainSamplingRate),
            mBTScanEventHandler =  null,
            mBTStateEventHandler =  null,
            mUxParserEventHandler =  null,
            mItemClickListener =  null,
            mBLEManager =  null,
            mEMGBuffer =  null,
            mAccBuffer =  null,
            mGyroBuffer =  null,
            mMagnetoBuffer =  null,
            mDataUpdateTimer =  null,
            mStartTime =  0,
            mEMGCount =  0,
            mAccCount =  0,
            mGyroCount =  0,
            mMagnetoCount =  0
        )*/

      /*  mesureData.mBtnStop?.setOnClickListener(this)
        mesureData.mBtnScan?.setOnClickListener(this)
        mesureData.mBtnStart?.setOnClickListener(this)
        mesureData.mBtnDisconnect?.setOnClickListener(this)*/



    }

    override fun onClick(v: View?) {
        Log.d("fragOnclick",v?.id.toString())
        (activity as StartActivity).onClickBleBtn(v)
    }
}