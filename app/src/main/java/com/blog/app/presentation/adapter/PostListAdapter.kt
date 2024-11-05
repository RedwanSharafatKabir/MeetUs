package com.blog.app.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blog.app.R
import com.blog.app.model.data.getPost.PostsDataResponseItem

class PostListAdapter(
    private var context: Context,
    private var postData:  List<PostsDataResponseItem>

): RecyclerView.Adapter<PostListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.adapter_post_list, parent, false)
        return MyViewHolder(view)
    }

    fun setData(postData: List<PostsDataResponseItem>) {
        this.postData = postData
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat", "NotifyDataSetChanged",
        "ClickableViewAccessibility"
    )
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dataInfo: PostsDataResponseItem = postData[position]

        val itemTitle = dataInfo.title
        val body = dataInfo.body

        holder.itemTitle.text = itemTitle
        holder.itemBody.text = body
    }

    override fun getItemCount(): Int {
        return postData.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTitle: TextView = itemView.findViewById(R.id.itemTitle)
        val itemBody: TextView = itemView.findViewById(R.id.itemBody)
    }
}
