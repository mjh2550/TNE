package com.solmi.biobrainexample.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.solmi.biobrainexample.R
import com.solmi.biobrainexample.databinding.ActivityPushMessageBinding

class PushMessageActivity : AppCompatActivity() {
    lateinit var binding : ActivityPushMessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPushMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar2)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "푸시 테스트"

        binding.btnPush.setOnClickListener{
            if(binding.edtTitle.length()!=0 && binding.edtBody.length()!=0){
                val title = binding.edtTitle.text
                val body = binding.edtBody.text

                val builder1 = getNotificationBuilder("channerl1","첫번째 채널").apply {
                    //작은아이콘(수신 시 상단 아이콘)
                    setSmallIcon(android.R.drawable.ic_menu_search)
                    //큰아이콘(본문 표시할 메시지 BitMap 객체
                    val bitmap = BitmapFactory.decodeResource(resources,R.mipmap.ic_launcher)
                    setLargeIcon(bitmap)
                    setNumber(100)
                    setContentTitle(title)
                    setContentText(body)
                    //메시지 객체 생성
                    val notification = build()
                    //알림 메시지 관리하는 매니저 객체를 추출
                    val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    //출력 //id값에 따라서 메시지 나타남
                    manager.notify(10,notification)
                }


            }else{
                Toast.makeText(this,"푸시메시지에 빈칸이 있습니다.",Toast.LENGTH_SHORT).show()
            }

        }

        binding.btnFb.setOnClickListener {
            val intent = Intent(this@PushMessageActivity,FcmTestMainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            android.R.id.home -> {
                onBackPressed()
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getNotificationBuilder(id:String , name:String) : NotificationCompat.Builder{

        //OS버전별 분기
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //알림 메시지 관리하는 객체 추출
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            //채널 객체 생성           //id : 기본키 name : 사용자에게 노출하는 메시지 이름
            val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH).apply {
                //LED 색상변경 여부
                enableLights(true)
                //진동 사용 여부
                enableVibration(true)
            }

            //알림 메시지 관리하는 객체에 채널 등록
            manager.createNotificationChannel(channel)

            return  NotificationCompat.Builder(this, id)

        }else{
            return  NotificationCompat.Builder(this)
        }
    }
}