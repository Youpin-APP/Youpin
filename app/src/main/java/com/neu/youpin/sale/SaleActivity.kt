package com.neu.youpin.sale

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.neu.youpin.R
import com.neu.youpin.location.LocationDialog
import kotlinx.android.synthetic.main.activity_loca_update.*
import kotlinx.android.synthetic.main.activity_sale.*

class SaleActivity : AppCompatActivity() {
    private var builderForDialog: SelectClassDialog.Builder? = null
    private var locaDialog: SelectClassDialog? = null

    //class id 为-1时代表需要新建！！！

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sale)

        builderForDialog = SelectClassDialog.Builder(this)

        SaleGoodTid1.setOnClickListener {
            locaDialog = builderForDialog!!.setListener(object: SelectClassDialog.SaleListener{
                override fun setActivityText(className: String, classId: Int){
                    SaleGoodTid1.text = className
                }
            }).setTitle("商品一级细分类").createDialog()
            locaDialog!!.show()
        }

        SaleGoodTid2.setOnClickListener {
            locaDialog = builderForDialog!!.setListener(object: SelectClassDialog.SaleListener{
                override fun setActivityText(className: String, classId: Int){
                    SaleGoodTid2.text = className
                }
            }).setTitle("商品二级细分类").createDialog()
            locaDialog!!.show()
        }

        SaleGoodTid3.setOnClickListener {
            locaDialog = builderForDialog!!.setListener(object: SelectClassDialog.SaleListener{
                override fun setActivityText(className: String, classId: Int){
                    SaleGoodTid3.text = className
                }
            }).setTitle("商品三级细分类").createDialog()
            locaDialog!!.show()
        }
    }
}