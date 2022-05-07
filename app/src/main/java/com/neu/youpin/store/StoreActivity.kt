package com.neu.youpin.store

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neu.youpin.R
import com.neu.youpin.entity.ServiceCreator
import kotlinx.android.synthetic.main.activity_store.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*


class StoreActivity : AppCompatActivity() {
    private var shopItemList:List<StoreMap>? = null
    private var shopItemName: String = ""
    private val storeService = ServiceCreator.create<StoreService>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        shopItemName = intent.getStringExtra("gName").toString()
        StoreSearchBox.setText(shopItemName)

        initItem(shopItemName)

        StoreButtonBack.setOnClickListener {
            finish()
        }

        StoreButtonSearch.setOnClickListener {
            initItem(StoreSearchBox.text.toString())
        }

        StoreSearchBox.doOnTextChanged { text, start, before, count ->
            if(StoreSearchBox.text.toString().isEmpty()){
                StoreButtonClear.visibility = View.GONE
            }else StoreButtonClear.visibility = View.VISIBLE
        }

        StoreSearchBox.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                StoreSearchBox.setText(shopItemName)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        StoreButtonClear.setOnClickListener {
            StoreSearchBox.setText("")
        }
    }

    private fun initRecycleView(){
        val layoutManager = LinearLayoutManager(this)
        val recyclerView = findViewById<RecyclerView>(R.id.StoreItemList)
        recyclerView.layoutManager = layoutManager
        val adapter = shopItemList?.let { StoreListAdapter(it, this) }
        recyclerView.adapter = adapter
    }

    private fun initItem(gName: String) {
        storeService.search(gName).enqueue(object : Callback<List<StoreMap>> {
            override fun onResponse(call: Call<List<StoreMap>>,
                                    response: Response<List<StoreMap>>
            ) {
                shopItemList = response.body()
                initRecycleView()
            }
            override fun onFailure(call: Call<List<StoreMap>>, t: Throwable) {
                t.printStackTrace()
                Log.d("LoginActivity", "network failed")
            }
        })
    }
}

data class StoreMap(val name: String, val id: Int, val price: Float, val picUrl: String)

interface StoreService {
    @GET("store/search")
    fun search(@Query("name") name: String): Call<List<StoreMap>>
}