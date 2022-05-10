package com.solmi.biobrainexample.kotlin.bio.fragment

import android.os.Bundle
import android.text.Layout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.TextView
import androidx.annotation.ContentView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.solmi.biobrainexample.R
import com.solmi.biobrainexample.kotlin.bio.StartActivity
import org.w3c.dom.Text
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [StartOneFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StartOneFragment : Fragment(),View.OnClickListener{

    lateinit var navController : NavController
    lateinit var mainBLEView : View
    lateinit var btn_test: Button
    lateinit var tv_ble_info01 : TextView
    lateinit var tv_ble_info02 : TextView
    lateinit var fadeIn : AlphaAnimation
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
        
        //애니메이션
        fadeIn = AlphaAnimation(0.0f, 1.0f)
        fadeOut = AlphaAnimation(1.0f, 0.0f)
        fadeIn.duration = 1200
        fadeIn.fillAfter = true
        fadeOut.duration = 1200
        fadeOut.fillAfter = true
        fadeOut.startOffset = 4200 + fadeIn.startOffset


        //컴포넌트
        btn_test = view.findViewById(R.id.btn_test01)
        tv_ble_info01 = view.findViewById(R.id.tv_BLE_info_01)
        tv_ble_info02 = view.findViewById(R.id.tv_BLE_info_02)
        btn_test.setOnClickListener(this)
        tv_ble_info01.startAnimation(fadeIn)
        tv_ble_info02.startAnimation(fadeIn)
        tv_ble_info01.startAnimation(fadeOut)
        tv_ble_info02.startAnimation(fadeOut)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_test01 -> {
                if(mainBLEView.visibility == View.VISIBLE)
                 mainBLEView.visibility = View.INVISIBLE
                else
                mainBLEView.visibility = View.VISIBLE
            }
        }

    }
}