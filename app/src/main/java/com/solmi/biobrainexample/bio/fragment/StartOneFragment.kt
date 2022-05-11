package com.solmi.biobrainexample.bio.fragment

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.solmi.biobrainexample.R
import com.solmi.biobrainexample.bio.StartActivity
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [StartOneFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StartOneFragment : Fragment(),View.OnClickListener{

    private lateinit var navController : NavController
    private lateinit var mainBLEView : View
    private lateinit var btn_test: Button
    private lateinit var tv_ble_info01 : TextView
    private lateinit var tv_ble_info02 : TextView
    private lateinit var infoMsgFadeIn01 : AlphaAnimation
    lateinit var fadeOut : AlphaAnimation
    


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

        initBinding(view)


    }

    private fun initBinding(view: View) {
        //메인 블루투스 뷰
        mainBLEView = StartActivity.mainBLEView

        //컴포넌트
        btn_test = view.findViewById(R.id.btn_test01)
        tv_ble_info01 = view.findViewById(R.id.tv_BLE_info_01)
        tv_ble_info02 = view.findViewById(R.id.tv_BLE_info_02)
        btn_test.setOnClickListener(this)
        
        //애니메이션
        infoMsgFadeIn01 = AlphaAnimation(0.0f, 1.0f)
        infoMsgFadeIn01.duration = 1200
        infoMsgFadeIn01.fillAfter = true
        tv_ble_info01.startAnimation(infoMsgFadeIn01)

        val infoMsgFadeIn02 = AlphaAnimation(0.0f, 1.0f)
        infoMsgFadeIn02.duration = 1200
        infoMsgFadeIn02.fillAfter = true
        infoMsgFadeIn02.startOffset = 1500
        tv_ble_info02.startAnimation(infoMsgFadeIn02)

        fadeOut = AlphaAnimation(1.0f, 0.0f)
        fadeOut.duration = 1200
        fadeOut.fillAfter = true
        fadeOut.startOffset = 1000 + infoMsgFadeIn02.startOffset

        val mainViewFadeIn = AlphaAnimation(0.0f, 1.0f)
        mainViewFadeIn.duration = 1200
        mainViewFadeIn.fillAfter = true
        mainViewFadeIn.startOffset = 1000 + fadeOut.startOffset
        mainBLEView.startAnimation(mainViewFadeIn)
        mainBLEView.visibility = View.VISIBLE

        val infoMsgFadeOut01 = AlphaAnimation(1.0f,0.0f)
        infoMsgFadeOut01.duration = 1200
        infoMsgFadeOut01.fillAfter = true


        ObjectAnimator.ofFloat(mainBLEView,"translationY",400f).apply {
            duration = 2000
            start()
        }



    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_test01 -> {

                val tf = if (mainBLEView.visibility ==View.VISIBLE )"true" else "false"
                Log.d("TEST>>>>",tf)
                if(mainBLEView.visibility ==View.INVISIBLE){
                   mainBLEView.startAnimation(infoMsgFadeIn01)
                   mainBLEView.visibility = View.VISIBLE
                   return
                }
                   fadeOut.startOffset = 0
                   mainBLEView.startAnimation(fadeOut)
                   mainBLEView.visibility =View.INVISIBLE
            }
        }

    }
}