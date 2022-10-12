package com.akstudios.adminapp.faculty

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.akstudios.adminapp.R
import com.bumptech.glide.Glide

class FacultyAdapter(private val list: ArrayList<FacultyData>, private val category: String, val context: Context): RecyclerView.Adapter<FacultyAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.facultyNameRV)
        val email: TextView = itemView.findViewById(R.id.facultyEmailRV)
        val post: TextView = itemView.findViewById(R.id.facultyPostRV)
        val profilePic: ImageView = itemView.findViewById(R.id.facultyImage)
        val btnUpdateInfo: Button = itemView.findViewById(R.id.btnFacultyUpdateInfoRV)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.update_faculty_cv, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = list[position].name
        holder.email.text = list[position].email
        holder.post.text = list[position].post
        Glide.with(context).load(list[position].url).into(holder.profilePic)

        holder.btnUpdateInfo.setOnClickListener {
           val intent = Intent(context, UpdateTeacherActivity::class.java)
            intent.putExtra("name", list[position].name)
            intent.putExtra("email", list[position].email)
            intent.putExtra("post", list[position].post)
            intent.putExtra("imageUrl", list[position].url)
            intent.putExtra("uniqueKey", list[position].uniqueKey)
            intent.putExtra("category", category)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
       return list.size
    }
}