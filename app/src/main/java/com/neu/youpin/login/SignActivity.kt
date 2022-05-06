package com.neu.youpin.login

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.neu.youpin.R
import com.neu.youpin.entity.ServiceCreator
import com.neu.youpin.entity.UserApplication
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.regex.Pattern

class SignActivity : AppCompatActivity() {
    private var editTextList = arrayOf<EditText>()
    private var textEmptyList = arrayOf<TextView>()
    private var textList = arrayOf("账号名称不能为空",
        "手机号不能为空","密码不能为空","再次输入密码不能为空")

    private val signSuccess: String = "注册成功"
    private val signError: String = "注册失败"

    val signService = ServiceCreator.create<SignService>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)

        editTextList = arrayOf(signUserName,signUserPhone,
            signUserPass,signUserPassAgain)
        textEmptyList = arrayOf(signUserNameEmpty,signUserPhoneEmpty,
            signUserPassEmpty,signUserPassAgainEmpty)


        signBackButton.setOnClickListener {
            finish()
        }

        signSignButton.setOnClickListener {
            if(isAllFull() && regexPhone(signUserPhone.text.toString()) && isPassSame()){
//                Toast.makeText(this,signSuccess, Toast.LENGTH_SHORT).show()
                signByRetrofit()
            }
        }

        for ((index, editText) in editTextList.withIndex()){
            editText.doOnTextChanged { text, start, before, count ->
                if(textEmptyList[index].visibility == View.VISIBLE){
                    editText.setBackgroundResource(R.drawable.custom_edittext_background)
                    textEmptyList[index].visibility = View.GONE
                }
            }
//            Toast.makeText(this,signError, Toast.LENGTH_SHORT).show()
        }
    }

    private fun isSign(success: Boolean){
        if(success){
            Toast.makeText(this, signSuccess, Toast.LENGTH_SHORT).show()
            finish()
        }else{
            signUserPassAgainEmpty.visibility = View.VISIBLE
            signUserPassAgainEmpty.text = "注册失败"
        }
    }

    private fun signByRetrofit(){
        signService.loginPost(signUserPhone.text.toString(),signUserPass.text.toString()).enqueue(object :
            Callback<SignToken> {
            override fun onResponse(call: Call<SignToken>,
                                    response: Response<SignToken>
            ) {
                val list = response.body()
                if (list != null) {
                    isSign(list.status)
                }else toastNetworkError()
            }
            override fun onFailure(call: Call<SignToken>, t: Throwable) {
                t.printStackTrace()
                Log.d("SignActivity", "network failed")
            }
        })
    }

    private fun toastNetworkError(){
        signUserPassAgainEmpty.visibility = View.VISIBLE
        signUserPassAgainEmpty.text = "网络连接错误"
    }


    // 判断各个文字输入框内是否都填入了内容
    private fun isAllFull(): Boolean{
        var flag = true
        for ((index, editText) in editTextList.withIndex()){
            if(editText.text.isEmpty()){
                textEmptyList[index].visibility = View.VISIBLE
                textEmptyList[index].text = textList[index]
                editText.setBackgroundResource(R.drawable.custom_edittext_error_background)
                flag = false
            }
        }
        return flag
    }

    private fun isPassSame(): Boolean{
        return if(signUserPass.text.toString() == signUserPassAgain.text.toString())
            true
        else{
            signUserPassAgainEmpty.visibility = View.VISIBLE
            signUserPassAgainEmpty.text = "两次输入密码不一致"
            false
        }
    }

    private fun regexPhone(phone: String): Boolean {
        val mainRegex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,1,2,3,5-9])|(177))\\d{8}$"
        val p = Pattern.compile(mainRegex)
        val m = p.matcher(phone)
        return if(m.matches()){
            true
        }else{
            signUserPhoneEmpty.visibility = View.VISIBLE
            signUserPhoneEmpty.text = "手机号输入不符合规范"
            false
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
    }
}
class SignToken(val status: Boolean)

interface SignService {
    @FormUrlEncoded
    @POST("user/register")
    fun loginPost(@Field("uid") uid: String, @Field("pw") pw: String): Call<SignToken>
}