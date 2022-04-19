package com.neu.youpin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class DebugActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug)
        val openStore = findViewById<Button>(R.id.openStore)
        openStore.setOnClickListener(View.OnClickListener {
            val intent = Intent(this,StoreActivity::class.java)
            startActivity(intent)
        })
    }
}