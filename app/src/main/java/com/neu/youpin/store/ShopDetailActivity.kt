package com.neu.youpin.store

import android.annotation.SuppressLint
import android.content.Intent
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
import com.neu.youpin.entity.ServiceCreator
import com.neu.youpin.entity.UserApplication
import com.neu.youpin.location.*
import com.neu.youpin.order.CreateOrderActivity
import com.neu.youpin.sale.SelectClassMap
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.activity_create_order.*
import kotlinx.android.synthetic.main.activity_shop_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*

class ShopDetailActivity : AppCompatActivity() {

    private var gid: Int = 0
    private var shopDetailMap:ShopDetailMap? = null
    private val shopDetailService = ServiceCreator.create<ShopDetailService>()
    private val locaListService = ServiceCreator.create<LocaListService>()

    private val urlHeader:String = "http://hqyz.cf:8080/pic/"

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

        ShopDetailLocaLayout.setOnClickListener {
            val locaIntent = Intent(this, LocaListActivity::class.java)
            startActivity(locaIntent)
        }

        ShopDetailAddCart.setOnClickListener {
            addToCart(false)
        }

        ShopDetailBuy.setOnClickListener {
            addToCart(true)
        }
    }

    private fun addToCart(isCreate: Boolean){
        UserApplication.getInstance().getId()?.let {
            shopDetailService.putItem(gid, it).enqueue(object : Callback<CartMap> {
                override fun onResponse(call: Call<CartMap>,
                                        response: Response<CartMap>
                ) {
                    val body = response.body()
                    if (body != null && body.success) {
                        if (isCreate){
                            createOrder(body.caid)
                        }else doError("添加到购物车成功")
                    }else{
                        if (isCreate){
                            doError("创建订单失败")
                        }else doError("添加到购物车失败")
                    }
                }

                override fun onFailure(call: Call<CartMap>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("ShopDetailActivity", "network failed")
                }
            })
        }
    }

    private fun createSuccess(oid: Int){
        val intent = Intent(this, CreateOrderActivity::class.java).apply {
            putExtra("oid", oid)
        }
        startActivity(intent)
    }

    private fun createOrder(caid: Int){
        UserApplication.getInstance().getId()?.let {
            shopDetailService.quickCheckout(it, caid).enqueue(object : Callback<OrderMap> {
                override fun onResponse(call: Call<OrderMap>,
                                        response: Response<OrderMap>
                ) {
                    val body = response.body()
                    if (body != null && body.success) {
                        createSuccess(body.oid)
                    }else doError("创建订单失败!")
                }

                override fun onFailure(call: Call<OrderMap>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("ShopDetailActivity", "network failed")
                }
            })
        }
    }

    private fun sOut(position: Int){
        Toast.makeText(this,"第" + position + "张图片",Toast.LENGTH_SHORT).show()
    }

    private fun doError(text: String){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
    }

    private fun initByRetrofit(){
        shopDetailService.getInfo(gid).enqueue(object : Callback<ShopDetailMap> {
            override fun onResponse(call: Call<ShopDetailMap>,
                                    response: Response<ShopDetailMap>
            ) {
                shopDetailMap = response.body()
                if (shopDetailMap != null && shopDetailMap?.success == true) {
                    init()
                }else doError("查询商品详情失败！！")
            }
            override fun onFailure(call: Call<ShopDetailMap>, t: Throwable) {
                t.printStackTrace()
                Log.d("ShopDetailActivity", "network failed")
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if(UserApplication.getInstance().getAid()<0){
            UserApplication.getInstance().getId()?.let {
                locaListService.getAddrList(it).enqueue(object : Callback<List<Loca>> {
                    override fun onResponse(call: Call<List<Loca>>,
                                            response: Response<List<Loca>>
                    ) {
                        val userLocaList = response.body()
                        var defaultIndex = 0
                        for((i, item) in userLocaList!!.withIndex()){
                            if(item.default == 1){
                                defaultIndex = i
                                break
                            }
                        }
                        UserApplication.getInstance().setAid(userLocaList[defaultIndex].aid)
                        initUserAdd(userLocaList[defaultIndex])
                    }

                    override fun onFailure(call: Call<List<Loca>>, t: Throwable) {
                        t.printStackTrace()
                        Log.d("ShopDetailActivity", "network failed")
                    }
                })
            }
        }else{
            locaListService.getAddr(UserApplication.getInstance().getAid()).enqueue(object : Callback<Loca> {
                override fun onResponse(call: Call<Loca>,
                                        response: Response<Loca>
                ) {
                    val loca = response.body()
                    if(loca!=null && loca.success){
                        initUserAdd(loca)
                    }
                }

                override fun onFailure(call: Call<Loca>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("ShopDetailActivity", "network failed")
                }
            })
        }
    }

    private fun initUserAdd(location: Loca){
        ShopDetailUserLocation.text = location.pname.plus(" ").plus(location.cname)
            .plus(" ").plus(location.dname).plus(location.detail)
    }

    @SuppressLint("SetTextI18n")
    private fun init(){
        ShopDetailMainTitle.text = shopDetailMap?.content?.name
        ShopDetailSecondTitle.text = "${shopDetailMap?.type1?.name} ${shopDetailMap?.type2?.name} ${shopDetailMap?.type3?.name}"
        ShopDetailItemPrice.text = shopDetailMap?.content?.price.toString()
        // 设置商品缩略图
        val banner: Banner<PicDetail, BannerImageAdapter<PicDetail>> = findViewById(R.id.ShopDetailBanner)
        banner.apply {
            addBannerLifecycleObserver(this@ShopDetailActivity)
            indicator = CircleIndicator(this@ShopDetailActivity)
            setAdapter(object : BannerImageAdapter<PicDetail>(shopDetailMap?.pic_banner) {
                override fun onBindView(holder: BannerImageHolder, data: PicDetail, position: Int, size: Int) {
                    Glide.with(this@ShopDetailActivity)
                        .load(urlHeader.plus(data.url))
                        .into(holder.imageView)
                }
            })
            setOnBannerListener { _: PicDetail, position: Int ->
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
        val adapter = shopDetailMap?.pic_detail?.let { ListAdapter(it) }
        recyclerView.adapter = adapter
    }
}

data class PicDetail(val id:Int, val url:String)

data class ShopContent(val name:String, val price: Float)

// 分类 tname 名字 tid 主键
data class TList(var name:String, var id:Int)

data class ShopDetailMap(val success:Boolean, val content:ShopContent, val type1:TList, val type2:TList, val type3:TList,
                         val pic_detail: List<PicDetail>, val pic_banner: List<PicDetail>, val sid:Int, val sname: String)

data class CartMap(val success: Boolean, val caid: Int)

data class OrderMap(val success: Boolean, val oid: Int)

interface ShopDetailService {
    @GET("/goods/getInfo")
    fun getInfo(@Query("id") name: Int): Call<ShopDetailMap>

    @FormUrlEncoded
    @POST("/cart/putItem")
    fun putItem(@Field("gid") gid: Int, @Field("uid") uid: String): Call<CartMap>

    @FormUrlEncoded
    @POST("/order/quickCheckout")
    fun quickCheckout(@Field("uid") uid: String, @Field("caid") caid: Int): Call<OrderMap>
}

class ListAdapter(private val imagesList: List<PicDetail>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    private val urlHeader:String = "http://hqyz.cf:8080/pic/"
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
            .load(urlHeader.plus(uri.url))
            .centerInside()
            .into(holder.ListImage)
    }

    //获取列表中的项目个数，将其定义为水果数组的个数
    override fun getItemCount() = imagesList.size
}