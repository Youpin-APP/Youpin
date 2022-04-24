package com.neu.youpin.store

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.neu.youpin.R

class StoreListAdapter(private val itemList:List<ShopItem>) : RecyclerView.Adapter<StoreListAdapter.ViewHolder>() {
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val thumbnailImage : ImageView = view.findViewById(R.id.thumbnailImage)
        val itemPrice : TextView = view.findViewById(R.id.itemPrice)
        val itemName : TextView = view.findViewById(R.id.itemName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_linear,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val shopItem = itemList[position]
        holder.thumbnailImage.setImageResource(shopItem.pic)
        holder.itemPrice.text = shopItem.price
        holder.itemName.text = shopItem.name
    }

    override fun getItemCount() = itemList.size
}