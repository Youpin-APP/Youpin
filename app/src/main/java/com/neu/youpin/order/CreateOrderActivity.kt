package com.neu.youpin.order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neu.youpin.R
import com.neu.youpin.location.LocaListActivity
import com.neu.youpin.orderDetail.OrderDetailInfo
import com.neu.youpin.orderDetail.OrderDetailListAdapter
import com.neu.youpin.store.StoreActivity
import kotlinx.android.synthetic.main.activity_create_order.*
import java.util.*

class CreateOrderActivity : AppCompatActivity() {
    private val createOrderInfoList = Vector<OrderDetailInfo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_order)
        initList()
        val layoutManager = LinearLayoutManager(this)
        val recyclerView = findViewById<RecyclerView>(R.id.CreateOrderList)
        recyclerView.layoutManager = layoutManager
        val adapter = OrderDetailListAdapter(createOrderInfoList)
        recyclerView.adapter = adapter

        CreateOrderBack.setOnClickListener {
            finish()
        }

        CreateOrderLoca.setOnClickListener {
            val intent = Intent(this, LocaListActivity::class.java)
            startActivity(intent)
        }

        // 监听checkbox的状态变化 以实现互斥
        CreateOrderWechat.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                CreateOrderAlipay.isChecked = false
            }
        }
        CreateOrderAlipay.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                CreateOrderWechat.isChecked = false
            }
        }
    }

    private fun initList() {
        repeat(10) {
            createOrderInfoList.add(
                OrderDetailInfo(
                    "furry", 11415,
                    "数量 1 颜色: 蓝色", R.drawable.item_img
                )
            )
        }
    }
}