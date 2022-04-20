package com.neu.youpin

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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

        val sendText = findViewById<TextView>(R.id.searchBox) //初始化文本
        val left = resources.getDrawable(R.drawable.search_img)
        left.setBounds(0, 0, 50, 50) //必须设置图片的大小否则没有作用

        sendText.setCompoundDrawables(left, null, null, null) //设置图片left这里如果是右边就放到第二个参数里面依次对应


    }

    private fun initItem() {
        repeat(10) {
            shopItemList.add(ShopItem("furry",R.drawable.item_img,"11415元"))
        }
    }
}