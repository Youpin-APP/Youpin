package com.neu.youpin.location

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import com.neu.youpin.R
import kotlinx.android.synthetic.main.activity_loca_update.*
import java.lang.StringBuilder
import java.util.regex.Pattern

class LocaUpdateActivity : AppCompatActivity() {

    private var isSaveAble = arrayOf(true, true, true, true)
    private var isTextChange:Boolean = false

    private val NUM_3 = 3
    private val NUM_7 = 8


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loca_update)

        LocaUpdateButtonBack.setOnClickListener {
            finish()
        }

        LocaUpdateButtonClear.setOnClickListener {
            LocaUpdateAddTele.setText("")
            LocaUpdateButtonClear.visibility = View.INVISIBLE
        }

        val isEdit = intent.getBooleanExtra("isEdit", false)

        if (isEdit){
            LocaUpdateTitle.text = "修改收货地址"
            val location = intent.getParcelableExtra<Loca>("loca")
            LocaUpdateAddName.setText(location?.name)
            LocaUpdateAddTele.setText(location?.tele?.substring(0,3).plus(" ")
                .plus(location?.tele?.substring(3,7).plus(" ").plus(location?.tele?.substring(7,11))))
            LocaUpdateAddZone.text = location?.province?.plus(" ")
                .plus(location?.city).plus(" ").plus(location?.zone)
            LocaUpdateAddDetail.setText(location?.detailAdd)
            LocaUpdateAddDefault.isChecked = location?.isDefault == true
            LocaUpdateSave.isClickable = true
            LocaUpdateSave.setTextColor(Color.BLACK)
        }else{
//            LocaUpdateAddTele.setText(LocaUpdateAddTele.text.toString().replace("\\s".toRegex(), ""))
            LocaUpdateTitle.text = "添加收货地址"
            isSaveAble = arrayOf(false, false, false, false)
            LocaUpdateSave.isClickable = false
            LocaUpdateSave.setTextColor(Color.parseColor("#9e9e9e"))
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
//            Log.d("start",start.toString())
//            Log.d("before",before.toString())
//            Log.d("count",count.toString())
            if(LocaUpdateAddTele.text.toString().isEmpty()) LocaUpdateButtonClear.visibility = View.INVISIBLE
            else LocaUpdateButtonClear.visibility = View.VISIBLE
//            if(start== 12 && count==1){
//                isSaveAble[1] = true
//            }else if ()
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

}