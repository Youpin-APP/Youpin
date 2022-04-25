package com.neu.youpin.cart

import android.os.Build
import android.os.Bundle
import android.text.BoringLayout
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.neu.youpin.R
import kotlinx.coroutines.selects.select
import java.util.*
import java.util.function.Predicate


class CartActivity : AppCompatActivity() , OnItemClickListener{
    private val cartItemList = Vector<CartItem>()
    private lateinit var adapter : CartListAdapter
    private var isEditing = false
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        initItem()
        val layoutManager = LinearLayoutManager(this)
        val recyclerView = findViewById<RecyclerView>(R.id.cartItemList)
        recyclerView.layoutManager = layoutManager
        adapter = CartListAdapter(cartItemList)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(this)
        //禁用RecyclerView 刷新动画
        (recyclerView.itemAnimator as SimpleItemAnimator?)!!.supportsChangeAnimations = false

        val cartItemSelectAll = findViewById<CheckBox>(R.id.selectAllCart)
        val editCart = findViewById<Button>(R.id.editCart)
        val cartCheckout = findViewById<Button>(R.id.cartCheckoutButton)
        cartItemSelectAll.setOnClickListener {
            Log.d("selectAll", "click" + cartItemSelectAll.isChecked)
            if(cartItemSelectAll.isChecked) {
                for (cartItem in cartItemList) {
                    cartItem.selected = true
                }
                adapter.notifyItemRangeChanged(0,adapter.itemCount)
            }
            else {
                for (cartItem in cartItemList) {
                    cartItem.selected = false
                }
                adapter.notifyItemRangeChanged(0,adapter.itemCount)
            }
        }
        editCart.setOnClickListener {
            isEditing = !isEditing
            if(isEditing) {
                editCart.text = "完成"
                cartCheckout.text = "删除"
            }
            else {
                editCart.text = "编辑"
                cartCheckout.text = "结算"
            }
        }
        cartCheckout.setOnClickListener {
            if(isEditing){
                cartItemList.removeIf { it.selected }
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun initItem() {
        repeat(10) {
            cartItemList.add(CartItem("furry", R.drawable.item_img,"11415元",
                20,"蓝白"))
        }
    }

    override fun onItemClick(pos: Int, id : Int) {
        when(id) {
            R.id.cartItemPlus -> {
                cartItemList[pos].count++
                adapter.notifyItemChanged(pos)
            }
            R.id.cartItemSub -> {
                cartItemList[pos].count--
                adapter.notifyItemChanged(pos)
            }
            R.id.cartItemCheckBox -> {
                Log.d("checkItem","")
                val cartItemSelectAll = findViewById<CheckBox>(R.id.selectAllCart)
                if(cartItemList[pos].selected && cartItemSelectAll.isChecked) {
                    cartItemSelectAll.isChecked = false
                }
                cartItemList[pos].selected = !cartItemList[pos].selected
                if(cartItemList[pos].selected) {
                    var selectAll = true
                    for (cartItem in cartItemList) {
                        if(!cartItem.selected) {
                            selectAll = false
                            break
                        }
                    }
                    if(selectAll) {
                        cartItemSelectAll.isChecked = true
                    }
                }
            }
        }
    }
}