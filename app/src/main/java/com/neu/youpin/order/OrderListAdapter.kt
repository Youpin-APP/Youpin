package com.neu.youpin.order

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neu.youpin.R
import java.util.*

enum class ItemType {
    SINGLE,MULTI
}

class OrderListAdapter(private var infoList: Vector<OrderInfo>) :

    RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderItemState : TextView = view.findViewById(R.id.orderItemState)
        val orderItemImg : ImageView? = view.findViewById(R.id.orderItemImg)
        val orderItemImgList : RecyclerView? = view.findViewById(R.id.orderItemImgList)
        val orderItemName : TextView? = view.findViewById(R.id.orderItemName)
        val orderItemTotalPrice : TextView = view.findViewById(R.id.orderItemTotalPrice)
    }

    override fun getItemViewType(position: Int): Int {
        return if (infoList[position].pic.size == 1) {
            ItemType.SINGLE.ordinal
        } else {
            ItemType.MULTI.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.order_item_single, parent, false)
        if(viewType == ItemType.MULTI.ordinal) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.order_item_multi, parent, false)
        }
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrderListAdapter.ViewHolder, position: Int) {
        val orderInfo = infoList[position]
        holder.orderItemTotalPrice.text = "合计\n" + orderInfo.price + "元"
        holder.orderItemState.text = "完成"
        if(orderInfo.pic.size == 1) {
            holder.orderItemName!!.text = orderInfo.name
            holder.orderItemImg!!.setImageResource(orderInfo.pic[0])
        }
        else {
            val layoutManager = object : LinearLayoutManager(holder.orderItemState.context, LinearLayoutManager.HORIZONTAL,false) {
                override fun canScrollHorizontally(): Boolean {
                    return false
                }
            }
            holder.orderItemImgList!!.layoutManager = layoutManager
            holder.orderItemImgList.adapter = OrderImgListAdapter(orderInfo.pic)
            holder.orderItemImgList.isNestedScrollingEnabled = false
        }
    }

    override fun getItemCount() = infoList.size
}