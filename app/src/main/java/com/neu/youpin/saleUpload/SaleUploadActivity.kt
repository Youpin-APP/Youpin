package com.neu.youpin.saleUpload

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neu.youpin.R
import com.neu.youpin.store.ShopItem
import com.neu.youpin.store.StoreListAdapter
import java.util.*

class SaleUploadActivity : AppCompatActivity() {
    private val detailImgItemList = Vector<ImgItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sale_upload)
        initItem()
        val layoutManager = LinearLayoutManager(this)
        val recyclerView = findViewById<RecyclerView>(R.id.StoreItemList)
        recyclerView.layoutManager = layoutManager
        val adapter = SaleUploadDetailListAdapter(detailImgItemList)
        recyclerView.adapter = adapter
    }

    private fun initItem() {
        repeat(10) {
            detailImgItemList.add(ImgItem("http://hqyz.cf:8080/pic/fc916569-2ff8-4841-b582-922e70139945.jpg",false))
        }
    }
}