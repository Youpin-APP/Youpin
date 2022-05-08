package com.neu.youpin.order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.neu.youpin.R
import java.util.*
import kotlin.collections.ArrayList

class OrderImgListAdapter(private var imgList: Vector<String>) :

    RecyclerView.Adapter<OrderImgListAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderItemImg : ImageView = view.findViewById(R.id.orderItemImg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item_img, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.orderItemImg.setImageResource(imgList[position])
        Glide.with(holder.orderItemImg.context)
            .load("http://hqyz.cf:8080/pic/" + imgList[position])
            .into(holder.orderItemImg)
    }

    override fun getItemCount() = imgList.size
}