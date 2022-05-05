package com.neu.youpin.login

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import com.neu.youpin.R
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class LoginActivity : AppCompatActivity() {
    private val loginSuccess: String = "登录成功"
    private val loginError: String = "用户名密码不匹配"
    private val passEmpty: String = "密码不能为空"

    private val url: String = "http://hqyz.cf:8080/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

//        loginUserNameEmpty

        loginBackButton.setOnClickListener {
            finish()
        }

        loginUserSign.setOnClickListener {
            val intent = Intent(this, SignActivity::class.java)
            startActivity(intent)
        }

        loginUserName.doOnTextChanged { _, start, _, count ->
            if(start == 0 && count > 0)loginUserNameEmpty.visibility = View.GONE
        }

        loginUserPass.doOnTextChanged { _, start, _, count ->
            if(start == 0 && count > 0) loginUserPassEmpty.visibility = View.GONE
        }

        loginLoginButton.setOnClickListener {
            // 判断用户手机号输入是否为空 为空则发出警告 否则不显示警告 继续下一个判断
            if(loginUserName.text.isEmpty()){
                loginUserNameEmpty.visibility = View.VISIBLE
            }else{
                // 判断用户密码输入是否为空 为空则发出警告 否则不显示警告 继续下一个判断
                if(loginUserPass.text.isEmpty()) {
                    loginUserPassEmpty.text = passEmpty
                    loginUserPassEmpty.visibility = View.VISIBLE
                }else{
                    isUserPassCorrect()
                }
            }

        }
    }

    private fun isLogin(success: Boolean){
        if (success){
            Toast.makeText(this,loginSuccess, Toast.LENGTH_SHORT).show()
            finish()
        }else{
            loginUserPassEmpty.text = loginError
            loginUserPassEmpty.visibility = View.VISIBLE
        }
    }

    // 用于登录使用
    private fun isUserPassCorrect(){
        Toast.makeText(this,"开始尝试登录", Toast.LENGTH_SHORT).show()
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val appService = retrofit.create(LoginService::class.java)
        appService.loginPost(loginUserName.text.toString(),loginUserPass.text.toString()).enqueue(object : Callback<LoginToken> {
            override fun onResponse(call: Call<LoginToken>,
                                    response: Response<LoginToken>
            ) {
                val list = response.body()
                if (list != null) {
                    Log.d("LoginActivity", "success is ${list.success}")
                    Log.d("LoginActivity", "name is ${list.token}")
                    isLogin(list.success)
                }else isLogin(false)
            }
            override fun onFailure(call: Call<LoginToken>, t: Throwable) {
                t.printStackTrace()
                Log.d("LoginActivity", "network failed")
            }
        })
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
        loginUserNameEmpty.visibility = View.GONE
        loginUserPassEmpty.visibility = View.GONE
//        loginUserName.setBackgroundColor(Color.parseColor("#ff0000"))
//        loginUserName.setBackgroundResource(R.drawable.custom_edittext_error_background)
    }
}

class LoginToken(val success: Boolean, val token: String)

interface LoginService {
    @FormUrlEncoded
    @POST("user/login")
    fun loginPost(@Field("uid") uid: String, @Field("pw") pw: String): Call<LoginToken>
}