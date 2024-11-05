package com.blog.app.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blog.app.R
import com.blog.app.model.data.getUsers.Data
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class UsersListAdapter(
    private var context: Context,
    private var userData: List<Data>

): RecyclerView.Adapter<UsersListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.adapter_user_list, parent, false)
        return MyViewHolder(view)
    }

    fun setData(userData: List<Data>) {
        this.userData = userData
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat", "NotifyDataSetChanged",
        "ClickableViewAccessibility"
    )
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dataInfo: Data = userData[position]

        val userId = dataInfo.id
        val usersFirstName = dataInfo.first_name
        val avatar = dataInfo.avatar

        Glide.with(context).load(avatar).into(holder.usersAvatar)
        holder.usersFirstName.text = usersFirstName

        holder.itemView.setOnClickListener {
//            val intent = Intent(context, UserDetailsActivity::class.java)
//            intent.putExtra("userId", userId)
//            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userData.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usersAvatar: CircleImageView = itemView.findViewById(R.id.usersAvatar)
        val usersFirstName: TextView = itemView.findViewById(R.id.usersFirstName)
    }
}
