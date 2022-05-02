package com.neu.youpin.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.neu.youpin.HomePageActivity
import com.neu.youpin.R
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.listener.OnBannerListener
import java.text.FieldPosition

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var NowFragment: String = "HomeFragment"
    private var root: View? = null
    private var rootActivity:HomePageActivity? = null
    private var tabLayout: TabLayout? = null

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

        root = inflater.inflate(R.layout.fragment_home, container, false)
        if (activity != null) {
            rootActivity = activity as HomePageActivity
        }
        val homeSearchBox: EditText? = root?.findViewById(R.id.HomeSearchBox)
        val homeSearchButton: ImageButton? = root?.findViewById(R.id.HomeSearchButton)
        homeSearchButton?.setOnClickListener {
            param2 = homeSearchBox?.text.toString()
        }

        tabLayout = root?.findViewById(R.id.tab_layout)

        val tabItemList = arrayOf("热门", "女装", "男装", "美妆", "内衣配饰",
            "个护", "饮食", "家纺", "生鲜直供", "鞋靴", "餐厨", "电器")

        for(element in tabItemList)
            tabLayout?.addTab(tabLayout?.newTab()!!.setText(element));

        var banner: Banner<DataBean, BannerImageAdapter<DataBean>>? = root?.findViewById(R.id.banner)

        banner?.setAdapter(object : BannerImageAdapter<DataBean>(DataBean.testData) {
            override fun onBindView(holder: BannerImageHolder, data: DataBean, position: Int, size: Int) {
                //图片加载自己实现
                Glide.with(holder.itemView)
                    .load(data.imageUrl)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                    .into(holder.imageView) }
        })?.addBannerLifecycleObserver(this)?.indicator = CircleIndicator(rootActivity)

        banner?.setOnBannerListener(OnBannerListener {
                data: DataBean?, position: Int ->
            Log.i("HomeFragment", "第"+position+"张图片")
        })

        return root
    }

    class DataBean {
        var imageRes: Int? = null
        var imageUrl: String? = null
        var title: String?
        var viewType: Int

        constructor(imageRes: Int?, title: String?, viewType: Int) {
            this.imageRes = imageRes
            this.title = title
            this.viewType = viewType
        }

        constructor(imageUrl: String?, title: String?, viewType: Int) {
            this.imageUrl = imageUrl
            this.title = title
            this.viewType = viewType
        }

        companion object {
            //测试数据，如果图片链接失效请更换
            val testData: List<DataBean>
                get() {
                    val list: MutableList<DataBean> = ArrayList()
                    list.add(
                        DataBean(
                            "https://img.zcool.cn/community/013de756fb63036ac7257948747896.jpg",
                            null,
                            1
                        )
                    )
                    list.add(
                        DataBean(
                            "https://img.zcool.cn/community/01639a56fb62ff6ac725794891960d.jpg",
                            null,
                            1
                        )
                    )
                    list.add(
                        DataBean(
                            "https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg",
                            null,
                            1
                        )
                    )
                    list.add(
                        DataBean(
                            "https://img.zcool.cn/community/01233056fb62fe32f875a9447400e1.jpg",
                            null,
                            1
                        )
                    )
                    list.add(
                        DataBean(
                            "https://img.zcool.cn/community/016a2256fb63006ac7257948f83349.jpg",
                            null,
                            1
                        )
                    )
                    return list
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
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}