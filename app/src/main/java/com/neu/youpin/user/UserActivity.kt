package com.neu.youpin.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neu.youpin.R
import com.neu.youpin.store.ShopItem
import com.neu.youpin.store.StoreListAdapter

class UserActivity : AppCompatActivity() {
    private val shopItemList = ArrayList<ShopItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        initItem()
        val layoutManager : RecyclerView.LayoutManager = object : GridLayoutManager(this,2) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        val recyclerView = findViewById<RecyclerView>(R.id.userItemList)
        recyclerView.layoutManager = layoutManager
        val adapter = UserItemListAdapter(shopItemList)
        recyclerView.adapter = adapter
    }

    private fun initItem() {
        repeat(10) {
            shopItemList.add(ShopItem("furry", R.drawable.item_img,"11415元"))
        }
    }
}