package com.neu.youpin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.neu.youpin.cart.CartActivity
import com.neu.youpin.login.LoginActivity
import com.neu.youpin.order.CreateOrderActivity
import com.neu.youpin.order.OrderActivity
import com.neu.youpin.orderDetail.OrderDetailActivity
import com.neu.youpin.orderDetail.OrderDetailInfo
import com.neu.youpin.sale.SaleActivity
import com.neu.youpin.store.ShopDetailActivity
import com.neu.youpin.store.StoreActivity
import com.neu.youpin.user.UserActivity

class DebugActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug)
        val openSale = findViewById<Button>(R.id.openSale)
        openSale.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, SaleActivity::class.java)
            startActivity(intent)
        })
        val openStore = findViewById<Button>(R.id.openStore)
        openStore.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, StoreActivity::class.java)
            startActivity(intent)
        })
        val openHomePage = findViewById<Button>(R.id.openHomePage)
        openHomePage.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }
        val openShopDetail = findViewById<Button>(R.id.openShopDetail)
        openShopDetail.setOnClickListener {
            val intent = Intent(this, ShopDetailActivity::class.java)
            startActivity(intent)
        }
        val openOrder = findViewById<Button>(R.id.openOrder)
        openOrder.setOnClickListener {
            val intent = Intent(this, OrderActivity::class.java)
            startActivity(intent)
        }
        val openCreateOrder = findViewById<Button>(R.id.openCreateOrder)
        openCreateOrder.setOnClickListener {
            val intent = Intent(this, CreateOrderActivity::class.java)
            startActivity(intent)
        }
        val openOrderDetail = findViewById<Button>(R.id.openOrderDetail)
        openOrderDetail.setOnClickListener {
            val intent = Intent(this, OrderDetailActivity::class.java)
            startActivity(intent)
        }
    }
}