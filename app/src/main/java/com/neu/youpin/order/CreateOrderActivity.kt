package com.neu.youpin.order

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.neu.youpin.R
import com.neu.youpin.entity.ServiceCreator
import com.neu.youpin.entity.UserApplication
import com.neu.youpin.location.Loca
import com.neu.youpin.location.LocaListActivity
import com.neu.youpin.location.LocaListService
import com.neu.youpin.orderDetail.OrderDetailInfo
import com.neu.youpin.orderDetail.OrderDetailListAdapter
import com.neu.youpin.store.CartMap
import com.neu.youpin.store.ShopDetailMap
import kotlinx.android.synthetic.main.activity_create_order.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

class CreateOrderActivity : AppCompatActivity() {
//    private val createOrderInfoList = Vector<OrderDetailInfo>()
    private val locaListService = ServiceCreator.create<LocaListService>()
    private var oid = -1

    private var orderDetail: OrderDetail? = null

    private val createOrderService = ServiceCreator.create<CreateOrderService>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_order)

        oid = intent.getIntExtra("oid", -1)
        if(oid == -1) doError("订单号错误")
        else getByRetrofit()
//        CreateOrderPrice.text = totalPrice.toString()

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

        CreateOrderLoca.setOnClickListener {
            val locaIntent = Intent(this, LocaListActivity::class.java)
            startActivity(locaIntent)
        }

        CreateOrderButtonPay.setOnClickListener {
            if (CreateOrderWechat.isChecked || CreateOrderAlipay.isChecked){
                payByRetrofit()
            }else Toast.makeText(this, "请选择一种支付方式", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initUserAdd(location: Loca){
        CreateOrderUserLocation.text = location.pname.plus(" ").plus(location.cname)
            .plus(" ").plus(location.dname).plus(location.detail)
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
                        editByRetrofit(userLocaList[defaultIndex])
                    }

                    override fun onFailure(call: Call<List<Loca>>, t: Throwable) {
                        t.printStackTrace()
                        Log.d("ShopDetailActivity", "network failed")
                    }
                })
            }
        }else{
            locaListService.getAddr(UserApplication.getInstance().getAid()).enqueue(object :
                Callback<Loca> {
                override fun onResponse(call: Call<Loca>,
                                        response: Response<Loca>
                ) {
                    val loca = response.body()
                    if(loca!=null && loca.success){
                        editByRetrofit(loca)
                    }
                }

                override fun onFailure(call: Call<Loca>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("CreateOrderActivity", "network failed")
                }
            })
        }
    }

    private fun doError(text: String){
        Toast.makeText(this,text, Toast.LENGTH_SHORT).show()
    }

    private fun init(){
        CreateOrderPrice.text = orderDetail?.totalPrice.toString()
        val layoutManager = LinearLayoutManager(this)
        val recyclerView = findViewById<RecyclerView>(R.id.CreateOrderList)
        recyclerView.layoutManager = layoutManager
        val adapter = orderDetail?.infos?.let { OrderDetailAdapter(it) }
        recyclerView.adapter = adapter
    }

    private fun getByRetrofit(){
        createOrderService.getOrderDetail(oid).enqueue(object : Callback<OrderDetail> {
            override fun onResponse(call: Call<OrderDetail>,
                                    response: Response<OrderDetail>
            ) {
                orderDetail = response.body()
                if (orderDetail != null && orderDetail!!.success) {
                    init()
                }else doError("查询订单状态失败")
            }

            override fun onFailure(call: Call<OrderDetail>, t: Throwable) {
                t.printStackTrace()
                Log.d("CreateOrderActivity", "network failed")
            }
        })
    }

    private fun paySuccess(){
        doError("订单支付成功")
        finish()
    }

    private fun payByRetrofit(){
        createOrderService.payOrder(oid).enqueue(object : Callback<CreateOrderMap> {
            override fun onResponse(call: Call<CreateOrderMap>,
                                    response: Response<CreateOrderMap>
            ) {
                val list = response.body()
                if (list != null && list.success) {
                    paySuccess()
                }else doError("支付订单失败")
            }

            override fun onFailure(call: Call<CreateOrderMap>, t: Throwable) {
                t.printStackTrace()
                Log.d("CreateOrderActivity", "network failed")
            }
        })
    }


    private fun editByRetrofit(location: Loca){
        createOrderService.editOrder(oid, location.aid).enqueue(object : Callback<CreateOrderMap> {
            override fun onResponse(call: Call<CreateOrderMap>,
                                    response: Response<CreateOrderMap>
            ) {
                val list = response.body()
                if (list != null && list.success) {
                    initUserAdd(location)
                }else doError("修改订单地址失败")
            }

            override fun onFailure(call: Call<CreateOrderMap>, t: Throwable) {
                t.printStackTrace()
                Log.d("CreateOrderActivity", "network failed")
            }
        })
    }

}

data class Deliver(val otel:String, val name:String, val aid:Int, val addr:String)

data class Basic(val oid: Int, val stateName:String, val state:Int, val otime1: String)

data class Info(val pic:String, val name: String, val type: String,
                val gid:Int, val price:Float, val count: Int)

data class OrderDetail(val totalPrice: Float, val basic: Basic, val success:Boolean,
                        val infos:List<Info>, val deliver: Deliver)

data class CreateOrderMap(val success: Boolean)

interface CreateOrderService {
    @FormUrlEncoded
    @POST("/order/getOrderDetail")
    fun getOrderDetail(@Field("oid") oid: Int): Call<OrderDetail>

    @FormUrlEncoded
    @POST("/order/payOrder")
    fun payOrder(@Field("oid") oid: Int): Call<CreateOrderMap>

    @FormUrlEncoded
    @POST("/order/editOrder")
    fun editOrder(@Field("oid") oid: Int, @Field("aid") aid: Int): Call<CreateOrderMap>
}

fun getNoMoreThanTwoDigits(number: Float): String {
    val format = DecimalFormat("0.##")
    //未保留小数的舍弃规则，RoundingMode.FLOOR表示直接舍弃。
    format.roundingMode = RoundingMode.FLOOR
    return format.format(number)
}

private class OrderDetailAdapter(private var infoList: List<Info>) :
    RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>() {
    private val urlHeader:String = "http://hqyz.cf:8080/pic/"

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image : ImageView = view.findViewById(R.id.orderDetailImg)
        val name : TextView = view.findViewById(R.id.orderDetailName)
        val description : TextView = view.findViewById(R.id.orderDetailDescription)
        val price : TextView = view.findViewById(R.id.orderDetailSinglePrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_detail_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = infoList[position]
        Glide.with(holder.itemView)
            .load(urlHeader.plus(info.pic))
            .centerInside()
            .into(holder.image)

        holder.name.text = info.name
        holder.description.text = "数量：${info.count} 标签：${info.type}"

        holder.price.text = getNoMoreThanTwoDigits(info.price)
    }

    override fun getItemCount() = infoList.size
}