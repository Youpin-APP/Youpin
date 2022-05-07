package com.neu.youpin.saleUpload

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.neu.youpin.Interface.OnItemClickListener
import com.neu.youpin.R
import com.neu.youpin.entity.ServiceCreator
import kotlinx.android.synthetic.main.activity_store.*
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*
import java.io.File
import java.io.FileOutputStream
import java.util.*


class SaleUploadActivity : AppCompatActivity(), OnItemClickListener {
    private val bannerImgItemList = Vector<ImgItem>()
    private val detailImgItemList = Vector<ImgItem>()
    private val fromAlbum = 2
    private var pos = 0
    private var gid = -1
    private val imgService = ServiceCreator.create<ImgService>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sale_upload)

        if (Build.VERSION.SDK_INT > 9) {
            val policy = ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
        gid = intent.getIntExtra("gid", -1)
//        if (gid == -1) {
////            toast("gid参数异常")
////            finish();
//            gid = 2
//        }
        updateItem()
        val layoutManagerDetail = LinearLayoutManager(this)

        val recyclerViewDetail = findViewById<RecyclerView>(R.id.detailImgEditList)
        recyclerViewDetail.layoutManager = layoutManagerDetail
        val adapterDetail = SaleUploadDetailListAdapter(detailImgItemList, this)
        adapterDetail.setOnItemClickListener(this)
        recyclerViewDetail.adapter = adapterDetail

        val layoutManagerBanner = LinearLayoutManager(this)
        layoutManagerBanner.orientation = LinearLayoutManager.HORIZONTAL
        val recyclerViewBanner = findViewById<RecyclerView>(R.id.bannerImgEditList)
        recyclerViewBanner.layoutManager = layoutManagerBanner
        Log.d("bannerTotal", bannerImgItemList.size.toString())
        val adapterBanner = SaleUploadBannerListAdapter(bannerImgItemList, this)
        adapterBanner.setOnItemClickListener(this)
        recyclerViewBanner.adapter = adapterBanner


        val addBannerImg: Button = findViewById(R.id.addBannerImg)
        val deleteBannerImg: Button = findViewById(R.id.deleteBannerImg)
        val addDetailImg: Button = findViewById(R.id.addDetailImg)
        val deleteDetailImg: Button = findViewById(R.id.deleteDetailImg)

        addBannerImg.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            pos = 0
            startActivityForResult(intent, fromAlbum)
        }
        deleteBannerImg.setOnClickListener {
            val list = ArrayList<Int>()
            for (index in bannerImgItemList.indices) {
                if(bannerImgItemList[index].selected){
                    list.add(index);
                }
            }
            Log.d("delBanner", list.toString())
            imgService.delGoodsBannerPic(gid, list).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    updateItem()
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("LoginActivity", "network failed")
                }
            })
        }
        addDetailImg.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            pos = 1
            startActivityForResult(intent, fromAlbum)
        }
        deleteDetailImg.setOnClickListener {
            val list = ArrayList<Int>()
            for (index in detailImgItemList.indices) {
                if(detailImgItemList[index].selected){
                    list.add(index);
                }
            }
            Log.d("delDetail", list.toString())
            imgService.delGoodsDetailPic(gid, list).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    updateItem()
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("LoginActivity", "network failed")
                }
            })
        }
    }

    private fun updateItem() {
        imgService.getGoodsInfo(gid).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                bannerImgItemList.clear()
                detailImgItemList.clear()
                val jsonStr = String(response.body()!!.bytes())
                val obj = Gson().fromJson(jsonStr, JsonObject::class.java)
                val bannerArray = obj.getAsJsonArray("pic_banner")
                for (jsonElement in bannerArray) {
                    Log.d(
                        "addGoodsInfo",
                        "http://hqyz.cf:8080/pic/" + jsonElement.asJsonObject.get("url").asString
                    )
                    bannerImgItemList.addElement(
                        ImgItem(
                            "http://hqyz.cf:8080/pic/" + jsonElement.asJsonObject.get("url").asString,
                            selected = false,
                            loaded = false
                        )
                    )
                    Log.d("bannerTotal", bannerImgItemList.size.toString())
                    val recyclerViewBanner = findViewById<RecyclerView>(R.id.bannerImgEditList)
                    recyclerViewBanner.adapter!!.notifyDataSetChanged()
                }
                val detailArray = obj.getAsJsonArray("pic_detail")
                for (jsonElement in detailArray) {
                    Log.d(
                        "addGoodsInfo",
                        "http://hqyz.cf:8080/pic/" + jsonElement.asJsonObject.get("url").asString
                    )
                    detailImgItemList.addElement(
                        ImgItem(
                            "http://hqyz.cf:8080/pic/" + jsonElement.asJsonObject.get("url").asString,
                            selected = false,
                            loaded = false
                        )
                    )
                    Log.d("bannerTotal", detailImgItemList.size.toString())
                    val recyclerViewDetail = findViewById<RecyclerView>(R.id.detailImgEditList)
                    recyclerViewDetail.adapter!!.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
                Log.d("LoginActivity", "network failed")
            }
        })

    }

    override fun onItemClick(pos: Int, id: Int) {
        when (id) {
            R.id.bannerImgSelect -> {
                bannerImgItemList[pos].selected = !bannerImgItemList[pos].selected
            }
            R.id.detailImgSelect -> {
                detailImgItemList[pos].selected = !detailImgItemList[pos].selected
            }
            R.id.detailImgMoveUp -> {
                if (pos > 0) {
                    imgService.moveDetailPicUp(gid,pos).enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            updateItem()
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            t.printStackTrace()
                            Log.d("LoginActivity", "network failed")
                        }
                    })
                }
            }
            R.id.detailImgMoveDown -> {
                imgService.moveDetailPicDown(gid,pos).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        updateItem()
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        t.printStackTrace()
                        Log.d("LoginActivity", "network failed")
                    }
                })
            }
            R.id.bannerImgMoveLeft -> {
                imgService.moveBannerPicLeft(gid,pos).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        updateItem()
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        t.printStackTrace()
                        Log.d("LoginActivity", "network failed")
                    }
                })
            }
            R.id.bannerImgMoveRight -> {
                imgService.moveBannerPicRight(gid,pos).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        updateItem()
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        t.printStackTrace()
                        Log.d("LoginActivity", "network failed")
                    }
                })
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            fromAlbum -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.let { uri ->
                        val bitmap = getBitmapFromUri(uri)
                        if (bitmap != null) {
                            uploadImg(bitmap)
                        }
                    }
                }
            }
        }
    }

    private fun getBitmapFromUri(uri: Uri) = contentResolver.openFileDescriptor(uri, "r")?.use {
        BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
    }

    private fun uploadImg(bitmap: Bitmap) {
        val file = File(Environment.getExternalStorageDirectory().absolutePath + "/buffer_Pic");
        if (!file.exists()) {
            file.createNewFile()
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))
        val fileName = "pic.jpg"
        val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file)
        val partFile = MultipartBody.Part.createFormData("file", fileName, requestFile)
        val partName = MultipartBody.Part.createFormData("gid", gid.toString())
        when (pos) {
            0 -> {
                imgService.bannerImgPost(partName, partFile)
                    .enqueue(object : Callback<ImgPostResult> {
                        override fun onResponse(
                            call: Call<ImgPostResult>,
                            response: Response<ImgPostResult>
                        ) {
                            val list = response.body()
                            if (list != null) {
                                if (list.success) {
                                    toast("上传成功")
                                } else {
                                    toast("上传失败")
                                }
                            } else {
                                toast("上传失败")
                            }
                            updateItem()
                        }

                        override fun onFailure(call: Call<ImgPostResult>, t: Throwable) {
                            t.printStackTrace()
                            Log.d("LoginActivity", "network failed")
                        }
                    })
            }
            1 -> {
                imgService.detailImgPost(partName, partFile)
                    .enqueue(object : Callback<ImgPostResult> {
                        override fun onResponse(
                            call: Call<ImgPostResult>,
                            response: Response<ImgPostResult>
                        ) {
                            val list = response.body()
                            if (list != null) {
                                if (list.success) {
                                    toast("上传成功")
                                } else {
                                    toast("上传失败")
                                }
                            } else {
                                toast("上传失败")
                            }
                            updateItem()
                        }

                        override fun onFailure(call: Call<ImgPostResult>, t: Throwable) {
                            t.printStackTrace()
                            Log.d("LoginActivity", "network failed")
                        }
                    })
            }
        }


    }

    private fun toast(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show()
    }

    class ImgPostResult(val success: Boolean)


    interface ImgService {
        @Multipart
        @POST("goodsEdit/addGoodsBannerPic")
        fun bannerImgPost(@Part name: MultipartBody.Part, @Part file: MultipartBody.Part):
                Call<ImgPostResult>

        @Multipart
        @POST("goodsEdit/addGoodsDetailPic")
        fun detailImgPost(@Part name: MultipartBody.Part, @Part file: MultipartBody.Part):
                Call<ImgPostResult>

        @GET("goods/getInfo")
        fun getGoodsInfo(@Query("id") id: Int): Call<ResponseBody>

        @FormUrlEncoded
        @POST("goodsEdit/moveBannerPicLeft")
        fun moveBannerPicLeft(@Field("gid") gid: Int, @Field("pid") pid: Int): Call<ResponseBody>

        @FormUrlEncoded
        @POST("goodsEdit/moveBannerPicRight")
        fun moveBannerPicRight(@Field("gid") gid: Int, @Field("pid") pid: Int): Call<ResponseBody>

        @FormUrlEncoded
        @POST("goodsEdit/moveDetailPicUp")
        fun moveDetailPicUp(@Field("gid") gid: Int, @Field("pid") pid: Int): Call<ResponseBody>

        @FormUrlEncoded
        @POST("goodsEdit/moveDetailPicDown")
        fun moveDetailPicDown(@Field("gid") gid: Int, @Field("pid") pid: Int): Call<ResponseBody>

        @FormUrlEncoded
        @POST("goodsEdit/delGoodsBannerPic")
        fun delGoodsBannerPic(@Field("gid") gid: Int, @Field("pid") pid: List<Int>): Call<ResponseBody>

        @FormUrlEncoded
        @POST("goodsEdit/delGoodsDetailPic")
        fun delGoodsDetailPic(@Field("gid") gid: Int, @Field("pid") pid: List<Int>): Call<ResponseBody>

    }


}