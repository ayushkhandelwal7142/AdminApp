package com.akstudios.adminapp

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.akstudios.adminapp.dataClasses.NoticeData
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class NoticeAdapter(val list: ArrayList<NoticeData>, val context: Context) :
    RecyclerView.Adapter<NoticeAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noticeImage = itemView.findViewById<ImageView>(R.id.deleteNoticeImage)
        val btnDeleteNotice = itemView.findViewById<Button>(R.id.btnDeleteNotice)
        val noticeTitle = itemView.findViewById<TextView>(R.id.noticeTitle)
        val noticeDate = itemView.findViewById<TextView>(R.id.noticeDate)
        val noticeTime = itemView.findViewById<TextView>(R.id.noticeTime)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.delete_notice, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.noticeDate.text = list[position].date
        holder.noticeTime.text = list[position].time
        holder.noticeTitle.text = list[position].title
        Glide.with(context).load(list[position].imageUrl).into(holder.noticeImage)
        holder.btnDeleteNotice.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.apply {
                setMessage("Are you sure you want to delete thi Notice?")
                setCancelable(true)
                setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
                    val databaseReference: DatabaseReference =
                        FirebaseDatabase.getInstance().reference.child("Notice")
                    databaseReference.child(list[position].key.toString()).removeValue()
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
            if (alertDialog != null) {
                alertDialog?.show()
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}