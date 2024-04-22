package com.example.weatherapp_mobapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import com.example.weatherapp_mobapp.databinding.ActivityA1SplashScreenBinding

class A1SplashScreen : AppCompatActivity() {

    private val view by lazy { ActivityA1SplashScreenBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(view.root);

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, A2MainActivity::class.java))
            finish()
        }, 3000)
    }
}