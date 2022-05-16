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


/**
 * A simple [Fragment] subclass.
 * Use the [StartTwoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StartTwoFragment : Fragment(),View.OnClickListener {

    private lateinit var navController : NavController
    private lateinit var viewMainBLE : View
    private lateinit var viewBottom : View
    private lateinit var viewBleState : View
    private lateinit var viewBottomBtnBar : View
    private lateinit var viewMainGraphView : View

//    private lateinit var tv_frag_01 : TextView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_two, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        initBinding(view)
        setAnimation()

    }

    override fun onClick(v: View?) {

    }


    /**
     * 컴포넌트 바인딩
     */
    private fun initBinding(view: View) {
        //메인 뷰
        viewMainBLE = StartActivity.viewMainBLE
        //하단 뷰
        viewBottom = StartActivity.viewBottom
        //블루투스 기기 정보
        viewBleState = StartActivity.viewBLEState
        //하단 버튼
        viewBottomBtnBar = StartActivity.viewBottomBtnBar
        //블루투스 측정 뷰
        viewMainGraphView = StartActivity.viewMainGraphView

//        tv_frag_01 = view.findViewById(R.id.tv_frag_01)

    }

    private fun setAnimation(){

        val fadeOutAndFadeIn = AlphaAnimation(1.0f, 0.0f).apply {
            duration = 600
            fillAfter = true
            startOffset = 0

            setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    val fadeIn = AlphaAnimation(0.0f,1.0f).apply {
                        duration = 600
                        fillAfter = true
                        startOffset = 0
                    }
//                    tv_frag_01.startAnimation(fadeIn)
//                    tv_frag_01.visibility = View.VISIBLE

                    viewMainGraphView.startAnimation(fadeIn)
                    viewMainGraphView.visibility = View.VISIBLE


                }

                override fun onAnimationRepeat(animation: Animation?) {
                }

            })
        }
        viewMainBLE.startAnimation(fadeOutAndFadeIn)
        viewMainBLE.visibility = View.INVISIBLE

    }


}