package com.neu.youpin.store

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.neu.youpin.R
import com.neu.youpin.location.LocaUpdateActivity

class StoreListAdapter(private val itemList: List<StoreMap>, private val context: Context) : RecyclerView.Adapter<StoreListAdapter.ViewHolder>() {
    private val picUrlHeader = "http://hqyz.cf:8080/pic/"
    private val mContext = context
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val thumbnailImage : ImageView = view.findViewById(R.id.thumbnailImage)
        val itemPrice : TextView = view.findViewById(R.id.itemPrice)
        val itemName : TextView = view.findViewById(R.id.itemName)
        val linearLayout: LinearLayout = view.findViewById(R.id.storeItemBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.store_item_linear,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val shopItem = itemList[position]
        Glide.with(holder.itemView)
            .load(picUrlHeader.plus(shopItem.picUrl))
            .centerInside()
            .into(holder.thumbnailImage)
        holder.itemPrice.text = shopItem.price.toString().plus("元")
        holder.itemName.text = shopItem.name
        holder.linearLayout.setOnClickListener {
            val shopDetailIntent = Intent(mContext, ShopDetailActivity::class.java).apply {
                putExtra("gid", shopItem.id)
            }
            mContext.startActivity(shopDetailIntent)
        }
    }

    override fun getItemCount() = itemList.size
}