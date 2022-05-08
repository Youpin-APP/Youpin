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

    private var tid = arrayOf(-1, -1, -1)
    private var tName = arrayOf("", "", "")
    private var gid = 2
    private var sid = -1
    private var sName:String? = null

    private val saleService = ServiceCreator.create<SaleService>()

    //class id 为-1时代表需要新建！！！

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sale)

        builderForDialog = SelectClassDialog.Builder(this)

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
            locaDialog = builderForDialog!!.setListener(object: SelectClassDialog.SaleListener{
                override fun setActivityText(className: String, classId: Int){
                    SaleGoodSid.text = className
                    sName = className
                    sid = classId
                }
            }).setTitle("商品主分类").createIdDialog(null)
            locaDialog!!.show()
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
            if(tid[0]!=-1 && tid[1]!=-1 && tid[2]!=-1 && sid!=-1 && SaleGoodName.text.toString().isNotBlank()
                && SaleGoodPrice.text.toString().isNotBlank()){
                addItemInfo()
            }else doError("商品信息填写错误")
        }

        SaleButtonEdit.setOnClickListener {
            if(tid[0]!=-1 && tid[1]!=-1 && tid[2]!=-1 && sid!=-1 && SaleGoodName.text.toString().isNotBlank()
                && sid!=-1 && SaleGoodPrice.text.toString().isNotBlank()){
                editItemInfo()
            }else doError("商品信息填写错误")
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
        SaleGoodSid.text = shopDetailMap?.sname
        sid = shopDetailMap?.sid!!
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

    private fun addItemInfoSuccess(){
        SaleGoodPrimary.setText(gid.toString())
    }

    private fun addItemInfo(){
        saleService.addGoods(SaleGoodName.text.toString(), sid, tid[0], tid[1],
            tid[2], SaleGoodPrice.text.toString().toFloat()).enqueue(object : Callback<SaleMap> {
            override fun onResponse(call: Call<SaleMap>,
                                    response: Response<SaleMap>
            ) {
                val body = response.body()
                if (body != null) {
                    gid = body.gid
                    addItemInfoSuccess()
                }else doError("商品新增失败！！")
            }
            override fun onFailure(call: Call<SaleMap>, t: Throwable) {
                t.printStackTrace()
                Log.d("ShopDetailActivity", "network failed")
            }
        })
    }

    private fun editItemInfo(){
        saleService.editGoods(gid, SaleGoodName.text.toString(), sid, SaleGoodPrice.text.toString().toFloat(),
            tid[0], tid[1], tid[2]).enqueue(object : Callback<SaleStatus> {
            override fun onResponse(call: Call<SaleStatus>,
                                    response: Response<SaleStatus>
            ) {
                val body = response.body()
                if (body != null) {
                    doError("商品修改成功")
                }else doError("商品修改失败")
            }
            override fun onFailure(call: Call<SaleStatus>, t: Throwable) {
                t.printStackTrace()
                Log.d("ShopDetailActivity", "network failed")
            }
        })
    }

}

class SaleMap(val gid:Int)

class SaleStatus(val success:Boolean)

interface SaleService {
    @GET("/goods/getInfo")
    fun getInfo(@Query("id") name: Int): Call<ShopDetailMap>

    @GET("/store/search")
    fun search(@Query("name") name: String): Call<List<StoreMap>>

    @FormUrlEncoded
    @POST("/goodsEdit/addGoods")
    fun addGoods(@Field("name") name: String, @Field("sid") pw: Int, @Field("tid1") tid1: Int,
                 @Field("tid2") tid2: Int, @Field("tid3") tid3: Int, @Field("price") price: Float): Call<SaleMap>

    @FormUrlEncoded
    @POST("/goodsEdit/editGoods")
    fun editGoods(@Field("gid") gid: Int, @Field("name") name: String, @Field("sid") pw: Int, @Field("price") price: Float,
                  @Field("tid1") tid1: Int, @Field("tid2") tid2: Int, @Field("tid3") tid3: Int): Call<SaleStatus>
}