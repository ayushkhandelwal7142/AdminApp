package com.akstudios.adminapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.akstudios.adminapp.databinding.ActivityMainBinding
import com.akstudios.adminapp.faculty.UpdateFacultyActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addNotice.setOnClickListener {
            val intent = Intent(this, UploadNoticeActivity::class.java)
            startActivity(intent)
        }
        binding.uploadGalleryImage.setOnClickListener {
            val intent = Intent(this, UploadImageActivity::class.java)
            startActivity(intent)
        }
        binding.uploadEbook.setOnClickListener {
            val intent = Intent(this, UploadPdfActivity::class.java)
            startActivity(intent)
        }
        binding.updateFaculty.setOnClickListener {
            val intent = Intent(this, UpdateFacultyActivity::class.java)
            startActivity(intent)
        }
        binding.deleteNotice.setOnClickListener {
            val intent = Intent(this, DeleteNoticeActivity::class.java)
            startActivity(intent)
        }
    }
}