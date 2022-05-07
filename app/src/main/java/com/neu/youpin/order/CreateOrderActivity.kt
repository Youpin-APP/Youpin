package com.neu.youpin.order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neu.youpin.R
import com.neu.youpin.entity.UserApplication
import com.neu.youpin.location.Loca
import com.neu.youpin.location.LocaListActivity
import com.neu.youpin.orderDetail.OrderDetailInfo
import com.neu.youpin.orderDetail.OrderDetailListAdapter
import com.neu.youpin.store.StoreActivity
import kotlinx.android.synthetic.main.activity_create_order.*
import kotlinx.android.synthetic.main.activity_shop_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class CreateOrderActivity : AppCompatActivity() {
    private val createOrderInfoList = Vector<OrderDetailInfo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_order)
        initList()
        val layoutManager = LinearLayoutManager(this)
        val recyclerView = findViewById<RecyclerView>(R.id.CreateOrderList)
        recyclerView.layoutManager = layoutManager
        val adapter = OrderDetailListAdapter(createOrderInfoList)
        recyclerView.adapter = adapter

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

//    override fun onResume() {
//        super.onResume()
//        if(UserApplication.getInstance().getAid()<0){
//            UserApplication.getInstance().getId()?.let {
//                locaListService.getAddrList(it).enqueue(object : Callback<List<Loca>> {
//                    override fun onResponse(call: Call<List<Loca>>,
//                                            response: Response<List<Loca>>
//                    ) {
//                        val userLocaList = response.body()
//                        var defaultIndex = 0
//                        for((i, item) in userLocaList!!.withIndex()){
//                            if(item.default == 1){
//                                defaultIndex = i
//                                break
//                            }
//                        }
//                        UserApplication.getInstance().setAid(userLocaList[defaultIndex].aid)
//                        initUserAdd(userLocaList[defaultIndex])
//                    }
//
//                    override fun onFailure(call: Call<List<Loca>>, t: Throwable) {
//                        t.printStackTrace()
//                        Log.d("ShopDetailActivity", "network failed")
//                    }
//                })
//            }
//        }else{
//            locaListService.getAddr(UserApplication.getInstance().getAid()).enqueue(object :
//                Callback<Loca> {
//                override fun onResponse(call: Call<Loca>,
//                                        response: Response<Loca>
//                ) {
//                    val loca = response.body()
//                    if(loca!=null && loca.success){
//                        initUserAdd(loca)
//                    }
//                }
//
//                override fun onFailure(call: Call<Loca>, t: Throwable) {
//                    t.printStackTrace()
//                    Log.d("ShopDetailActivity", "network failed")
//                }
//            })
//        }
//    }

    private fun initList() {
        repeat(10) {
            createOrderInfoList.add(
                OrderDetailInfo(
                    "furry", 11415,
                    "数量 1 颜色: 蓝色", R.drawable.item_img
                )
            )
        }
    }
}