package com.akstudios.adminapp.loginScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.akstudios.adminapp.MainActivity
import com.akstudios.adminapp.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var btnLogin: Button
    private lateinit var etEmailAddress: EditText
    private lateinit var etPassword: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_actiity)

        firebaseAuth = FirebaseAuth.getInstance()
        btnLogin = findViewById(R.id.btnLogin)
        etEmailAddress = findViewById(R.id.etEmailAddress)
        etPassword = findViewById(R.id.etPassword)

        btnLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val email = etEmailAddress.text.toString()
        val password = etPassword.text.toString()

        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Email/password cannot be empty", Toast.LENGTH_SHORT).show()
            return
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){
                if(it.isSuccessful){
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else {
                    Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}