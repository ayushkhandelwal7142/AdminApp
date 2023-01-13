package com.akstudios.adminapp.notice

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.akstudios.adminapp.R
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class NoticeAdapter(private val context: Context) :
    RecyclerView.Adapter<NoticeAdapter.MyViewHolder>() {
    private val noticeData: ArrayList<NoticeData?> = arrayListOf()

    fun addAll(list: ArrayList<NoticeData?>) {
        val listSize: Int = list.size
        noticeData.addAll(list)
        notifyItemRangeChanged(listSize, list.size)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noticeImage: ImageView = itemView.findViewById(R.id.deleteNoticeImage)
        val btnDeleteNotice: Button = itemView.findViewById(R.id.btnDeleteNotice)
        val noticeTitle: TextView = itemView.findViewById(R.id.noticeTitle)
        val noticeDate: TextView = itemView.findViewById(R.id.noticeDate)
        val noticeTime: TextView = itemView.findViewById(R.id.noticeTime)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.delete_notice, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.noticeDate.text = noticeData[position]?.date
        holder.noticeTime.text = noticeData[position]?.time
        holder.noticeTitle.text = noticeData[position]?.title
        Glide.with(context).load(noticeData[position]?.imageUrl).into(holder.noticeImage)
        holder.btnDeleteNotice.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.apply {
                setMessage("Are you sure you want to delete thi Notice?")
                setCancelable(true)
                setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
                    val databaseReference: DatabaseReference =
                        FirebaseDatabase.getInstance().reference.child("Notice")
                    databaseReference.child(noticeData[position]?.key.toString()).removeValue()
                        .addOnSuccessListener {
                            Toast.makeText(context, "Notice Deleted", Toast.LENGTH_LONG).show()

                        }.addOnFailureListener {

                        Toast.makeText(
                            context,
                            "Something went wrong. PLease try again",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    notifyItemRemoved(position)
                })
                setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.cancel()
                })
            }
            var alertDialog: AlertDialog? = null
            try {
                alertDialog = builder.create()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            alertDialog?.show()
        }
    }

    override fun getItemCount(): Int {
        Log.d("NoticeData size = ", noticeData.size.toString())
        return noticeData.size
    }
}