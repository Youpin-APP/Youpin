package com.neu.youpin.order

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.neu.youpin.Interface.OnItemClickListener
import com.neu.youpin.R
import kotlin.collections.ArrayList

enum class ItemType {
    SINGLE,MULTI
}

class OrderListAdapter(private var infoList: ArrayList<OrderInfo>) :

    RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {

    private var onItemClickListener : OnItemClickListener? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderItemState : TextView = view.findViewById(R.id.orderItemState)
        val orderItemImg : ImageView? = view.findViewById(R.id.orderItemImg)
        val orderItemImgList : RecyclerView? = view.findViewById(R.id.orderItemImgList)
        val orderItemName : TextView? = view.findViewById(R.id.orderItemName)
        val orderItemTotalPrice : TextView = view.findViewById(R.id.orderItemTotalPrice)
        val orderItem : RelativeLayout = view.findViewById(R.id.orderItem)
    }

    override fun getItemViewType(position: Int): Int {
        return if (infoList[position].pics.size == 1) {
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

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onBindViewHolder(holder: OrderListAdapter.ViewHolder, position: Int) {
        val orderInfo = infoList[position]
        holder.orderItemTotalPrice.text = "合计\n" + orderInfo.totalPrice + "元"
        holder.orderItemState.text = orderInfo.stateName
        if(orderInfo.pics.size == 1) {
            holder.orderItemName!!.text = orderInfo.name
//            holder.orderItemImg!!.setImageResource(orderInfo.pics[0])
            holder.orderItemImg?.let {
                Glide.with(holder.orderItemState.context)
                    .load("http://hqyz.cf:8080/pic/" +orderInfo.pics[0])
                    .into(it)
            }
        }
        else {
            val layoutManager = object : LinearLayoutManager(holder.orderItemState.context, LinearLayoutManager.HORIZONTAL,false) {
                override fun canScrollHorizontally(): Boolean {
                    return false
                }
            }
            holder.orderItemImgList!!.layoutManager = layoutManager
            holder.orderItemImgList.adapter = OrderImgListAdapter(orderInfo.pics)
            holder.orderItemImgList.isNestedScrollingEnabled = false
        }

        holder.orderItem.setOnClickListener {
            if(onItemClickListener != null) {
                onItemClickListener?.onItemClick(position, holder.orderItem.id)
            }
            Log.d("item","click")
        }
        holder.orderItemImg?.setOnClickListener {
            if(onItemClickListener != null) {
                onItemClickListener?.onItemClick(position, holder.orderItem.id)
            }
            Log.d("item","click")
        }
        holder.orderItemImgList?.setOnTouchListener(object :View.OnTouchListener {
            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                if(onItemClickListener != null && p1?.action == MotionEvent.ACTION_UP) {
                    onItemClickListener?.onItemClick(position, holder.orderItem.id)
                    Log.d("item","click")
                }
                return false
            }

        })
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun getItemCount() = infoList.size
}