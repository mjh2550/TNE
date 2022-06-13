package com.solmi.biobrainexample.bio.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
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
    private lateinit var viewMainBLE : View
    private lateinit var viewBottom : View
    private lateinit var viewBleState : View
    private lateinit var viewBottomBtnBar : View
    private lateinit var btn_test: Button
    private lateinit var tv_ble_info01 : TextView
    private lateinit var tv_ble_info02 : TextView
    private lateinit var infoMsgFadeIn01 : AlphaAnimation
    private lateinit var infoMsgFadeIn02 : AlphaAnimation
    private lateinit var fadeOut : AlphaAnimation



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
        setAnimation()

    }

    /**
     * 컴포넌트 바인딩
     */
    private fun initBinding(view: View) {
        //메인 뷰
        viewMainBLE = StartActivity.viewMainBLE
        viewBottom = StartActivity.viewBottom
        viewBleState = StartActivity.viewBLEState
        viewBottomBtnBar = StartActivity.viewBottomBtnBar


        //컴포넌트
        tv_ble_info01 = view.findViewById(R.id.tv_BLE_info_01)
        tv_ble_info02 = view.findViewById(R.id.tv_BLE_info_02)
        btn_test = view.findViewById(R.id.btn_test01)
        btn_test.setOnClickListener(this)
    }

    /**
     * 에니메이션 설정
     */
    private fun setAnimation() {
        val fadeDurSet: Long = 600

        //애니메이션
        infoMsgFadeIn01 = AlphaAnimation(0.0f, 1.0f).apply {
            duration = fadeDurSet
            fillAfter = true

        }
        tv_ble_info01.startAnimation(infoMsgFadeIn01)

        infoMsgFadeIn02 = AlphaAnimation(0.0f, 1.0f).apply {
            duration = fadeDurSet
            fillAfter = true
            startOffset = 1500

            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    val infoMsgFadeOut = AlphaAnimation(1.0f, 0.0f).apply {
                        duration = fadeDurSet
                        fillAfter = true
                        startOffset = infoMsgFadeIn02.startOffset
                    }
                    tv_ble_info01.startAnimation(infoMsgFadeOut)
                    tv_ble_info02.startAnimation(infoMsgFadeOut)
                    tv_ble_info01.visibility = View.INVISIBLE
                    tv_ble_info02.visibility = View.INVISIBLE

                    val botFadeIn = AlphaAnimation(0.0f,1.0f).apply {
                        duration = fadeDurSet
                        fillAfter = true
                        startOffset = infoMsgFadeIn02.startOffset
                    }
                    viewBleState.startAnimation(botFadeIn)
                    viewBleState.visibility = View.VISIBLE
                    viewBottomBtnBar.startAnimation(botFadeIn)
                    viewBottomBtnBar.visibility = View.VISIBLE
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }

            })
        }
        tv_ble_info02.startAnimation(infoMsgFadeIn02)

        fadeOut = AlphaAnimation(1.0f, 0.0f).apply {
            duration = fadeDurSet
            fillAfter = true
            startOffset = 1000 + infoMsgFadeIn02.startOffset
        }

        val mainViewFadeIn = AlphaAnimation(0.0f, 1.0f).apply {
            duration = fadeDurSet
            fillAfter = true
            startOffset = 1000 + fadeOut.startOffset
        }
        viewMainBLE.startAnimation(mainViewFadeIn)
        viewMainBLE.visibility = View.VISIBLE


        /* ObjectAnimator.ofFloat(viewMainBLE,"translationY",400f).apply {
            duration = 2000
            start()
        }*/
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_test01 -> {
                if(viewMainBLE.visibility ==View.INVISIBLE){
                    viewMainBLE.startAnimation(infoMsgFadeIn01)
                    viewMainBLE.visibility = View.VISIBLE
                    return
                }
                fadeOut.startOffset = 0
                viewMainBLE.startAnimation(fadeOut)
                viewMainBLE.visibility =View.INVISIBLE
            }
        }
    }
}