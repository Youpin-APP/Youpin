package com.neu.youpin.order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.neu.youpin.R
import java.util.*

class OrderImgListAdapter(private var imgList: Vector<Int>) :

    RecyclerView.Adapter<OrderImgListAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderItemImg : ImageView = view.findViewById(R.id.orderItemImg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item_img, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.orderItemImg.setImageResource(imgList[position])
    }

    override fun getItemCount() = imgList.size
}