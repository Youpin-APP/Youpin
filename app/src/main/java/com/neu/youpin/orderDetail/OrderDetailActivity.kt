package com.neu.youpin.orderDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neu.youpin.R
import com.neu.youpin.store.StoreListAdapter
import java.util.*

class OrderDetailActivity : AppCompatActivity() {
    private val orderDetailInfoList = Vector<OrderDetailInfo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        initList()
        val layoutManager = LinearLayoutManager(this)
        val recyclerView = findViewById<RecyclerView>(R.id.orderDetailList)
        recyclerView.layoutManager = layoutManager
        val adapter = OrderDetailListAdapter(orderDetailInfoList)
        recyclerView.adapter = adapter
    }

    private fun initList() {
        repeat(10) {
            orderDetailInfoList.add(
                OrderDetailInfo(
                    "furry", 11415,
                    "数量 1 颜色: 蓝色", R.drawable.item_img
                )
            )
        }
    }
}