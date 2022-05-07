package com.neu.youpin.store

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.neu.youpin.R

class StoreListAdapter(private val itemList: List<StoreMap>) : RecyclerView.Adapter<StoreListAdapter.ViewHolder>() {
    val picUrlHeader = "http://hqyz.cf:8080/pic/"
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val thumbnailImage : ImageView = view.findViewById(R.id.thumbnailImage)
        val itemPrice : TextView = view.findViewById(R.id.itemPrice)
        val itemName : TextView = view.findViewById(R.id.itemName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.store_item_linear,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val price:Float = 5999.0F
        val shopItem = itemList[position]
        Glide.with(holder.itemView)
            .load(picUrlHeader.plus(shopItem.picUrl))
            .centerInside()
            .into(holder.thumbnailImage)
        holder.itemPrice.text = shopItem.price.toString().plus("元")
        holder.itemName.text = shopItem.name
    }

    override fun getItemCount() = itemList.size
}