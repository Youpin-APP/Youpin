package com.neu.youpin.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neu.youpin.R
import com.neu.youpin.store.ShopItem


class UserItemListAdapter(private val itemList:List<ShopItem>) : RecyclerView.Adapter<UserItemListAdapter.ViewHolder>() {
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val thumbnailImage : ImageView = view.findViewById(R.id.thumbnailImage)
        val itemPrice : TextView = view.findViewById(R.id.itemPrice)
        val itemName : TextView = view.findViewById(R.id.itemName)
        val storeItemBox : LinearLayout = view.findViewById(R.id.storeItemBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.store_item_grid,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val shopItem = itemList[position]
        holder.thumbnailImage.setImageResource(shopItem.pic)
        holder.itemPrice.text = shopItem.price
        holder.itemName.text = shopItem.name
        val lp = holder.storeItemBox.layoutParams as GridLayoutManager.LayoutParams
        lp.leftMargin = 15
        if(position % 2 == 1) {
            lp.rightMargin = 15
        }
        holder.storeItemBox.layoutParams = lp
    }

    override fun getItemCount() = itemList.size
}