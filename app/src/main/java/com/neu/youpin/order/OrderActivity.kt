package com.neu.youpin.order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neu.youpin.Interface.OnItemClickListener
import com.neu.youpin.R
import com.neu.youpin.entity.ServiceCreator
import com.neu.youpin.entity.UserApplication
import com.neu.youpin.orderDetail.OrderDetailActivity
import kotlinx.android.synthetic.main.activity_order.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.*

class OrderActivity : AppCompatActivity() , OnItemClickListener{
    private val orderInfoList = ArrayList<OrderInfo>()
    private var uid = "11415"
    private val orderListService = ServiceCreator.create<OrderListService>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        //TODO:这里设置uid

        if(UserApplication.getInstance().isLogin()) uid = UserApplication.getInstance().getId().toString()
        else finish()

        initList()
        val orderListView = findViewById<RecyclerView>(R.id.orderList)
        orderListView.layoutManager = LinearLayoutManager(this)
        val adapter = OrderListAdapter(orderInfoList)
        adapter.setOnItemClickListener(this)
        orderListView.adapter = adapter
        orderBack.setOnClickListener {
            finish()
        }
    }

    private fun initList() {
        orderListService.getList(uid).enqueue(object : Callback<List<OrderInfo>> {
            override fun onResponse(call: Call<List<OrderInfo>>, response: Response<List<OrderInfo>>) {
                val list = response.body()
                orderInfoList.clear()
                if (list != null) {
                    orderInfoList.addAll(list)
                }
                var orderListView : RecyclerView? = findViewById(R.id.orderList)
                if(orderListView != null && orderListView.adapter != null) {
                    orderListView.adapter!!.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<OrderInfo>>, t: Throwable) {
                t.printStackTrace()
                Log.d("LoginActivity", "network failed")
            }
        })
    }

    override fun onItemClick(pos: Int, id: Int) {
        when(id) {
            R.id.orderItem -> {
                val intent = Intent(this, OrderDetailActivity::class.java)
                intent.putExtra("oid", orderInfoList[pos].oid)
                startActivity(intent)
            }
        }
    }

}

interface OrderListService {
    @FormUrlEncoded
    @POST("order/getOrderList")
    fun getList(@Field("uid") uid : String): Call<List<OrderInfo>>
}