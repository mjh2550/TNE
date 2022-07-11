package com.solmi.biobrainexample.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.solmi.biobrainexample.R


/**
 * A simple [Fragment] subclass.
 * Use the [SettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingFragment : Fragment() ,View.OnClickListener{

    lateinit var navController: NavController
//    lateinit var rg_selectNotType : RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_setting, container, false)
        return inflater.inflate(R.layout.activity_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        /*rg_selectNotType = view.findViewById(R.id.rg_selectNotType)
        rg_selectNotType.setOnClickListener {
            //벨소리 타입 설정 (진동 , 벨소리, 진동+벨, 무음) (추후 추가 + 푸시알림, 커스텀 음악)
            //해당 버튼을 누르고 저장 시 객체에 저장하고, SharedPreference에 객체 저장하기

            when(it.id){
                R.id.rb_vib ->{

                }
                R.id.rb_ring -> {

                }
                R.id.rb_ringAndVib ->{

                }
                R.id.rb_mute ->{

                }
            }
        }*/
    }



    override fun onClick(v: View?) {
    }
}