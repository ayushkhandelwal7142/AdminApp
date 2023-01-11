package com.akstudios.adminapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akstudios.adminapp.dataClasses.NoticeData
import com.akstudios.adminapp.databinding.ActivityDeleteNoticeBinding
import com.akstudios.adminapp.faculty.FacultyAdapter
import com.google.firebase.database.*

class DeleteNoticeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeleteNoticeBinding
    private var list: ArrayList<NoticeData> = arrayListOf()
    private lateinit var databaseReference: DatabaseReference
    private lateinit var fAdapter: NoticeAdapter
    private lateinit var manager: LinearLayoutManager
    private var lastKey = ""
    private var lastNode = ""
    private var isMaxData = false
    private var isScrolling = false
    private var ITEM_LOAD_COUNT = 10
    private var currentItems: Int = 0
    private var totalItems: Int = 0
    private var scrolledOutItems: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_notice)

        databaseReference = FirebaseDatabase.getInstance().reference.child("Notice")
        binding = ActivityDeleteNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fAdapter = NoticeAdapter(this)
        manager = LinearLayoutManager(this)
        getLastKeyFromFirebase()
        binding.deleteNoticeRV.apply {
            layoutManager = manager
            adapter = fAdapter
            //binding.deleteNoticePB.visibility = View.GONE
        }
        getUsers()
        //getData()
        binding.deleteNoticeRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentItems = manager.getChildCount()
                totalItems = manager.getItemCount()
                scrolledOutItems = manager.findFirstVisibleItemPosition()
                if (isScrolling && currentItems + scrolledOutItems === totalItems) {
                    //  Toast.makeText(getContext(), "fetch data", Toast.LENGTH_SHORT).show();
                    isScrolling = false
                    //fetch data
                    binding.scrollingPB.visibility = View.VISIBLE
                    getUsers()
                }
            }
        })
    }

    private fun getUsers() {
        if (!isMaxData) // 1st fasle
        {
            val query: Query = if (TextUtils.isEmpty(lastNode)) {
                databaseReference
                    .orderByKey()
                    .limitToFirst(ITEM_LOAD_COUNT)
            }  else {
                databaseReference
                    .orderByKey()
                    .startAt(lastNode)
                    .limitToFirst(ITEM_LOAD_COUNT)
            }
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {
                        val newUsers: ArrayList<NoticeData?> = ArrayList()
                        for (userSnapshot in snapshot.children) {
                            newUsers.add(userSnapshot.getValue(NoticeData::class.java))
                        }
                        lastNode =
                            newUsers[newUsers.size - 1]!!.key.toString() //10  if it greater than the toatal items set to visible then fetch data from server
                        if (!lastNode.equals(lastKey)) newUsers.removeAt(newUsers.size - 1) // 19,19 so to renove duplicate removeone value
                        else lastNode = "end"

                        Log.d("LastNode = ", lastNode)
                        // Toast.makeText(getContext(), "last_node"+last_node, Toast.LENGTH_SHORT).show();
                        fAdapter.addAll(newUsers)
                        fAdapter.notifyDataSetChanged()
                        binding.deleteNoticePB.visibility = View.GONE
                    } else  //reach to end no further child avaialable to show
                    {
                        isMaxData = true
                    }
                    binding.scrollingPB.visibility = View.GONE
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        } else {
            binding.scrollingPB.visibility = View.GONE //if data end
        }
    }

    private fun getLastKeyFromFirebase() {
        val getLastKey = databaseReference
            .orderByKey()
            .limitToLast(1)

        getLastKey.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (lastkey in snapshot.children) {
                    lastKey = lastkey.key.toString()
                    Toast.makeText(this@DeleteNoticeActivity, "last_key $lastKey", Toast.LENGTH_SHORT).show()
                    Log.d("LastKey = ", lastKey)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DeleteNoticeActivity, "can not get last key", Toast.LENGTH_SHORT).show()
            }
        })
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
                        fAdapter = NoticeAdapter(this@DeleteNoticeActivity)
                        binding.deleteNoticeRV.apply {
                            setHasFixedSize(true)
                            layoutManager = LinearLayoutManager(this@DeleteNoticeActivity)
                            adapter = fAdapter
                            binding.deleteNoticePB.visibility = View.GONE
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