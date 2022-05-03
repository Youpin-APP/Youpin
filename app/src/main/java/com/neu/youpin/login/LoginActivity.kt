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

class LoginActivity : AppCompatActivity() {
    private val loginSuccess: String = "登录成功"
    private val loginError: String = "用户名密码不匹配"
    private val passEmpty: String = "密码不能为空"
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
                    // 判断用户名密码是否匹配 匹配则提示用户登录成功并自动返回用户界面 否则发出警告
                    if (isUserPassCorrect()){
                        Toast.makeText(this,loginSuccess, Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        loginUserPassEmpty.text = loginError
                        loginUserPassEmpty.visibility = View.VISIBLE
                    }
                }
            }

        }
    }
    // 用于登录使用
    private fun isUserPassCorrect():Boolean{
        return true
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