package com.solmi.biobrainexample.kotlin.bio.fragment

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
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

        initBinding(view)
    }

    private fun initBinding(view: View) {

        Log.d("isnull?? >>>>",mesureData.mBtnScan!!.id.toString())
        val fmBtnScan = view.findViewById(R.id.btnScanFrag) as Button
        val fmBtnStart = view.findViewById(R.id.btnStartFrag) as Button
        val fmBtnStop = view.findViewById(R.id.btnStopFrag) as Button
        val fmBtnDisconnect = view.findViewById(R.id.btnDisconnectFrag) as Button
//        val fmTVLogTextView = view.findViewById(R.id.tv_fragLogTextView) as TextView

        fmBtnScan!!.setOnClickListener(this)
        fmBtnStart!!.setOnClickListener(this)
        fmBtnStop!!.setOnClickListener(this)
        fmBtnDisconnect!!.setOnClickListener(this)

        mesureData.mTVLogTextView = view.findViewById(R.id.tv_fragLogTextView)
        mesureData.mTVLogTextView!!.setMovementMethod(
            ScrollingMovementMethod()
        )



    }

    override fun onClick(v: View?) {
        Log.d("fragOnclick",v?.id.toString())
        when(v?.id){
            R.id.btnScanFrag -> (activity as StartActivity).onClickBleBtn(mesureData.mBtnScan)
            R.id.btnStartFrag -> (activity as StartActivity).onClickBleBtn(mesureData.mBtnStart)
            R.id.btnStopFrag -> (activity as StartActivity).onClickBleBtn(mesureData.mBtnStop)
            R.id.btnDisconnectFrag -> (activity as StartActivity).onClickBleBtn(mesureData.mBtnDisconnect)
        }

    }
}