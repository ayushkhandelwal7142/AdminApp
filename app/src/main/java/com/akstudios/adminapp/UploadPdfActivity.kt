package com.akstudios.adminapp

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.akstudios.adminapp.databinding.ActivityUploadPdfBinding
import com.akstudios.adminapp.services.Constants
import com.akstudios.adminapp.services.Notification
import com.akstudios.adminapp.services.NotificationData
import com.akstudios.adminapp.services.PushNotifications
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class UploadPdfActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadPdfBinding
    private val REQ: Int = 1
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private var downloadUrl: String = ""
    private var pdfName: String? = null
    private lateinit var progressDialog: ProgressDialog
    private var pdfData: Uri? = null
    private lateinit var pdfTitle: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_pdf)

        binding = ActivityUploadPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseReference = FirebaseDatabase.getInstance().reference.child("pdf")
        storageReference = FirebaseStorage.getInstance().reference.child("pdf")
        progressDialog = ProgressDialog(this)

        binding.addPdf.setOnClickListener {
            openGallery()
        }
        binding.btnUploadPdf.setOnClickListener {
            pdfTitle = binding.pdfTextView.text.toString()
            if (pdfTitle.isEmpty()) {
                if (binding.pdfTitle.text?.isEmpty() == true) {
                    binding.pdfTitle.error = "Empty"
                    binding.pdfTitle.requestFocus()
                    Toast.makeText(this, "Please add pdf title", Toast.LENGTH_LONG).show()
                } else {
                    pdfTitle = binding.pdfTitle.text.toString()
                }
            } else if (pdfData == null) {
                Toast.makeText(this, "Please upload pdf", Toast.LENGTH_LONG).show()
            } else {
                uploadPdf()
            }
        }
    }

    private fun uploadPdf() {
        progressDialog.apply {
            setTitle("Please wait")
            setMessage("Uploading Pdf...")
            show()
        }
        storageReference = storageReference.child("Pdf/$pdfName-"+ System.currentTimeMillis()+".pdf")
        pdfData?.let { storageReference.putFile(it) }
            ?.addOnSuccessListener {
                val uploadTask: Task<Uri> = it.storage.downloadUrl
                while (!uploadTask.isComplete) { }
                val result = uploadTask.result
                uploadData(result.toString())
            }?.addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            }

    }

    private fun uploadData(downloadUrl: String) {
        val uniqueKey = databaseReference.push().key

        val data: HashMap<String, String> = HashMap()
        data.put("pdfTitle", pdfTitle )
        data.put("pdfUrl", downloadUrl)

        if (uniqueKey != null) {
            databaseReference.child(uniqueKey).setValue(data).addOnCompleteListener {
                progressDialog.dismiss()
                binding.pdfTextView.text = ""
                Toast.makeText(this, "Pdf uploaded successfully", Toast.LENGTH_LONG).show()
                val notification = PushNotifications(NotificationData("New Pdf Uploaded", "Hey! You have one unchecked Pdf file"), Constants.TOPIC)
                Notification().sendNotification(this, notification)
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
                finish()
            }.addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to upload Pdf", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select pdf file"), REQ)
    }

    @SuppressLint("Range", "NewApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ && resultCode == RESULT_OK) {
            pdfData = data?.data
            if (pdfData.toString().startsWith("content://")) {
                try {
                    val cursor: Cursor? = pdfData?.let { contentResolver.query(it, null, null ,null) }
                    if (cursor != null && cursor.moveToFirst()) {
                        pdfName = cursor?.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)).toString()
                        if (pdfName == null) {
                            pdfName = pdfData?.path
                            val cut: Int? = pdfName?.lastIndexOf('/')
                            if (cut != -1) {
                                if (cut != null) {
                                    pdfName = pdfName?.substring(cut +  1)
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else if (pdfData.toString().startsWith("file://")) {
                pdfName = File(pdfData.toString()).name
            }
            binding.pdfTextView.text = pdfName
        }
    }
}