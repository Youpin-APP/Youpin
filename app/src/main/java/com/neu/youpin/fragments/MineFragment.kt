package com.neu.youpin.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.neu.youpin.HomePageActivity
import com.neu.youpin.R
import com.neu.youpin.store.ShopItem
import com.neu.youpin.user.UserItemListAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MineFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MineFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var NowFragment: String = "MineFragment"
    private var root: View? = null
    private var rootActivity: HomePageActivity? = null

    private val shopItemList = ArrayList<ShopItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        Log.d(NowFragment,"create!")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_mine, container, false)
        if (activity != null) {
            rootActivity = activity as HomePageActivity
        }
        initItem()
        val layoutManager : RecyclerView.LayoutManager = object : GridLayoutManager(rootActivity,2) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        val recyclerView = root?.findViewById<RecyclerView>(R.id.userItemList)
        recyclerView?.layoutManager = layoutManager
        val adapter = UserItemListAdapter(shopItemList)
        recyclerView?.adapter = adapter
        return root
    }

    private fun initItem() {
        repeat(10) {
            shopItemList.add(ShopItem("furry", R.drawable.item_img,"11415元"))
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MineFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MineFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}