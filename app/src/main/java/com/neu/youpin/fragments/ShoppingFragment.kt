package com.neu.youpin.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.neu.youpin.HomePageActivity
import com.neu.youpin.Interface.OnItemClickListener
import com.neu.youpin.R
import com.neu.youpin.cart.CartItem
import com.neu.youpin.cart.CartListAdapter
import com.neu.youpin.entity.ServiceCreator
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShoppingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShoppingFragment : Fragment() , OnItemClickListener, com.neu.youpin.cart.OnItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var root: View? = null
    private var rootActivity: HomePageActivity? = null
    private val cartItemList = Vector<CartItem>()
    private lateinit var adapter : CartListAdapter
    private var isEditing = false
    private var totalPrice = 0f
    private lateinit var cartTotalPrice : TextView
    private val cartService = ServiceCreator.create<CartService>()
    private val uid = "11415"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_shopping, container, false)
        if (activity != null) {
            rootActivity = activity as HomePageActivity
        }

        initItem()
        val layoutManager = LinearLayoutManager(rootActivity)
        val recyclerView = root?.findViewById<RecyclerView>(R.id.cartItemList)
        recyclerView?.layoutManager = layoutManager
        adapter = CartListAdapter(cartItemList)
        recyclerView?.adapter = adapter
        adapter.setOnItemClickListener(this)
        //禁用RecyclerView 刷新动画
        (recyclerView?.itemAnimator as SimpleItemAnimator?)!!.supportsChangeAnimations = false
        cartTotalPrice = root?.findViewById(R.id.cartTotalPrice)!!
        val cartItemSelectAll = root?.findViewById<CheckBox>(R.id.selectAllCart)
        val editCart = root?.findViewById<Button>(R.id.editCart)
        val cartCheckout = root?.findViewById<Button>(R.id.cartCheckoutButton)

        cartItemSelectAll?.setOnClickListener {
            Log.d("selectAll", "click" + cartItemSelectAll.isChecked)
            totalPrice = 0f
            if(cartItemSelectAll.isChecked) {
                for (cartItem in cartItemList) {
                    cartItem.selected = 1
                    totalPrice += cartItem.price * cartItem.count
                }
                adapter.notifyItemRangeChanged(0,adapter.itemCount)
            }
            else {
                for (cartItem in cartItemList) {
                    cartItem.selected = 0
                }
                adapter.notifyItemRangeChanged(0,adapter.itemCount)
            }
            updateTotalPrice()
        }
        editCart?.setOnClickListener {
            isEditing = !isEditing
            if(isEditing) {
                editCart.text = "完成"
                cartCheckout?.text = "删除"
            }
            else {
                editCart.text = "编辑"
                cartCheckout?.text = "结算"
            }
        }
        cartCheckout?.setOnClickListener {
            if(isEditing){
                cartItemList.removeIf { it.selected == 1 }
                adapter.notifyDataSetChanged()
                totalPrice = 0f
                for (cartItem in cartItemList) {
                    totalPrice += cartItem.price * cartItem.count
                }
                updateTotalPrice()
            }
            else {

            }
        }

        return root
    }

    private fun initItem() {
        cartService.getList(uid).enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                cartItemList.clear()
                val jsonStr = String(response.body()!!.bytes())
                val obj = Gson().fromJson(jsonStr, JsonArray::class.java)
                val bannerArray = obj.asJsonArray
                for (jsonElement in bannerArray) {
                    val cartObj = jsonElement.asJsonObject
                    cartItemList.add(CartItem(cartObj.get("name").asString,
                        "http://hqyz.cf:8080/pic/" + cartObj.get("pic").asString,
                        cartObj.get("price").asFloat,cartObj.get("count").asInt,
                        cartObj.get("type").asString,cartObj.get("selected").asInt,
                        cartObj.get("gid").asInt,cartObj.get("caid").asInt))
                    val recyclerViewBanner = view!!.findViewById<RecyclerView>(R.id.cartItemList)
                    recyclerViewBanner.adapter!!.notifyDataSetChanged()
                    updateTotalPrice()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
                Log.d("LoginActivity", "network failed")
            }
        })
    }

    @SuppressLint("SetTextI18n")
    override fun onItemClick(pos: Int, id : Int) {
        when(id) {
            R.id.cartItemPlus -> {
                cartService.itemAddOne(cartItemList[pos].caid).enqueue(object : retrofit2.Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        val jsonStr = String(response.body()!!.bytes())
                        val obj = Gson().fromJson(jsonStr, JsonObject::class.java)
                        if(obj.get("success").asBoolean) {
                            cartItemList[pos].count = obj.get("count").asInt
                            adapter.notifyItemChanged(pos)
                            updateTotalPrice()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        t.printStackTrace()
                        Log.d("LoginActivity", "network failed")
                    }
                })
            }
            R.id.cartItemSub -> {
                cartService.itemMinusOne(cartItemList[pos].caid).enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    val jsonStr = String(response.body()!!.bytes())
                    val obj = Gson().fromJson(jsonStr, JsonObject::class.java)
                    if(obj.get("success").asBoolean) {
                        cartItemList[pos].count = obj.get("count").asInt
                        adapter.notifyItemChanged(pos)
                        updateTotalPrice()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("LoginActivity", "network failed")
                }
            })
            }
            R.id.cartItemCheckBox -> {
                Log.d("checkItem","")
                cartItemList[pos].selected = 1-cartItemList[pos].selected
                cartService.selectItem(cartItemList[pos].caid, cartItemList[pos].selected).enqueue(
                    object : retrofit2.Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        t.printStackTrace()
                        Log.d("LoginActivity", "network failed")
                    }
                })
                val cartItemSelectAll = view?.findViewById<CheckBox>(R.id.selectAllCart)
                if(cartItemList[pos].selected == 0 && cartItemSelectAll!!.isChecked) {
                    cartItemSelectAll.isChecked = false
                }
                if(cartItemList[pos].selected == 1) {
                    var selectAll = true
                    for (cartItem in cartItemList) {
                        if(cartItem.selected == 0) {
                            selectAll = false
                            break
                        }
                    }
                    if(selectAll) {
                        cartItemSelectAll?.isChecked = true
                    }
                }
                updateTotalPrice()
            }
        }
    }

    fun updateTotalPrice() {
        totalPrice = 0f
        for (cartItem in cartItemList) {
            if(cartItem.selected == 1) {
                totalPrice += cartItem.price * cartItem.count
            }
            cartTotalPrice.text = "合计: "+String.format("%.2f", totalPrice)+"元"

        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ShoppingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShoppingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

interface CartService {
    @FormUrlEncoded
    @POST("cart/getList")
    fun getList(@Field("uid") uid : String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("cart/cartItemAddOne")
    fun itemAddOne(@Field("caid") caid : Int): Call<ResponseBody>

    @FormUrlEncoded
    @POST("cart/cartItemMinusOne")
    fun itemMinusOne(@Field("caid") caid : Int): Call<ResponseBody>

    @FormUrlEncoded
    @POST("cart/cartItemDelete")
    fun itemDelete(@Field("caids") caids : List<Int>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("cart/selectItem")
    fun selectItem(@Field("caid") caid : Int, @Field("selected") selected : Int): Call<ResponseBody>
}