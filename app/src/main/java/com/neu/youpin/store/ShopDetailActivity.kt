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
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

class ShopDetailActivity : AppCompatActivity() {

    private val imagesList = ArrayList<String>()
    private var gid: Int = 0

    var imageUrls = listOf(
//        "http://hqyz.cf:8080/pic/7ef1640f-e1b4-4a24-8801-b4d7261d62ad.jpg",
        "https://hqyzcyp.xyz/imgs/2022/04/5a3997b18924a1ef.png",
        "https://hqyzcyp.xyz/imgs/2022/04/500bea1125066f33.png",
        "https://hqyzcyp.xyz/imgs/2022/04/b2a37428f2cb8fdb.png",
        "https://hqyzcyp.xyz/imgs/2022/04/336d8de85f40df50.png"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_detail)

        gid = intent.getIntExtra("gid", 0)
        initByRetrofit()

        // 设置按钮点击事件
        // 返回按钮，触发结束当前activity
        ShopDetailBack.setOnClickListener {
            finish()
        }
    }
    private fun sOut(position: Int){
        Toast.makeText(this,"第" + position + "张图片",Toast.LENGTH_SHORT).show()
    }
    private fun initByRetrofit(){

    }

    private fun init(){
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
        val layoutManager = LinearLayoutManager(this) //线性布局布局管理器
        val recyclerView:RecyclerView = findViewById(R.id.ShopDetailRecyclerView)
        recyclerView.layoutManager = layoutManager
        val adapter = ListAdapter(imagesList)
        recyclerView.adapter = adapter
    }
}

data class PicDetail(val url:String, val id:Int)

data class ShopContent(val name:String,)

// 分类 tname 名字 tid 主键
data class TList(var name:String, var id:Int)

data class ShopDetailMap(val name: String, val id: Int, val price: Float, val picUrl: String)

interface ShopDetailService {
    @GET("/goods/getInfo")
    fun getInfo(@Query("name") name: String): Call<List<StoreMap>>
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