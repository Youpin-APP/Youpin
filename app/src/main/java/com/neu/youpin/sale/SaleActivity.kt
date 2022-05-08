package com.neu.youpin.sale

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.neu.youpin.R
import com.neu.youpin.entity.ServiceCreator
import com.neu.youpin.location.LocationDialog
import com.neu.youpin.login.SignToken
import com.neu.youpin.saleUpload.SaleUploadActivity
import com.neu.youpin.store.*
import kotlinx.android.synthetic.main.activity_loca_update.*
import kotlinx.android.synthetic.main.activity_sale.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*

class SaleActivity : AppCompatActivity() {
    private var builderForDialog: SelectClassDialog.Builder? = null
    private var locaDialog: SelectClassDialog? = null
    private var shopItemList:List<StoreMap>? = null
    private var shopDetailMap:ShopDetailMap? = null
    private var shopMainClassList:List<SortList>? = null

    private var tid = arrayOf(-1, -1, -1)
    private var tName = arrayOf("", "", "")
    private var gid = 2
    private var sid = -1

    private val saleService = ServiceCreator.create<SaleService>()

    //class id 为-1时代表需要新建！！！

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sale)

        builderForDialog = SelectClassDialog.Builder(this)
        initMainClassList()

        SaleButtonBack.setOnClickListener {
            finish()
        }

        SaleGoodTid1.setOnClickListener {
            locaDialog = builderForDialog!!.setListener(object: SelectClassDialog.SaleListener{
                override fun setActivityText(className: String, classId: Int){
                    SaleGoodTid1.text = className
                    tName[0] = className
                    tid[0] = classId
                }
            }).setTitle("商品一级细分类").setType(0, gid).createDialog()
            locaDialog!!.show()
        }

        SaleGoodTid2.setOnClickListener {
            locaDialog = builderForDialog!!.setListener(object: SelectClassDialog.SaleListener{
                override fun setActivityText(className: String, classId: Int){
                    SaleGoodTid2.text = className
                    tName[1] = className
                    tid[1] = classId
                }
            }).setTitle("商品二级细分类").setType(1, gid).createDialog()
            locaDialog!!.show()
        }

        SaleGoodTid3.setOnClickListener {
            locaDialog = builderForDialog!!.setListener(object: SelectClassDialog.SaleListener{
                override fun setActivityText(className: String, classId: Int){
                    SaleGoodTid3.text = className
                    tName[2] = className
                    tid[2] = classId
                }
            }).setTitle("商品三级细分类").setType(2, gid).createDialog()
            locaDialog!!.show()
        }

        SaleGoodSid.setOnClickListener {
//            val list: MutableList<TList> = mutableListOf<TList>()
//            for (i in shopItemList!!){
//                list.add(TList("名称：${i.name}  ￥${i.price}",i.id))
//            }
//            locaDialog = builderForDialog!!.setListener(object: SelectClassDialog.SaleListener{
//                override fun setActivityText(className: String, classId: Int){
//                    sid = classId
//                }
//            }).setTitle("商品主分类").createIdDialog()
//            locaDialog!!.show()
        }

        SaleButtonEditImg.setOnClickListener {
            if(gid>=0){
                val saleUploadIntent = Intent(this, SaleUploadActivity::class.java).apply {
                    putExtra("gid", SaleGoodPrimary.text.toString().toInt())
                }
                this.startActivity(saleUploadIntent)
            }else Toast.makeText(this,"请先选择商品再编辑图片",Toast.LENGTH_SHORT).show()
        }


        SaleButtonSearch.setOnClickListener {
            if(SaleGoodName.text.toString().isNotBlank()){
                initItemList(SaleGoodName.text.toString())
            }else doError("商品名为空不得查询")
        }

        SaleButtonAdd.setOnClickListener {
//            locaDialog = builderForDialog!!.setListener(object: SelectClassDialog.SaleListener{
//                override fun setActivityText(className: String, classId: Int){
//                    SaleGoodTid3.text = className
//                    tName[2] = className
//                    tid[2] = classId
//                }
//            }).setTitle("商品三级细分类").setType(2, gid).createDialog()
//            locaDialog!!.show()
        }
    }

    /**
     * {@inheritDoc}
     *
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are *not* resumed.
     */
    override fun onResume() {
        super.onResume()
        if(gid > 0){
            initItemInfo()
        }else doError("商品信息查询失败")
    }

    private fun doError(text: String){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
    }

    private fun initItemList(gName: String) {
        saleService.search(gName).enqueue(object : Callback<List<StoreMap>> {
            override fun onResponse(call: Call<List<StoreMap>>,
                                    response: Response<List<StoreMap>>
            ) {
                shopItemList = response.body()
                showGidDialog()
            }
            override fun onFailure(call: Call<List<StoreMap>>, t: Throwable) {
                t.printStackTrace()
                Log.d("LoginActivity", "network failed")
            }
        })
    }

    private fun showGidDialog(){
        val list: MutableList<TList> = mutableListOf<TList>()
        for (i in shopItemList!!){
            list.add(TList("名称：${i.name}  ￥${i.price}",i.id))
        }
        locaDialog = builderForDialog!!.setListener(object: SelectClassDialog.SaleListener{
            override fun setActivityText(className: String, classId: Int){
                gid = classId
                initItemInfo()
            }
        }).setTitle("选择商品").createIdDialog(list)
        locaDialog!!.show()
    }

    private fun initItemInfoSuccess(){
        SaleGoodPrimary.setText(gid.toString())
        SaleGoodName.setText(shopDetailMap?.content?.name)
        SaleGoodTid1.text = shopDetailMap?.type1?.name
        SaleGoodTid2.text = shopDetailMap?.type2?.name
        SaleGoodTid3.text = shopDetailMap?.type3?.name
        tid[0] = shopDetailMap?.type1?.id!!
        tid[1] = shopDetailMap?.type2?.id!!
        tid[2] = shopDetailMap?.type3?.id!!
        SaleGoodPrice.setText(shopDetailMap?.content?.price.toString())
        SaleGoodSid.text = shopDetailMap?.sid?.let { shopMainClassList?.get(it)?.sname }
    }

    private fun initItemInfo(){
        saleService.getInfo(gid).enqueue(object : Callback<ShopDetailMap> {
            override fun onResponse(call: Call<ShopDetailMap>,
                                    response: Response<ShopDetailMap>
            ) {
                shopDetailMap = response.body()
                if (shopDetailMap != null && shopDetailMap?.success == true) {
                    initItemInfoSuccess()
                }else doError("查询商品详情失败！！")
            }
            override fun onFailure(call: Call<ShopDetailMap>, t: Throwable) {
                t.printStackTrace()
                Log.d("ShopDetailActivity", "network failed")
            }
        })
    }

    private fun initMainClassList(){
        saleService.sortList().enqueue(object : Callback<List<SortList>> {
            override fun onResponse(call: Call<List<SortList>>,
                                    response: Response<List<SortList>>
            ) {
                shopMainClassList = response.body()
            }
            override fun onFailure(call: Call<List<SortList>>, t: Throwable) {
                t.printStackTrace()
                Log.d("SaleActivity", "network failed")
            }
        })
    }

}

data class SortList(val sid:Int, val sname: String)

interface SaleService {
    @GET("/goods/getInfo")
    fun getInfo(@Query("id") name: Int): Call<ShopDetailMap>

    @GET("/store/search")
    fun search(@Query("name") name: String): Call<List<StoreMap>>

    @FormUrlEncoded
    @POST("/goodsEdit/addGoods")
    fun addGoods(@Field("uid") uid: String, @Field("pw") pw: String, @Field("name") name: String): Call<SignToken>

    @GET("/goods/sortList")
    fun sortList(): Call<List<SortList>>
}