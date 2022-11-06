package com.akstudios.adminapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import com.akstudios.adminapp.loginScreen.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {
    private lateinit var schoolName: TextView
    private lateinit var appType: TextView
    private lateinit var firebaseAuth: FirebaseAuth

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

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            if (firebaseAuth.currentUser != null) {
                Toast.makeText(this, "Admin is already logged in!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 5000)
    }
}