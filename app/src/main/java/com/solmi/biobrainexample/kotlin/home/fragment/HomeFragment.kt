package com.solmi.biobrainexample.kotlin.home.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.solmi.biobrainexample.R
import com.solmi.biobrainexample.kotlin.DemoActivity
import com.solmi.biobrainexample.kotlin.bio.StartActivity


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() , View.OnClickListener {

    lateinit var navController: NavController
    lateinit var btnGoMesure : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        initBinding(view)


    }
    private fun initBinding(v : View){

        btnGoMesure = v.findViewById(R.id.btn_go_measure)
        btnGoMesure.setOnClickListener(this)

    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_go_measure -> {
//                navController.navigate(R.id.action_bioStartFragment_to_bioStart1Fragment)
                //fragment는 activity 위에서 돌아가기 때문에 activity를 넣어줘야 함
                val intent = Intent(this.activity, StartActivity::class.java)
                startActivity(intent)

            }
        }

    }
}