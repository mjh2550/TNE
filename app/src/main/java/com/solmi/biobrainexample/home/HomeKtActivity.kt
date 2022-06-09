package com.solmi.biobrainexample.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.solmi.biobrainexample.R
import com.solmi.biobrainexample.common.CircularQueue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class HomeKtActivity : AppCompatActivity() {

    lateinit var navController: NavController
    lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_kt)

        initNavBinding()
//        test()
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
//            test()
            loop()
        }



    }

    /**
     * Navigation Binding
     * 2022.04.23
     */
    private fun initNavBinding() {

        //navigation Binding
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setupWithNavController(navController)
    }
    fun loop(){
        var idx = 0
        while (true){
            Log.d("test",idx++.toString())
        }
    }

    fun test(){
        val circularQueue = CircularQueue()
        var data = 0.11351f
        var insCnt = 0
        for(i in 0..5000){
            circularQueue.insert(data)
            data +=0.10101f
            insCnt++
        }

        val jsonObj = JSONObject()
        val jsonArray = JSONArray()
        var idx =0
           while(true){
                val jsonObject = JSONObject()
                var data = circularQueue.pop()
                if(data == -99999999f || insCnt==idx){
                    break
                }
                jsonObject.put("bioData",data)
                jsonObject.put("Time",System.currentTimeMillis().toString())
                jsonArray.put(jsonObject)
               idx++
            }
        jsonObj.put("item",jsonArray)

        Log.d("data : ",jsonObj.toString())
    }

}