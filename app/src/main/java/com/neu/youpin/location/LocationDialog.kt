package com.neu.youpin.location

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neu.youpin.R
import com.neu.youpin.entity.ServiceCreator
import com.neu.youpin.sale.SelectClassService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class LocationDialog: Dialog {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, theme: Int) : super(context, theme) {}

    /**
     * 自定义Dialog监听器
     */
    interface PriorityListener {
        /**
         * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
         */
        fun setActivityText(userLocation: String, did: Int)
    }

    class Builder(context: Context) {
        private var title: String? = null
        private var callBackListener: PriorityListener? = null

        private var locaList: List<LocaList>? = null

        private var pid: Int = 1
        private var pname: String = ""
        private var cid: Int = 2
        private var cname: String = ""
        private var did: Int = 3
        private var dname: String = ""

        private val layout: View
        private val dialog: LocationDialog = LocationDialog(context, R.style.CustomDialog)

        private val locaService = ServiceCreator.create<LocaService>()

        // 计算启动了几次 以此来推断当前显示窗口应该为什么
        private var locaCount: Int = 0

        init {
            //这里传入自定义的style，直接影响此Dialog的显示效果。style具体实现见style.xml
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            layout = inflater.inflate(R.layout.dialog_location, null)
            dialog.addContentView(layout, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        }

        fun setTitle(title: String): Builder{
            this.title = title
            return this
        }

        fun setListener(listener: PriorityListener): Builder {
            this.callBackListener = listener
            return this
        }

//        fun setContentView(v: View): Builder {
//            this.contentView = v
//            return this
//        }

//        fun setSingleButton(singleButtonText: String, listener: View.OnClickListener): Builder {
//            this.singleButtonText = singleButtonText
//            this.singleButtonClickListener = listener
//            return this
//        }

        /**
         * 从外部打开对话框进行对话框的初始化
         * @return
         */
        fun createDialog(): LocationDialog {
            locaCount = 0

            // 初始化省地区列表，设置recyclerView
            initProvinceLocaList()

            (layout.findViewById<View>(R.id.LocaDialogTitle) as TextView).text = "请选择省"

            dialog.setContentView(layout)
            dialog.setCancelable(true)     //用户可以点击手机Back键取消对话框显示
            dialog.setCanceledOnTouchOutside(true)        //用户不能通过点击对话框之外的地方取消对话框显示
            return dialog
        }


        fun updateDialog(id: Int, name: String) {
            when(locaCount){
                0 -> {
                    pid = id
                    pname = name
                    (layout.findViewById<View>(R.id.LocaDialogTitle) as TextView).text = "请选择市"
                    initCityLocaList(pid)
                    updateLocaList()
                }
                1 -> {
                    cid = id
                    cname = name
                    (layout.findViewById<View>(R.id.LocaDialogTitle) as TextView).text = "请选择区"
                    initDistrictLocaList(cid)
                    updateLocaList()
                }
                2 -> {
                    did = id
                    dname = name
                    callBackListener?.setActivityText("$pname $cname $dname", did)
                    dialog.dismiss()
                }
            }
            locaCount ++;
        }

        private fun initProvinceLocaList(){
            locaService.getProvinceList().enqueue(object : Callback<List<LocaList>> {
                override fun onResponse(call: Call<List<LocaList>>,
                                        response: Response<List<LocaList>>
                ) {
                    locaList = response.body()
                    updateLocaList()
                }
                override fun onFailure(call: Call<List<LocaList>>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("LoginActivity", "network failed")
                }
            })
        }

        private fun initCityLocaList(pid: Int){
            locaService.getCityList(pid).enqueue(object : Callback<List<LocaList>> {
                override fun onResponse(call: Call<List<LocaList>>,
                                        response: Response<List<LocaList>>
                ) {
                    locaList = response.body()
                    updateLocaList()
                }
                override fun onFailure(call: Call<List<LocaList>>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("LoginActivity", "network failed")
                }
            })
        }

        private fun initDistrictLocaList(did: Int){
            locaService.getDistrictList(did).enqueue(object : Callback<List<LocaList>> {
                override fun onResponse(call: Call<List<LocaList>>,
                                        response: Response<List<LocaList>>
                ) {
                    locaList = response.body()
                    updateLocaList()
                }
                override fun onFailure(call: Call<List<LocaList>>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("LoginActivity", "network failed")
                }
            })
        }

        private fun updateLocaList(){
            val layoutManager = LinearLayoutManager(layout.context) //线性布局布局管理器
            val recyclerView:RecyclerView = layout.findViewById(R.id.LocaDialogList)
            recyclerView.layoutManager = layoutManager
            val adapter = locaList?.let { LocaListAdapter(it, this) }
            recyclerView.adapter = adapter
        }
    }
}

// 省份名字 pid 主键 fid 外键
data class LocaList(var name:String, var id:Int)

class LocaListAdapter(private val locaList: List<LocaList>, private var builderForDialog: LocationDialog.Builder) : RecyclerView.Adapter<LocaListAdapter.ViewHolder>() {
    //自定义嵌套内部类 ViewHolder 来减少 findViewById() 的使用， 继承RecyclerView的ViewHolder
    //通过图片的id获取对应的视图，以便后续操作
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val locaDialogText: TextView = view.findViewById(R.id.LocaDialogText)
        val locaDialogLayout: LinearLayout = view.findViewById(R.id.LocaDialogLayout)
        val context: Context = view.context
    }

    //设置初始的布局
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dialog_location_item, parent, false)
        //第一个参数为单个图片对应的布局文件，第二个参数为RecyclerView要显示的位置
        //第三个参数设置为false效果为你在xml中设置为什么具体显示就为什么
        return ViewHolder(view)
    }       //加载布局

    //设置初次加载、滑动时的布局
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = locaList[position] //获取当前位置对应的loca
        holder.locaDialogText.text = location.name
        holder.locaDialogLayout.setOnClickListener {
            builderForDialog.updateDialog(location.id, location.name)
        }
    }

    //获取列表中的项目个数，将其定义为数组的个数
    override fun getItemCount() = locaList.size
}

interface LocaService {
    @GET("/addr/getProvinceList")
    fun getProvinceList(): Call<List<LocaList>>
    @GET("/addr/getCityList")
    fun getCityList(@Query("pid") id: Int): Call<List<LocaList>>
    @GET("/addr/getDistrictList")
    fun getDistrictList(@Query("cid") id: Int): Call<List<LocaList>>
}

//                    if (locaList != null) {
//                        for (loca in locaList!!) {
//                            Log.d("LocaActivity", "id is ${loca.id}")
//                            Log.d("LocaActivity", "name is ${loca.name}")
//                        }
//                    }