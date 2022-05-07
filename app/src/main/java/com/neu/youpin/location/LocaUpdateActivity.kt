package com.neu.youpin.location

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.neu.youpin.R
import com.neu.youpin.entity.ServiceCreator
import com.neu.youpin.entity.UserApplication
import com.neu.youpin.login.SignToken
import kotlinx.android.synthetic.main.activity_loca_update.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.lang.StringBuilder
import java.util.regex.Pattern

class LocaUpdateActivity : AppCompatActivity() {

    private var isSaveAble = arrayOf(true, true, true, true)
    private var isTextChange:Boolean = false

    private val NUM_3 = 3
    private val NUM_7 = 8

    private var did:Int = 0

    private val greyColor: Int = Color.parseColor("#9e9e9e")


    private val locaUpdateService = ServiceCreator.create<LocaUpdateService>()

    private var builderForDialog: LocationDialog.Builder? = null
    private var locaDialog: LocationDialog? = null

    private var isEdit:Boolean = false

    private var location:Loca? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loca_update)

//        builderForCustom = LocaDialog.Builder(this)
        builderForDialog = LocationDialog.Builder(this)

        LocaUpdateButtonBack.setOnClickListener {
            finish()
        }

        LocaUpdateButtonClear.setOnClickListener {
            LocaUpdateAddTele.setText("")
            LocaUpdateButtonClear.visibility = View.INVISIBLE
        }

        LocaUpdateAddZone.setOnClickListener{
            showLocationDialog()
        }

        LocaUpdateSave.setOnClickListener {
            addByRetrofit()
        }

        isEdit = intent.getBooleanExtra("isEdit", false)

        if (isEdit){
            LocaUpdateTitle.text = "修改收货地址"
            location = intent.getParcelableExtra<Loca>("loca")
            LocaUpdateAddName.setText(location?.name)
            LocaUpdateAddTele.setText(location?.tel?.substring(0,3).plus(" ")
                .plus(location?.tel?.substring(3,7).plus(" ").plus(location?.tel?.substring(7,11))))
            LocaUpdateAddZone.text = location?.pname?.plus(" ")
                .plus(location?.cname).plus(" ").plus(location?.dname)
            LocaUpdateAddDetail.setText(location?.detail)
            LocaUpdateAddDefault.isChecked = location?.default == 1
            LocaUpdateSave.isClickable = true
            LocaUpdateSave.setTextColor(Color.BLACK)
        }else{
//            LocaUpdateAddTele.setText(LocaUpdateAddTele.text.toString().replace("\\s".toRegex(), ""))
            LocaUpdateTitle.text = "添加收货地址"
            isSaveAble = arrayOf(false, true, false, false)
            LocaUpdateSave.isClickable = false
            LocaUpdateSave.setTextColor(greyColor)
        }

        // 设置号码输入格式为 xxx xxxx xxxx
        LocaUpdateAddTele.doOnTextChanged { text, start, before, count ->
            // 判断文本是否发生了变化 如果发生了变化则说明本次调度为程序设置文本所引起的
            if (isTextChange) isTextChange = false
            else{
                addTabTele(text.toString())
                if(count > 0){
                    var section:Int = start - before + count
                    if (section == 4) section += 1
                    if (section == 9) section += 1
                    if(section >= 0 && section <= LocaUpdateAddTele.text.toString().length) LocaUpdateAddTele.setSelection(section)
                }
                else if (before > 0){
                    var section:Int = start
                    if (section + before > 9 && section < 10) section -= 1
                    if (section + before > 4 && section < 5) section -= 1
                    if(section >= 0 && section <= LocaUpdateAddTele.text.toString().length) LocaUpdateAddTele.setSelection(section)
                }
            }
            val length = LocaUpdateAddTele.text.length
            if(length == 0){
                LocaUpdateButtonClear.visibility = View.INVISIBLE
            }
            else{
                LocaUpdateButtonClear.visibility = View.VISIBLE
            }
            if(length == 13){
                isSaveAble[1] = true
                judgeSaveAble()
            }else if(LocaUpdateSave.isClickable){
                isSaveAble[1] = false
                LocaUpdateSave.isClickable = false
                LocaUpdateSave.setTextColor(greyColor)
            }
        }

        LocaUpdateAddName.doOnTextChanged { _, _, _, _,->
            if(LocaUpdateAddName.text.isEmpty()){
                isSaveAble[0] = false
                if(LocaUpdateSave.isClickable){
                    LocaUpdateSave.isClickable = false
                    LocaUpdateSave.setTextColor(greyColor)
                }
            }else {
                isSaveAble[0] = true
                judgeSaveAble()
            }
        }

        LocaUpdateAddDetail.doOnTextChanged { _, _, _, _, ->
            if(LocaUpdateAddDetail.text.isEmpty()){
                isSaveAble[3] = false
                if(LocaUpdateSave.isClickable){
                    LocaUpdateSave.isClickable = false
                    LocaUpdateSave.setTextColor(greyColor)
                }
            }else {
                isSaveAble[3] = true
                judgeSaveAble()
            }
        }
    }


    private fun toastNetworkError(){
        Toast.makeText(this,"网络错误",Toast.LENGTH_SHORT).show()
    }

    private fun isAdd(success: Boolean){
        if(success){
            Toast.makeText(this, "添加新地址成功", Toast.LENGTH_SHORT).show()
            finish()
        }else{
            Toast.makeText(this,"添加新地址失败",Toast.LENGTH_SHORT).show()
        }
    }

    private fun isDefault():Int{
        return if(LocaUpdateAddDefault.isSelected) 1 else 0
    }

    private fun addByRetrofit(){
        Toast.makeText(this,isDefault().toString(),Toast.LENGTH_SHORT).show()
        UserApplication.getInstance().getId()?.let {
            if(isEdit){
//                locaUpdateService.updateAddr(, did ,LocaUpdateAddDetail.text.toString(), LocaUpdateAddName.text.toString(),
//                    LocaUpdateAddTele.text.toString().replace(" ",""),
//                    if(LocaUpdateAddDefault.isSelected) 1 else 0 ).enqueue(object :
//                    Callback<LocaUpdateMap> {
//                    override fun onResponse(call: Call<LocaUpdateMap>,
//                                            response: Response<LocaUpdateMap>
//                    ) {
//                        val list = response.body()
//                        if (list != null) {
//                            isAdd(list.success)
//                        }else toastNetworkError()
//                    }
//
//                    override fun onFailure(call: Call<LocaUpdateMap>, t: Throwable) {
//                        t.printStackTrace()
//                        Log.d("SignActivity", "network failed")
//                    }
//                })
            }else{
                locaUpdateService.addAddr(it, did ,LocaUpdateAddDetail.text.toString(), LocaUpdateAddName.text.toString(),
                    LocaUpdateAddTele.text.toString().replace(" ",""), isDefault() ).enqueue(object :
                    Callback<LocaUpdateMap> {
                    override fun onResponse(call: Call<LocaUpdateMap>,
                                            response: Response<LocaUpdateMap>
                    ) {
                        val list = response.body()
                        if (list != null) {
                            isAdd(list.success)
                        }else toastNetworkError()
                    }

                    override fun onFailure(call: Call<LocaUpdateMap>, t: Throwable) {
                        t.printStackTrace()
                        Log.d("SignActivity", "network failed")
                    }
                })
            }
        }
    }

    private fun judgeSaveAble(){
        if(!LocaUpdateSave.isClickable && isSaveAble[0] && isSaveAble[1]&&
            isSaveAble[2] && isSaveAble[3]){
            LocaUpdateSave.isClickable = true
            LocaUpdateSave.setTextColor(Color.BLACK)
        }
    }

    private fun formatPhoneNum(phoneNum: String):String{
        val regex = "(\\+86)|[^0-9]";
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(phoneNum)
        return matcher.replaceAll("")
    }

    private fun addTabTele(text: String){
        isTextChange = true
        val tempText:StringBuilder = StringBuilder(text.replace("\\s".toRegex(), ""))
        if (tempText.length > NUM_3){
            tempText.insert(NUM_3, ' ')
        }
        if (tempText.length > NUM_7){
            tempText.insert(NUM_7, ' ')
        }
        LocaUpdateAddTele.setText(tempText)
    }

    private fun showLocationDialog(){
        locaDialog = builderForDialog!!.setListener(object: LocationDialog.PriorityListener{
            override fun setActivityText(userLocation: String, id: Int){
                LocaUpdateAddZone.text = userLocation
                did = id
                isSaveAble[2] = true
                judgeSaveAble()
            }
        }).createDialog()
        locaDialog!!.show()
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
    }
}

class LocaUpdateMap(val success: Boolean)

interface LocaUpdateService {
    @FormUrlEncoded
    @POST("user/addAddr")
    fun addAddr(@Field("uid") uid: String, @Field("did") did: Int,
                  @Field("addrDetail") addrDetail: String,@Field("name") name: String,
                  @Field("tel") tel: String, @Field("isDefault") isDefault: Int): Call<LocaUpdateMap>

    @FormUrlEncoded
    @POST("user/updateAddr")
    fun updateAddr(@Field("aid") aid: Int, @Field("did") did: Int,
                @Field("addrDetail") addrDetail: String,@Field("name") name: String,
                @Field("tel") tel: String, @Field("isDefault") isDefault: Int): Call<LocaUpdateMap>
}