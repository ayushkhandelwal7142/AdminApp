package com.akstudios.adminapp.splashScreen

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.akstudios.adminapp.MainActivity
import com.akstudios.adminapp.R
import com.akstudios.adminapp.loginScreen.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {
    private lateinit var schoolName: TextView
    private lateinit var appType: TextView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        firebaseAuth = FirebaseAuth.getInstance()

        schoolName = findViewById(R.id.schoolName)
        appType = findViewById(R.id.adminApp)
        val schoolNameAnim: Animation = AnimationUtils.loadAnimation(this, R.anim.animation2)
        val appTypeAnim: Animation = AnimationUtils.loadAnimation(this, R.anim.animation1)
        schoolName.startAnimation(schoolNameAnim)
        //appType.startAnimation(appTypeAnim)
        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
        editor = sharedPreferences.edit()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object :Runnable{
            override fun run() {
                if (sharedPreferences.getString("isLogin", "false") == "false") {
                    openLoginActivity()
                } else {
                    openMainActivity()
                }
            }
        }, 3000)
//        handler.postDelayed({
//            if (firebaseAuth.currentUser != null) {
//                Toast.makeText(this, "Admin is already logged in!", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//            } else {
//                val intent = Intent(this, LoginActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//        }, 3000)
    }
    private fun openLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}