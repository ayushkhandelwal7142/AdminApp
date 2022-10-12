package com.akstudios.adminapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akstudios.adminapp.dataClasses.NoticeData
import com.akstudios.adminapp.databinding.ActivityDeleteNoticeBinding
import com.akstudios.adminapp.faculty.FacultyAdapter
import com.google.firebase.database.*

class DeleteNoticeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeleteNoticeBinding
    private lateinit var fAdapter: NoticeAdapter
    private var list: ArrayList<NoticeData> = arrayListOf()
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_notice)

        databaseReference = FirebaseDatabase.getInstance().reference.child("Notice")

        binding = ActivityDeleteNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getData()
    }

    private fun getData() {
        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    // to be written
                } else {
                    for (i in snapshot.children) {
                        val data = i.getValue(NoticeData::class.java)
                        if (data != null) {
                            list.add(data)
                        }
                        fAdapter = NoticeAdapter(list, this@DeleteNoticeActivity)
                        binding.deleteNoticeRV.apply {
                            setHasFixedSize(true)
                            layoutManager = LinearLayoutManager(this@DeleteNoticeActivity)
                            adapter = fAdapter
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}