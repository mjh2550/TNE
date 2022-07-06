package com.solmi.biobrainexample.fcm

import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.solmi.biobrainexample.R
import com.solmi.biobrainexample.databinding.ActivityFcmTestMainBinding


class FcmTestMainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFcmTestMainBinding
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFcmTestMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firebaseMessaging = FirebaseMessaging.getInstance()
        var fcmToken = ""
        //setMusic() //음악설정

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
            /**
             * https://o-s-z.tistory.com/62
             */
            //TODO 진동
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                 val vib =vibratorManager.defaultVibrator
                 vib.vibrate(VibrationEffect.createOneShot(1000,100))
            } else {
                @Suppress("DEPRECATION")
                val vib = getSystemService(VIBRATOR_SERVICE) as Vibrator
                 vib.vibrate(1000)
            }


        }


        binding.fcmBtn03.setOnClickListener {
            val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION) // 소리를 울리기 위한(TYPE_NOTIFICATION를 다른 거로 바꾸면 설정한 벨소리 등 소리 울리게 가능)

            val ringtone = RingtoneManager.getRingtone(applicationContext, uri)
            ringtone.play()
        }

        binding.fcmBtn04.setOnClickListener {
            if(mediaPlayer!=null){
                mediaPlayer.start()
                //setMusic()
            }
        }

        binding.fcmBtn05.setOnClickListener {
            if(mediaPlayer.isPlaying){
                mediaPlayer.pause()
            }else{
                mediaPlayer.start()
            }
        }
    }

    private fun setMusic(){
        //커스텀 뮤직 설정
        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.keep)//음악선택
        mediaPlayer.isLooping = true
    }
}