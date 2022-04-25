package com.neu.youpin.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.neu.youpin.R
import java.util.*


interface OnItemClickListener {
    fun onItemClick(pos: Int, id : Int)
}

class CartListAdapter(private var itemList: Vector<CartItem>) :
    RecyclerView.Adapter<CartListAdapter.ViewHolder>() {
    lateinit var listener: OnItemClickListener

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cartItemImg: ImageView = view.findViewById(R.id.cartItemImg)
        val cartItemPrice: TextView = view.findViewById(R.id.cartItemPrice)
        val cartItemName: TextView = view.findViewById(R.id.cartItemName)
        val cartItemType: TextView = view.findViewById(R.id.cartItemType)
        val cartItemCount: TextView = view.findViewById(R.id.cartItemCount)
        val cartItemBox: RelativeLayout = view.findViewById(R.id.cartItemBox)
        val cartItemPlus: ImageButton = view.findViewById(R.id.cartItemPlus)
        val cartItemSub: ImageButton = view.findViewById(R.id.cartItemSub)
        val cartItemCheckBox: CheckBox = view.findViewById(R.id.cartItemCheckBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = itemList[position]
        holder.cartItemImg.setImageResource(cartItem.pic)
        holder.cartItemPrice.text = cartItem.price
        holder.cartItemName.text = cartItem.name
        holder.cartItemCount.text = cartItem.count.toString()
        holder.cartItemType.text = cartItem.type
        holder.cartItemCheckBox.isChecked = itemList[position].selected
        if (position != itemList.size - 1) {
            val lp: RecyclerView.LayoutParams =
                holder.cartItemBox.layoutParams as (RecyclerView.LayoutParams)
            lp.topMargin = 25
            lp.leftMargin = 25
            lp.rightMargin = 25
            holder.cartItemBox.layoutParams = lp
        } else {
            val lp: RecyclerView.LayoutParams =
                holder.cartItemBox.layoutParams as (RecyclerView.LayoutParams)
            lp.topMargin = 25
            lp.bottomMargin = 25
            lp.leftMargin = 25
            lp.rightMargin = 25
            holder.cartItemBox.layoutParams = lp
        }

        holder.cartItemSub.isEnabled = itemList[position].count > 1

        holder.cartItemType.setOnClickListener {
            onClick(R.id.cartItemType, position)
        }
        holder.cartItemSub.setOnClickListener {
            onClick(R.id.cartItemSub, position)
        }
        holder.cartItemPlus.setOnClickListener {
            onClick(R.id.cartItemPlus, position)
        }
        holder.cartItemCheckBox.setOnClickListener {
            onClick(R.id.cartItemCheckBox, position)
        }

    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        listener = onItemClickListener
    }

    override fun getItemCount() = itemList.size

    private fun onClick(id : Int, position: Int) {
        listener.onItemClick(position, id)
    }
}