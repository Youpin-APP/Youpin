package com.neu.youpin.location

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.widget.doOnTextChanged
import com.neu.youpin.R
import kotlinx.android.synthetic.main.activity_loca_update.*

class LocaUpdateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loca_update)

        LocaUpdateButtonBack.setOnClickListener {
            finish()
        }

        val isEdit = intent.getBooleanExtra("isEdit", false)

        if (isEdit){
            LocaUpdateTitle.text = "修改收货地址"
            val location = intent.getParcelableExtra<Loca>("loca")
            LocaUpdateAddName.setText(location?.name)
            LocaUpdateAddTele.setText(location?.tele?.substring(0,3).plus(" ")
                .plus(location?.tele?.substring(4,7).plus(" ").plus(location?.tele?.substring(7,11))))
            LocaUpdateAddZone.text = location?.province?.plus(" ")
                .plus(location?.city).plus(" ").plus(location?.zone)
            LocaUpdateAddDetail.setText(location?.detailAdd)
            LocaUpdateAddDefault.isChecked = location?.isDefault == true
        }else{
            LocaUpdateTitle.text = "添加收货地址"
        }

        LocaUpdateAddTele.doOnTextChanged { text, start, before, count ->
            if(start==2 && count==1){
                LocaUpdateAddTele.setText(text.toString().plus(" "))
                LocaUpdateAddTele.setSelection(4)
            }
            if(start==4 && before==1){
                LocaUpdateAddTele.setText(text.toString().substring(0,3))
                LocaUpdateAddTele.setSelection(3)
            }
            if(start==7 && count==1){
                LocaUpdateAddTele.setText(text.toString().plus(" "))
                LocaUpdateAddTele.setSelection(9)
            }
            if(start==9 && before==1){
                LocaUpdateAddTele.setText(text.toString().substring(0,8))
                LocaUpdateAddTele.setSelection(8)
            }
        }
    }
}