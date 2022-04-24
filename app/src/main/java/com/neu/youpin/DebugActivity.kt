package com.neu.youpin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.neu.youpin.cart.CartActivity
import com.neu.youpin.store.StoreActivity

class DebugActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug)
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
        val openCart = findViewById<Button>(R.id.openCart)
        openCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
    }
}