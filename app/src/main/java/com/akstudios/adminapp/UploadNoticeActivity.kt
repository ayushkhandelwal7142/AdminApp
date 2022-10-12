package com.akstudios.adminapp

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.akstudios.adminapp.dataClasses.NoticeData
import com.akstudios.adminapp.databinding.ActivityUploadNoticeBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class UploadNoticeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadNoticeBinding
    private val REQ: Int = 1
    private  var bitmap: Bitmap? = null
    private lateinit var noticeTitle: EditText
    private lateinit var btnUploadNotice: Button
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private var downloadUrl: String = ""
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_notice)

        databaseReference = FirebaseDatabase.getInstance().reference
        storageReference = FirebaseStorage.getInstance().reference
        progressDialog = ProgressDialog(this)

        binding = ActivityUploadNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addImage.setOnClickListener {
            openGallery()
        }
        binding.btnUploadNotice.setOnClickListener {
            if (binding.noticeTitle.text.toString().isEmpty()) {
                binding.noticeTitle.error = "Please type Notice Title"
                binding.noticeTitle.requestFocus()
            } else if (bitmap == null) {
                Toast.makeText(this, "Add Notice Image First", Toast.LENGTH_LONG).show()
            } else {
                uploadImage()
            }
        }
    }

    /**
     * Uploading image file on cloud storage
     */
    private fun uploadImage() {
        progressDialog.setMessage("Uploading...")
        progressDialog.show()
        //compress image and store
        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 50, baos)

        val finalImage = baos.toByteArray()
        val filePath: StorageReference = storageReference.child("Notice").child("$finalImage jpg")
        val uploadTask: UploadTask = filePath.putBytes(finalImage)
        uploadTask.addOnCompleteListener(this@UploadNoticeActivity, OnCompleteListener {
                if (it.isSuccessful) {
                    uploadTask.addOnSuccessListener {
                        filePath.downloadUrl.addOnSuccessListener {
                            downloadUrl = it.toString()
                            uploadData()
                        }
                    }
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Error Uploading Image", Toast.LENGTH_LONG).show()
                }
        })

    }

    /**
     * Uploading data on Realtime Database
     * Image url is uploaded for image files that are stored on cloud
     */
    private fun uploadData() {
        //Parent child Notice
        databaseReference = databaseReference.child("Notice")
        // Unique key for Notice
        val uniqueKey = databaseReference.push().key

        // Get Data and time
        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.UK)
        val date = currentDate.format(Date())
        val currentTime = SimpleDateFormat("hh:mm a", Locale.UK) //a for am-pm
        val time = currentTime.format(Date())

        //Push data to NoticeData class
        val title = binding.noticeTitle.text.toString()
        val noticeData = NoticeData(title, downloadUrl, uniqueKey, date, time)

        if (uniqueKey != null) {
            databaseReference.child(uniqueKey).setValue(noticeData).addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Notice Uploaded", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Something Went Wrong. Please try again", Toast.LENGTH_LONG).show()
            }
        } else {
            progressDialog.dismiss()
            Toast.makeText(this, "Error. Could not found Unique Key", Toast.LENGTH_LONG).show()
        }
    }

    private fun openGallery() {
        val pickImage = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickImage, REQ)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ && resultCode == RESULT_OK) {
            val uri: Uri? = data?.data
            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            binding.imgViewNotice.setImageBitmap(bitmap)
        }
    }
}