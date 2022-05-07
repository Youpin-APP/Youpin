package com.neu.youpin.location

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neu.youpin.R
import com.neu.youpin.entity.ServiceCreator
import com.neu.youpin.entity.UserApplication
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_loca_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class LocaListActivity : AppCompatActivity() {
    private var userLocaList: MutableList<Loca>? = null

    private val locaListService = ServiceCreator.create<LocaListService>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loca_list)

        // 设置地址页面
        initUserLocaList()

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
        UserApplication.getInstance().getId()?.let {
            locaListService.getAddrList(it).enqueue(object : Callback<List<Loca>> {
                override fun onResponse(call: Call<List<Loca>>,
                                        response: Response<List<Loca>>
                ) {
                    userLocaList = response.body()?.toMutableList()
                    initRecycleView()
                }

                override fun onFailure(call: Call<List<Loca>>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("LocaListActivity", "network failed")
                }
            })
        }

    }

    private fun initRecycleView(){
        var defaultIndex = -1
        for((i, item) in userLocaList!!.withIndex()){
            if(item.default == 1){
                defaultIndex = i
                break
            }
        }
        if(defaultIndex > 0) userLocaList!!.swap(0, defaultIndex)
        val layoutManager = LinearLayoutManager(this) //线性布局布局管理器
        val recyclerView:RecyclerView = findViewById(R.id.LocaListList)
        recyclerView.layoutManager = layoutManager
        val adapter = userLocaList?.let { ListAdapter(this, it, recyclerView, object :OnDeleteListener{
            override fun onDelete(position: Int) {
                delByRetrofit(position)
            }
        }) }
        recyclerView.adapter = adapter
    }

    private fun toastNetworkError(){
        Toast.makeText(this,"网络错误",Toast.LENGTH_SHORT).show()
    }

    private fun isDel(success: Boolean, position: Int){
        if(success){
//            Toast.makeText(this, "删除地址成功", Toast.LENGTH_SHORT).show()
            userLocaList?.removeAt(position)
            initRecycleView()
        }else{
            Toast.makeText(this,"删除地址失败",Toast.LENGTH_SHORT).show()
        }
    }

    private fun delByRetrofit(position: Int){
        locaListService.delAddr(userLocaList?.get(position)!!.aid).enqueue(object : Callback<LocaUpdateMap> {
            override fun onResponse(call: Call<LocaUpdateMap>,
                                    response: Response<LocaUpdateMap>
            ) {
                val list = response.body()
                if (list != null) {
                    isDel(list.success, position)
                }else toastNetworkError()
            }

            override fun onFailure(call: Call<LocaUpdateMap>, t: Throwable) {
                t.printStackTrace()
                Log.d("LocaListActivity", "network failed")
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
        initUserLocaList()
    }
}

@Parcelize
data class Loca(var aid: Int, var name:String, var tel:String, var pname: String, var pid: Int,
                var cname:String, var cid: Int, var dname:String, var did: Int,var detail: String,
                var success: Boolean, var default: Int):Parcelable{}



interface LocaListService {
    @FormUrlEncoded
    @POST("user/getAddrList")
    fun getAddrList(@Field("uid") uid: String): Call<List<Loca>>

    @FormUrlEncoded
    @POST("/user/delAddr")
    fun delAddr(@Field("aid") aid: Int): Call<LocaUpdateMap>
}

interface OnDeleteListener{
    fun onDelete(position: Int)
}

class ListAdapter(context: Context, private val locaList: List<Loca>, recyclerView: RecyclerView, onDeleteListener: OnDeleteListener) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    //自定义嵌套内部类 ViewHolder 来减少 findViewById() 的使用， 继承RecyclerView的ViewHolder
    //通过图片的id获取对应的视图，以便后续操作

    private val mContext = context

    private val mRecyclerView = recyclerView

    private val deleteListener = onDeleteListener

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val listName: TextView = view.findViewById(R.id.LocaItemAddName)
        val listPhone: TextView = view.findViewById(R.id.LocaItemAddPhone)
        val listDefault: TextView = view.findViewById(R.id.LocaItemAddDefault)
        val listDetail: TextView = view.findViewById(R.id.LocaItemAddDetail)
        val listEdit: ImageButton = view.findViewById(R.id.LocaItemButtonEdit)
        val listDelete: RelativeLayout = view.findViewById(R.id.LocaItemButtonDelete)
    }

    //设置初始的布局
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.loca_list_item, parent, false)
        //第一个参数为单个图片对应的布局文件，第二个参数为RecyclerView要显示的位置
        //第三个参数设置为false效果为你在xml中设置为什么具体显示就为什么
        val deleteView = view as LeftDeleteView
        deleteView.setRecyclerView(mRecyclerView)
        return ViewHolder(deleteView)
    }       //加载布局

    //设置初次加载、滑动时的布局
    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = locaList[position] //获取当前位置对应的loca
        holder.listName.text = location.name
        holder.listPhone.text = location.tel.substring(0,3).plus("****").plus(location.tel.substring(7,11))
        if (location.default == 1) holder.listDefault.visibility = View.VISIBLE
        else holder.listDefault.visibility = View.INVISIBLE
        holder.listDetail.text = location.pname.plus(" ").plus(location.cname)
            .plus(" ").plus(location.dname).plus(location.detail)
        holder.listEdit.setOnClickListener {
            val locaIntent = Intent(mContext, LocaUpdateActivity::class.java).apply {
                putExtra("loca", location)
                putExtra("isEdit",true)
            }
            mContext.startActivity(locaIntent)
        }
        holder.listDelete.setOnClickListener {
            deleteListener.onDelete(position)
        }
    }

    //获取列表中的项目个数，将其定义为数据数组的个数
    override fun getItemCount() = locaList.size
}

fun <T> MutableList<T>.swap(index1: Int, index2: Int){
    val tmp = this[index1]
    this[index1] = this[index2]
    this[index2] = tmp
}