package com.solmi.biobrainexample.fcm

import android.content.Context
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
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

        binding.fcmBtn02.setOnClickListener {
            //TODO 진동

         /*   val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator // 진동을 울리기 위한
            if (Build.VERSION.SDK_INT >= 26) {  // 안드로이드 버전에 따른 조건
                vibrator.vibrate(VibrationEffect.createOneShot(1000, 100)) // 지속시간, 크기
            } else {
                vibrator.vibrate(1000) // 지속시간
            }*/

            val vib = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                getSystemService(VIBRATOR_SERVICE) as Vibrator
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                vib.vibrate(VibrationEffect.createOneShot(1000,100))
            }else {
                vib.vibrate(1000)
            }

        }


        binding.fcmBtn03.setOnClickListener {

        }

        binding.fcmBtn04.setOnClickListener {

        }

        binding.fcmBtn05.setOnClickListener {

        }



    }
}