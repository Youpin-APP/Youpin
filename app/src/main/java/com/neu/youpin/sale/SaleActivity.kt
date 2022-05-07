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

    private var tid = arrayOf(-1, -1, -1)
    private var tName = arrayOf("", "", "")
    private var gid = 2


    //class id 为-1时代表需要新建！！！

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sale)

        builderForDialog = SelectClassDialog.Builder(this)

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
    }
}