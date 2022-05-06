package com.neu.youpin.store

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.neu.youpin.R
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.activity_shop_detail.*

class ShopDetailActivity : AppCompatActivity() {

    private val imagesList = ArrayList<String>()

    var imageUrls = listOf(
        "http://hqyz.cf:8080/pic/fc916569-2ff8-4841-b582-922e70139945.jpg",
        "https://hqyzcyp.xyz/imgs/2022/04/5a3997b18924a1ef.png",
        "https://hqyzcyp.xyz/imgs/2022/04/500bea1125066f33.png",
        "https://hqyzcyp.xyz/imgs/2022/04/b2a37428f2cb8fdb.png",
        "https://hqyzcyp.xyz/imgs/2022/04/336d8de85f40df50.png"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_detail)

        // 设置商品缩略图
        val banner: Banner<String, BannerImageAdapter<String>> = findViewById(R.id.ShopDetailBanner)
        banner.apply {
            addBannerLifecycleObserver(this@ShopDetailActivity)
            indicator = CircleIndicator(this@ShopDetailActivity)
            setAdapter(object : BannerImageAdapter<String>(imageUrls) {
                override fun onBindView(holder: BannerImageHolder, data: String, position: Int, size: Int) {
                    Glide.with(this@ShopDetailActivity)
                        .load(data)
                        .into(holder.imageView)
                }
            })
            setOnBannerListener { _: String, position: Int ->
                Log.d("HomeFragment", "第" + position + "张图片")
                sOut(position)
            }
            isAutoLoop(false)
            stop()
        }

        // 设置商品详情页
        initImagesList()
        val layoutManager = LinearLayoutManager(this) //线性布局布局管理器
        val recyclerView:RecyclerView = findViewById(R.id.ShopDetailRecyclerView)
        recyclerView.layoutManager = layoutManager
        val adapter = ListAdapter(imagesList)
        recyclerView.adapter = adapter

        // 设置按钮点击事件
        // 返回按钮，触发结束当前activity
        ShopDetailBack.setOnClickListener {
            finish()
        }
    }
    private fun sOut(position: Int){
        Toast.makeText(this,"第" + position + "张图片",Toast.LENGTH_SHORT).show()
    }
    private fun initImagesList(){
        imagesList.add("https://hqyzcyp.xyz/imgs/2022/04/0b177d93eb6b2f38.jpg")
        imagesList.add("https://hqyzcyp.xyz/imgs/2022/05/5617101d04c918d3.jpg")
        imagesList.add("https://hqyzcyp.xyz/imgs/2022/05/ba230524e2edb019.jpg")
        imagesList.add("https://hqyzcyp.xyz/imgs/2022/05/f45fc358818cef4c.jpg")
        imagesList.add("https://hqyzcyp.xyz/imgs/2022/05/5ac0b37f84c0fa75.jpg")
        imagesList.add("https://hqyzcyp.xyz/imgs/2022/05/7a8afea8faa06488.jpg")
        imagesList.add("https://hqyzcyp.xyz/imgs/2022/05/42c91f33d7c832d7.jpg")
        imagesList.add("https://hqyzcyp.xyz/imgs/2022/05/da16b0448bc6f635.jpg")
    }
}

class ListAdapter(private val imagesList: List<String>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    //自定义嵌套内部类 ViewHolder 来减少 findViewById() 的使用， 继承RecyclerView的ViewHolder
    //通过图片的id获取对应的视图，以便后续操作
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ListImage: ImageView = view.findViewById(R.id.ShopDetailListImage)
    }

    //设置初始的布局
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shop_detail_list_image, parent, false)
        //第一个参数为单个图片对应的布局文件，第二个参数为RecyclerView要显示的位置
        //第三个参数设置为false效果为你在xml中设置为什么具体显示就为什么
        return ViewHolder(view)
    }       //加载布局

    //设置初次加载、滑动时的布局
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val uri = imagesList[position] //获取当前位置的fruit
        Glide.with(holder.itemView)
            .load(uri)
            .centerInside()
            .into(holder.ListImage)
    }

    //获取列表中的项目个数，将其定义为水果数组的个数
    override fun getItemCount() = imagesList.size
}