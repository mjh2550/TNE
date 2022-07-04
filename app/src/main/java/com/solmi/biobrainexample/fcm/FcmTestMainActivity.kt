package com.solmi.biobrainexample.fcm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.solmi.biobrainexample.R
import com.solmi.biobrainexample.databinding.ActivityFcmTestMainBinding

class FcmTestMainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFcmTestMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFcmTestMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firebaseMessaging = FirebaseMessaging.getInstance()
        var fcmToken = ""

        binding.fcmBtn01.setOnClickListener {
            //TODO FCM 서버 -> 앱 TOKEN 받기
            firebaseMessaging.token.addOnCompleteListener(OnCompleteListener {
                task ->
                if(!task.isSuccessful){
                    Log.d("fcmT","Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                val token = task.result
                Toast.makeText(baseContext,token.toString(),Toast.LENGTH_SHORT).show()
                Log.d("fcmToken","$token")
                fcmToken = "$token"
                binding.fcmTv01.text = "$token"
            })
        }



    }
}