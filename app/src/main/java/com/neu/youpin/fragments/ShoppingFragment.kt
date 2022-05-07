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
import com.neu.youpin.HomePageActivity
import com.neu.youpin.R
import com.neu.youpin.cart.CartItem
import com.neu.youpin.cart.CartListAdapter
import com.neu.youpin.cart.OnItemClickListener
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
class ShoppingFragment : Fragment() , OnItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var root: View? = null
    private var rootActivity: HomePageActivity? = null
    private val cartItemList = Vector<CartItem>()
    private lateinit var adapter : CartListAdapter
    private var isEditing = false
    private var totalPrice = 0
    private lateinit var cartTotalPrice : TextView

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
            totalPrice = 0
            if(cartItemSelectAll.isChecked) {
                for (cartItem in cartItemList) {
                    cartItem.selected = true
                    totalPrice += cartItem.price * cartItem.count
                }
                adapter.notifyItemRangeChanged(0,adapter.itemCount)
            }
            else {
                for (cartItem in cartItemList) {
                    cartItem.selected = false
                }
                adapter.notifyItemRangeChanged(0,adapter.itemCount)
            }
            cartTotalPrice.text = "合计: "+totalPrice.toString()+"元"
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
                cartItemList.removeIf { it.selected }
                adapter.notifyDataSetChanged()
                totalPrice = 0
                for (cartItem in cartItemList) {
                    totalPrice += cartItem.price * cartItem.count
                }
                cartTotalPrice.text = "合计: "+totalPrice.toString()+"元"
            }
        }

        return root
    }

    private fun initItem() {
        repeat(10) {
            cartItemList.add(
                CartItem("furry", R.drawable.item_img,11415,
                20,"蓝白")
            )
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onItemClick(pos: Int, id : Int) {
        when(id) {
            R.id.cartItemPlus -> {
                cartItemList[pos].count++
                adapter.notifyItemChanged(pos)
                if(cartItemList[pos].selected){
                    totalPrice += cartItemList[pos].price
                    cartTotalPrice.text = "合计: "+totalPrice.toString()+"元"
                }

            }
            R.id.cartItemSub -> {
                cartItemList[pos].count--
                adapter.notifyItemChanged(pos)
                if(cartItemList[pos].selected){
                    totalPrice -= cartItemList[pos].price
                    cartTotalPrice.text = "合计: "+totalPrice.toString()+"元"
                }
            }
            R.id.cartItemCheckBox -> {
                Log.d("checkItem","")
                cartItemList[pos].selected = !cartItemList[pos].selected
                val cartItemSelectAll = view?.findViewById<CheckBox>(R.id.selectAllCart)
                if(!cartItemList[pos].selected && cartItemSelectAll!!.isChecked) {
                    cartItemSelectAll.isChecked = false
                }
                if(cartItemList[pos].selected) {
                    var selectAll = true
                    for (cartItem in cartItemList) {
                        if(!cartItem.selected) {
                            selectAll = false
                            break
                        }
                    }
                    if(selectAll) {
                        cartItemSelectAll?.isChecked = true
                    }
                }
                if(cartItemList[pos].selected) {
                    totalPrice += cartItemList[pos].price * cartItemList[pos].count
                }
                else {
                    totalPrice -= cartItemList[pos].price * cartItemList[pos].count
                }
                cartTotalPrice.text = "合计: "+totalPrice.toString()+"元"
            }
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