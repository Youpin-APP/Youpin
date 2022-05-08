package com.neu.youpin.orderDetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.neu.youpin.R
import java.util.*
import kotlin.collections.ArrayList

class OrderDetailListAdapter(private var infoList: List<Info>?) :

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
        val info = infoList?.get(position)
//        holder.image.setImageResource(info.pic)
        if (info != null) {
            holder.name.text = info.name
            holder.description.text = "数量:" + info.count + "  " + info.type
            holder.price.text = info.price.toString()
            Glide.with(holder.itemView.context)
                .load("http://hqyz.cf:8080/pic/" +info.pic)
                .into(holder.image)
        }
    }

    override fun getItemCount() : Int{
        return if(infoList != null) {
            infoList!!.size
        } else {
            0
        }
    }

    fun setData(infoList: List<Info>) {
        this.infoList = infoList
        notifyDataSetChanged()
    }
}