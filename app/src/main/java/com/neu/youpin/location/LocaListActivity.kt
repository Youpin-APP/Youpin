package com.neu.youpin.location

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neu.youpin.R
import com.neu.youpin.store.StoreActivity
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_loca_list.*

class LocaListActivity : AppCompatActivity() {
    private val userLocaList = ArrayList<Loca>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loca_list)

        // 设置地址页面
        initUserLocaList()
        val layoutManager = LinearLayoutManager(this) //线性布局布局管理器
        val recyclerView:RecyclerView = findViewById(R.id.LocaListList)
        recyclerView.layoutManager = layoutManager
        val adapter = ListAdapter(userLocaList)
        recyclerView.adapter = adapter

        // 设置按钮点击事件
        // 返回按钮，触发结束当前activity
        LocaListButtonBack.setOnClickListener {
            finish()
        }

        LocaListButtonAdd.setOnClickListener {
            val locaIntent = Intent(this, LocaUpdateActivity::class.java).apply {
                putExtra("isEdit",false)
            }
            startActivity(locaIntent)
        }
    }

    private fun initUserLocaList(){
//        userLocaList.add(Loca("汤神", "13611415114","辽宁省","沈阳市","浑南区","东北大学", true))
//        repeat(5){
//            userLocaList.add(Loca("汤神", "13611415114","辽宁省","沈阳市","浑南区","东北大学", false))
//        }
    }

}

@Parcelize
data class Loca(var aid: Int, var name:String, var tel:String, var pname: String, var pid: Int,
                var cname:String, var cid: Int, var dname:String, var did: Int,var detail: String,
                var success: Boolean, var default: Int):Parcelable{}

class ListAdapter(private val locaList: List<Loca>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    //自定义嵌套内部类 ViewHolder 来减少 findViewById() 的使用， 继承RecyclerView的ViewHolder
    //通过图片的id获取对应的视图，以便后续操作
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val listName: TextView = view.findViewById(R.id.LocaItemAddName)
        val listPhone: TextView = view.findViewById(R.id.LocaItemAddPhone)
        val listDefault: TextView = view.findViewById(R.id.LocaItemAddDefault)
        val listDetail: TextView = view.findViewById(R.id.LocaItemAddDetail)
        val listEdit: ImageButton = view.findViewById(R.id.LocaItemButtonEdit)
        val context: Context = view.context
    }

    //设置初始的布局
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.loca_list_item, parent, false)
        //第一个参数为单个图片对应的布局文件，第二个参数为RecyclerView要显示的位置
        //第三个参数设置为false效果为你在xml中设置为什么具体显示就为什么
        return ViewHolder(view)
    }       //加载布局

    //设置初次加载、滑动时的布局
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = locaList[position] //获取当前位置对应的loca
        holder.listName.text = location.name
        holder.listPhone.text = location.tel.substring(0,3).plus("****").plus(location.tel.substring(4,11))
        if (location.default == 1) holder.listDefault.visibility = View.VISIBLE
        else holder.listDefault.visibility = View.INVISIBLE
        holder.listDetail.text = location.pname.plus(" ").plus(location.cname)
            .plus(" ").plus(location.dname).plus(location.detail)
        holder.listEdit.setOnClickListener {
            val locaIntent = Intent(holder.context, LocaUpdateActivity::class.java).apply {
                putExtra("loca", location)
                putExtra("isEdit",true)
            }
            holder.context.startActivity(locaIntent)
        }
    }

    //获取列表中的项目个数，将其定义为水果数组的个数
    override fun getItemCount() = locaList.size
}