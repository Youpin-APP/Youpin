package com.neu.youpin.cart

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.BoringLayout
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
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
    private var totalPrice = 0
    private lateinit var cartTotalPrice : TextView
    @SuppressLint("SetTextI18n")
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
        cartTotalPrice = findViewById(R.id.cartTotalPrice)
        val cartItemSelectAll = findViewById<CheckBox>(R.id.selectAllCart)
        val editCart = findViewById<Button>(R.id.editCart)
        val cartCheckout = findViewById<Button>(R.id.cartCheckoutButton)
        val cartBack = findViewById<Button>(R.id.cartBack)
        cartItemSelectAll.setOnClickListener {
            Log.d("selectAll", "click" + cartItemSelectAll.isChecked)
            totalPrice = 0
            if(cartItemSelectAll.isChecked) {
                for (cartItem in cartItemList) {
                    cartItem.selected = true
                    totalPrice += cartItem.price * cartItem.count
                }
                adapter.notifyItemRangeChanged(0,adapter.itemCount)
            }
            else {
                for (cartItem in cartItemList) {
                    cartItem.selected = false
                }
                adapter.notifyItemRangeChanged(0,adapter.itemCount)
            }
            cartTotalPrice.text = "合计: "+totalPrice.toString()+"元"
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
                totalPrice = 0
                cartTotalPrice.text = "合计: "+totalPrice.toString()+"元"
            }
        }
        cartBack.setOnClickListener {
            finish()
        }
    }

    private fun initItem() {
        repeat(10) {
            cartItemList.add(CartItem("furry", R.drawable.item_img,11415,
                20,"蓝白"))
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onItemClick(pos: Int, id : Int) {
        when(id) {
            R.id.cartItemPlus -> {
                cartItemList[pos].count++
                adapter.notifyItemChanged(pos)
                totalPrice += cartItemList[pos].price
                cartTotalPrice.text = "合计: "+totalPrice.toString()+"元"
            }
            R.id.cartItemSub -> {
                cartItemList[pos].count--
                adapter.notifyItemChanged(pos)
                totalPrice -= cartItemList[pos].price
                cartTotalPrice.text = "合计: "+totalPrice.toString()+"元"
            }
            R.id.cartItemCheckBox -> {
                Log.d("checkItem","")
                cartItemList[pos].selected = !cartItemList[pos].selected
                val cartItemSelectAll = findViewById<CheckBox>(R.id.selectAllCart)
                if(!cartItemList[pos].selected && cartItemSelectAll.isChecked) {
                    cartItemSelectAll.isChecked = false
                }
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
                if(cartItemList[pos].selected) {
                    totalPrice += cartItemList[pos].price * cartItemList[pos].count
                }
                else {
                    totalPrice -= cartItemList[pos].price * cartItemList[pos].count
                }
                cartTotalPrice.text = "合计: "+totalPrice.toString()+"元"
            }
        }
    }
}