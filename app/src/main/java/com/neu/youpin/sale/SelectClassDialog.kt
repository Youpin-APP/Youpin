package com.neu.youpin.sale

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neu.youpin.R
import com.neu.youpin.entity.ServiceCreator
import com.neu.youpin.location.LocaList
import com.neu.youpin.location.LocaService
import com.neu.youpin.location.LocaUpdateMap
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*

class SelectClassDialog: Dialog {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, theme: Int) : super(context, theme) {}

    /**
     * 自定义Dialog监听器
     */
    interface SaleListener {
        /**
         * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
         */
        fun setActivityText(className: String, classId: Int)
    }

    class Builder(context: Context) {
        private var title: String? = null
        private var callBackListener: SaleListener? = null

        private var classList: List<TList>? = null

        private val layout: View
        private val dialog: SelectClassDialog = SelectClassDialog(context, R.style.CustomDialog)
        private var classType:Int = -1

        private var gid:Int = -1

        private var mContext = context

        private val selectClassService = ServiceCreator.create<SelectClassService>()

        init {
            //这里传入自定义的style，直接影响此Dialog的显示效果。style具体实现见style.xml
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            layout = inflater.inflate(R.layout.dialog_class, null)
            dialog.addContentView(layout, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        }

        fun setTitle(title: String): Builder{
            this.title = title
            return this
        }

        fun setType(type: Int, _gid: Int): Builder{
            this.classType = type
            this.gid = _gid
            return this
        }

        fun setListener(listener: SaleListener): Builder {
            this.callBackListener = listener
            return this
        }

        /**
         * 从外部打开对话框进行对话框的初始化
         * @return
         */
        fun createDialog(): SelectClassDialog {
            // 设置recyclerView
            initClassList()

            (layout.findViewById<View>(R.id.SaleDialogText) as EditText).setText("")

            if(title != null) (layout.findViewById<View>(R.id.SaleDialoTitle) as TextView).text = title

            (layout.findViewById<View>(R.id.SaleDialogAddLayout) as LinearLayout).visibility = View.VISIBLE

            (layout.findViewById<View>(R.id.SaleDialogConfirm) as Button).setOnClickListener {
                if((layout.findViewById<View>(R.id.SaleDialogText) as EditText).text.isEmpty()){
                    Toast.makeText(mContext,"新建分类为空！！",Toast.LENGTH_SHORT).show()
                }else addByRetrofit()
            }

            dialog.setContentView(layout)
            dialog.setCancelable(true)     //用户可以点击手机Back键取消对话框显示
            dialog.setCanceledOnTouchOutside(true)        //用户不能通过点击对话框之外的地方取消对话框显示
            return dialog
        }

        /**
         * 从外部打开对话框进行对话框的初始化
         * @return
         */
        fun createGidDialog(list: List<TList>): SelectClassDialog {
            classList = list
            // 设置recyclerView
            initRecycleView()

            (layout.findViewById<View>(R.id.SaleDialogText) as EditText).setText("")

            if(title != null) (layout.findViewById<View>(R.id.SaleDialoTitle) as TextView).text = title

            (layout.findViewById<View>(R.id.SaleDialogAddLayout) as LinearLayout).visibility = View.GONE

            dialog.setContentView(layout)
            dialog.setCancelable(true)     //用户可以点击手机Back键取消对话框显示
            dialog.setCanceledOnTouchOutside(true)        //用户不能通过点击对话框之外的地方取消对话框显示
            return dialog
        }


        fun updateDialog(className: String, classId: Int) {
            callBackListener?.setActivityText(className, classId)
            dialog.dismiss()
        }

        private fun initRecycleView(){
            val layoutManager = LinearLayoutManager(layout.context) //线性布局布局管理器
            val recyclerView:RecyclerView = layout.findViewById(R.id.SaleDialoList)
            recyclerView.layoutManager = layoutManager
            val adapter = classList?.let { LocaListAdapter(it, this) }
            recyclerView.adapter = adapter
        }

        private fun addError(){
            Toast.makeText(mContext,"新建分类失败！！",Toast.LENGTH_SHORT).show()
        }

        private fun addByRetrofit(){
            (layout.findViewById<View>(R.id.SaleDialogConfirm) as Button).isClickable = false
            var tName = arrayOf("", "", "")
            tName[classType] = (layout.findViewById<View>(R.id.SaleDialogText) as EditText).text.toString()
            selectClassService.addGoodsType(this.gid, tName[0], tName[1], tName[2]).enqueue(object : Callback<SelectClassMap> {
                override fun onResponse(call: Call<SelectClassMap>,
                                        response: Response<SelectClassMap>
                ) {
                    val list = response.body()
                    if (list != null && list.success) {
                        updateDialog((layout.findViewById<View>(R.id.SaleDialogText) as TextView).text.toString(), list.tid1)
                    }else addError()
                    (layout.findViewById<View>(R.id.SaleDialogConfirm) as Button).isClickable = true
                    initRecycleView()
                }
                override fun onFailure(call: Call<SelectClassMap>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("LoginActivity", "network failed")
                    (layout.findViewById<View>(R.id.SaleDialogConfirm) as Button).isClickable = true
                }
            })
        }

        private fun initClassList(){
            selectClassService.getTypeList(this.gid).enqueue(object : Callback<T3List> {
                override fun onResponse(call: Call<T3List>,
                                        response: Response<T3List>
                ) {
                    val list = response.body()
                    if (list != null && list.success) {
                        when(classType){
                            0 -> classList = list.tid1
                            1 -> classList = list.tid2
                            2 -> classList = list.tid3
                        }
                    }
                    initRecycleView()
                }
                override fun onFailure(call: Call<T3List>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("LoginActivity", "network failed")
                }
            })
        }
    }
}

// 分类 tname 名字 tid 主键
data class TList(var tname:String, var tid:Int)

data class T3List(var success:Boolean, var tid1:List<TList>, var tid2:List<TList>, var tid3:List<TList>)

class SelectClassMap(val success: Boolean, val tid1: Int)

interface SelectClassService {
    @GET("/goods/getTypeList")
    fun getTypeList(@Query("gid") gid: Int): Call<T3List>

    @FormUrlEncoded
    @POST("/goodsEdit/addGoodsType")
    fun addGoodsType(@Field("gid") gid: Int, @Field("tname1") tname1: String,
                     @Field("tname2") tname2: String, @Field("tname3") tname3: String): Call<SelectClassMap>
}

class LocaListAdapter(private val classList: List<TList>, private var builderForDialog: SelectClassDialog.Builder) : RecyclerView.Adapter<LocaListAdapter.ViewHolder>() {
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
        val goodClass = classList[position] //获取当前位置对应的loca
        holder.locaDialogText.text = goodClass.tname
        holder.locaDialogLayout.setOnClickListener {
            builderForDialog.updateDialog(goodClass.tname, goodClass.tid)
        }
    }

    //获取列表中的项目个数，将其定义为数组的个数
    override fun getItemCount() = classList.size
}