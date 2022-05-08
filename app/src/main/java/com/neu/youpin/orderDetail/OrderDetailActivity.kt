package com.neu.youpin.orderDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.BoringLayout
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neu.youpin.R
import com.neu.youpin.entity.ServiceCreator
import kotlinx.android.synthetic.main.activity_order_detail.*
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class OrderDetailActivity : AppCompatActivity() {
    private var orderDetailInfo : OrderDetailInfo? = null
    private val orderDetailService = ServiceCreator.create<OrderDetailService>()
    private var oid = 9
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        oid = intent.getIntExtra("oid", 0)
        initList()
        val layoutManager = LinearLayoutManager(this)
        val recyclerView = findViewById<RecyclerView>(R.id.orderDetailList)
        recyclerView.layoutManager = layoutManager
        val adapter = OrderDetailListAdapter(null)
        recyclerView.adapter = adapter
        orderDetailBack.setOnClickListener {
            finish()
        }

        orderPayButton.setOnClickListener {
            if(orderDetailInfo?.basic?.state == 0){
                orderDetailService.pay(oid).enqueue(object : retrofit2.Callback<OrderOpStatus> {
                    override fun onResponse(call: Call<OrderOpStatus>, response: Response<OrderOpStatus>) {
                        if(response.body() != null) {
                            if(response.isSuccessful) {
                                Toast.makeText(baseContext,"支付成功",Toast.LENGTH_LONG).show()
                                Thread.sleep(1000)
                                finish();
                                startActivity(getIntent());
                            }
                        }
                    }

                    override fun onFailure(call: Call<OrderOpStatus>, t: Throwable) {
                        t.printStackTrace()
                        Log.d("LoginActivity", "network failed")
                    }
                })
            }
            else if(orderDetailInfo?.basic?.state == 1) {
                orderDetailService.finish(oid).enqueue(object : retrofit2.Callback<OrderOpStatus> {
                    override fun onResponse(call: Call<OrderOpStatus>, response: Response<OrderOpStatus>) {
                        if(response.body() != null) {
                            if(response.isSuccessful) {
                                Toast.makeText(baseContext,"订单完成",Toast.LENGTH_LONG).show()
                                finish();
                                startActivity(getIntent());
                            }
                        }
                    }

                    override fun onFailure(call: Call<OrderOpStatus>, t: Throwable) {
                        t.printStackTrace()
                        Log.d("LoginActivity", "network failed")
                    }
                })
            }
        }

        orderCancelButton.setOnClickListener {
            orderDetailService.cancel(oid).enqueue(object : retrofit2.Callback<OrderOpStatus> {
                override fun onResponse(call: Call<OrderOpStatus>, response: Response<OrderOpStatus>) {
                    if(response.body() != null) {
                        if(response.isSuccessful) {
                            Toast.makeText(baseContext,"订单取消",Toast.LENGTH_LONG).show()
                            finish();
                            startActivity(getIntent())
                        }
                    }
                }

                override fun onFailure(call: Call<OrderOpStatus>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("LoginActivity", "network failed")
                }
            })
        }
    }

    private fun initList() {
        orderDetailService.getDetail(oid).enqueue(object : retrofit2.Callback<OrderDetailInfo> {
            override fun onResponse(call: Call<OrderDetailInfo>, response: Response<OrderDetailInfo>) {
                if(response.body() != null) {
                    orderDetailInfo = response.body()
                    (findViewById<RecyclerView>(R.id.orderDetailList).adapter as OrderDetailListAdapter).setData(orderDetailInfo!!.infos)
                    findViewById<TextView>(R.id.orderDetailPrice).text = orderDetailInfo?.totalPrice.toString()
                    findViewById<TextView>(R.id.orderId).text = orderDetailInfo?.basic?.oid.toString()
                    if(orderDetailInfo?.basic?.state == 2) {
                        findViewById<TextView>(R.id.orderTime2Title).text = "取消世间: "
                        findViewById<Button>(R.id.orderPayButton).visibility = View.GONE
                    }
                    if(orderDetailInfo?.basic?.state == 0) {
                        findViewById<Button>(R.id.orderCancelButton ).visibility = View.VISIBLE
                    }
                    if(orderDetailInfo?.basic?.state == 1) {
                        findViewById<TextView>(R.id.orderPayButton ).text = "确认收货"
                    }
                    if(orderDetailInfo?.basic?.state == 3) {
                        findViewById<Button>(R.id.orderPayButton).visibility = View.GONE
                    }
                    findViewById<TextView>(R.id.orderDetailStatus).text = orderDetailInfo?.basic?.stateName
                    if(orderDetailInfo?.basic?.otime1 != null) {
                        findViewById<TextView>(R.id.orderTime1).text = orderDetailInfo?.basic?.otime1
                    }
                    if(orderDetailInfo?.basic?.otime2 != null) {
                        findViewById<TextView>(R.id.orderTime2).text = orderDetailInfo?.basic?.otime2
                    }
                    if(orderDetailInfo?.basic?.otime3 != null) {
                        findViewById<TextView>(R.id.orderTime3).text = orderDetailInfo?.basic?.otime3
                    }
                }
            }

            override fun onFailure(call: Call<OrderDetailInfo>, t: Throwable) {
                t.printStackTrace()
                Log.d("LoginActivity", "network failed")
            }
        })
    }
}

data class OrderOpStatus(val success : Boolean)

interface OrderDetailService {
    @FormUrlEncoded
    @POST("order/getOrderDetail")
    fun getDetail(@Field("oid") oid : Int): Call<OrderDetailInfo>

    @FormUrlEncoded
    @POST("order/payOrder")
    fun pay(@Field("oid") oid : Int): Call<OrderOpStatus>

    @FormUrlEncoded
    @POST("order/cancelOrder")
    fun cancel(@Field("oid") oid : Int): Call<OrderOpStatus>

    @FormUrlEncoded
    @POST("order/finishOrder")
    fun finish(@Field("oid") oid : Int): Call<OrderOpStatus>
}