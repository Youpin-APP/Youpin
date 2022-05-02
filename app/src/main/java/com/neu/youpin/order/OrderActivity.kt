package com.neu.youpin.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.neu.youpin.R

class OrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        val sendText = findViewById<TextView>(R.id.searchBox) //初始化文本
        val left = resources.getDrawable(R.drawable.search_img)
        left.setBounds(0, 0, 50, 50) //必须设置图片的大小否则没有作用

        sendText.setCompoundDrawables(left, null, null, null) //设置图片left这里如果是右边就放到第二个参数里面依次对应
    }
}