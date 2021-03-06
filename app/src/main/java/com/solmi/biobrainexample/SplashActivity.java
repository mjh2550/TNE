package com.solmi.biobrainexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.solmi.biobrainexample.home.HomeKtActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler hd = new Handler(Looper.getMainLooper());
        hd.postDelayed(new SplashHandler(),2000);

    }

    private class SplashHandler implements Runnable {
        @Override
        public void run() {
           /* startActivity(new Intent(getApplication(),MainActivity.class));*/
//            startActivity(new Intent(getApplication(), LoginActivity.class));
            startActivity(new Intent(getApplication(), HomeKtActivity.class));
            SplashActivity.this.finish();

        }
    }
}