package com.neu.youpin.orderDetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.neu.youpin.R
import com.neu.youpin.order.OrderImgListAdapter
import java.util.*

class OrderDetailListAdapter(private var infoList: Vector<OrderDetailInfo>) :

    RecyclerView.Adapter<OrderDetailListAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image : ImageView = view.findViewById(R.id.orderDetailImg)
        val name : TextView = view.findViewById(R.id.orderDetailName)
        val description : TextView = view.findViewById(R.id.orderDetailDescription)
        val price : TextView = view.findViewById(R.id.orderDetailSinglePrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_detail_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = infoList[position]
        holder.image.setImageResource(info.pic)
        holder.name.text = info.name
        holder.description.text = info.description
        holder.price.text = info.price.toString()
    }

    override fun getItemCount() = infoList.size
}