package com.neu.youpin.order

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
import java.util.*

class CreateOrderActivity : AppCompatActivity() {
    private val createOrderInfoList = Vector<OrderDetailInfo>()
    private val locaListService = ServiceCreator.create<LocaListService>()
    private var oid = -1

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
                        initUserAdd(userLocaList[defaultIndex])
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
                        initUserAdd(loca)
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
        val layoutManager = LinearLayoutManager(this)
        val recyclerView = findViewById<RecyclerView>(R.id.CreateOrderList)
        recyclerView.layoutManager = layoutManager
        val adapter = OrderDetailListAdapter(createOrderInfoList)
        recyclerView.adapter = adapter
    }

    private fun getByRetrofit(){
        createOrderService.getOrderDetail(oid).enqueue(object : Callback<OrderDetail> {
            override fun onResponse(call: Call<OrderDetail>,
                                    response: Response<OrderDetail>
            ) {
                val body = response.body()
                if (body != null && body.success) {
                    init()
                }else doError("查询订单状态失败")
            }

            override fun onFailure(call: Call<OrderDetail>, t: Throwable) {
                t.printStackTrace()
                Log.d("CreateOrderActivity", "network failed")
            }
        })
    }

}

data class Deliver(val otel:String, val name:String, val aid:Int, val addr:String)

data class Basic(val oid: Int, val stateName:String, val state:Int, val otime1: String)

data class Info(val pic:String, val type: String, val gid:Int, val price:Float, val count: Int)

data class OrderDetail(val totalPrice: Float, val basic: Basic, val success:Boolean,
                        val infos:Info,val deliver: Deliver)

interface CreateOrderService {
    @FormUrlEncoded
    @POST("/order/getOrderDetail")
    fun getOrderDetail(@Field("oid") oid: Int): Call<OrderDetail>
}

private class OrderDetailAdapter(private var infoList: Vector<OrderDetailInfo>) :

    RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>() {
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = infoList[position]
        holder.image.setImageResource(info.pic)
        holder.name.text = info.name
        holder.description.text = info.description
        holder.price.text = info.price.toString()
    }

    override fun getItemCount() = infoList.size
}