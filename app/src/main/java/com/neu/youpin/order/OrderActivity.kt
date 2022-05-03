package com.neu.youpin.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neu.youpin.R
import java.util.*

class OrderActivity : AppCompatActivity() {
    private val orderInfoList = Vector<OrderInfo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        initList()
        val orderListView = findViewById<RecyclerView>(R.id.orderList)
        orderListView.layoutManager = LinearLayoutManager(this)
        orderListView.adapter = OrderListAdapter(orderInfoList)

        val sendText = findViewById<TextView>(R.id.searchBox) //初始化文本
        val left = resources.getDrawable(R.drawable.search_img)
        left.setBounds(0, 0, 50, 50) //必须设置图片的大小否则没有作用

        sendText.setCompoundDrawables(left, null, null, null) //设置图片left这里如果是右边就放到第二个参数里面依次对应
    }

    private fun initList() {
        repeat(2) {
            var singleImg = Vector<Int>()
            singleImg.addElement(R.drawable.item_img)
            var multiImg = Vector<Int>()
            repeat(6) {
                multiImg.addElement(R.drawable.item_img)
            }
            orderInfoList.addElement(OrderInfo("furry",singleImg,100,"蓝色"))
            orderInfoList.addElement(OrderInfo("furry",singleImg,100,"蓝色"))
            orderInfoList.addElement(OrderInfo("",multiImg,100,"蓝色"))
            orderInfoList.addElement(OrderInfo("",multiImg,100,"蓝色"))
        }
    }
}