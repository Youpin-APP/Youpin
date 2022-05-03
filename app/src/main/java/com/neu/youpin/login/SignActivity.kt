package com.neu.youpin.login

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.neu.youpin.R
import kotlinx.android.synthetic.main.activity_sign.*

class SignActivity : AppCompatActivity() {
    private var editTextList = arrayOf<EditText>()
    private var textEmptyList = arrayOf<TextView>()

    private val signSuccess: String = "注册成功"
    private val signError: String = "注册失败"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)
        editTextList = arrayOf(signUserName,signUserPhone,signUserPhoneVer,
            signUserPass,signUserPassAgain)
        textEmptyList = arrayOf(signUserNameEmpty,signUserPhoneEmpty,
            signUserPhoneVerEmpty,signUserPassEmpty,signUserPassAgainEmpty)

        signBackButton.setOnClickListener {
            finish()
        }

        signSignButton.setOnClickListener {
            if(isAllFull()){
                Toast.makeText(this,signSuccess, Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,signError, Toast.LENGTH_SHORT).show()
            }
        }

        for ((index, editText) in editTextList.withIndex()){
            editText.doOnTextChanged { text, start, before, count ->
                if(start == 0 && count > 0){
                    editText.setBackgroundResource(R.drawable.custom_edittext_background)
                    textEmptyList[index].visibility = View.GONE
                }
            }
        }
    }

    // 判断各个文字输入框内是否都填入了内容
    private fun isAllFull(): Boolean{
        var flag = true
        for ((index, editText) in editTextList.withIndex()){
            if(editText.text.isEmpty()){
                textEmptyList[index].visibility = View.VISIBLE
                editText.setBackgroundResource(R.drawable.custom_edittext_error_background)
                flag = false
            }
        }
        return flag
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