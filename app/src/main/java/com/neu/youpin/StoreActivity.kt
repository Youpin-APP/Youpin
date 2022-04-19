package com.neu.youpin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StoreActivity : AppCompatActivity() {
    private val shopItemList = ArrayList<ShopItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)
        initItem()
        val layoutManager = LinearLayoutManager(this)
        val recyclerView = findViewById<RecyclerView>(R.id.itemList)
        recyclerView.layoutManager = layoutManager
        val adapter = StoreListAdapter(shopItemList)
        recyclerView.adapter = adapter
    }

    private fun initItem() {
        repeat(10) {
            shopItemList.add(ShopItem("furry",R.drawable.item_img,"11415元"))
        }
    }
}