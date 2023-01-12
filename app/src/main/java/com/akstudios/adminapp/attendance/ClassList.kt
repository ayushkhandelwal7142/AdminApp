package com.akstudios.adminapp.attendance

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.akstudios.adminapp.R
import com.akstudios.adminapp.databinding.ActivityClassListBinding

class ClassList : AppCompatActivity() {
    private lateinit var binding: ActivityClassListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class_list)

        binding = ActivityClassListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNursery.setOnClickListener {
            val url = "https://docs.google.com/spreadsheets/d/1RAfGkFFMOgWNxJxbeidTO9xV5U_R1PeAQbvXk9-t28o/edit#gid=186001220" // google sheet link
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
        binding.btnKg1.setOnClickListener {
            val url = "" // google sheet link
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
        binding.btnKg2.setOnClickListener {
            val url = "" // google sheet link
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
        binding.btnClass1.setOnClickListener {
            val url = "" // google sheet link
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
        binding.btnClass2.setOnClickListener {
            val url = "" // google sheet link
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
        binding.btnClass3.setOnClickListener {
            val url = "" // google sheet link
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
        binding.btnClass4.setOnClickListener {
            val url = "" // google sheet link
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }
}