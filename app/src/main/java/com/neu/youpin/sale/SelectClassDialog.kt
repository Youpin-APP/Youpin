package com.neu.youpin.sale

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neu.youpin.R

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

        private val classList = ArrayList<ClassList>()

        private val layout: View
        private val dialog: SelectClassDialog = SelectClassDialog(context, R.style.CustomDialog)

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
            initLocaList()
            val layoutManager = LinearLayoutManager(layout.context) //线性布局布局管理器
            val recyclerView:RecyclerView = layout.findViewById(R.id.LocaDialogList)
            recyclerView.layoutManager = layoutManager
            val adapter = LocaListAdapter(classList, this)
            recyclerView.adapter = adapter

            if(title != null) (layout.findViewById<View>(R.id.LocaDialogTitle) as TextView).text = title

            dialog.setContentView(layout)
            dialog.setCancelable(true)     //用户可以点击手机Back键取消对话框显示
            dialog.setCanceledOnTouchOutside(true)        //用户不能通过点击对话框之外的地方取消对话框显示
            return dialog
        }


        fun updateDialog(className: String, classId: Int) {
            callBackListener?.setActivityText(className, classId)
            dialog.dismiss()
        }

        private fun initLocaList(){
            classList.clear()
            classList.add(ClassList("和平区",210102))
            classList.add(ClassList("沈河区",210103))
            classList.add(ClassList("大东区",210104))
            classList.add(ClassList("皇姑区",210105))
            classList.add(ClassList("铁西区",210106))
            classList.add(ClassList("苏家屯",210111))
            classList.add(ClassList("浑南区",210112))
            classList.add(ClassList("沈北新区",210113))
            classList.add(ClassList("于洪区",210114))
        }
    }
}

// 省份名字 pid 主键 fid 外键
data class ClassList(var name:String, var pid:Int)

class LocaListAdapter(private val classList: List<ClassList>, private var builderForDialog: SelectClassDialog.Builder) : RecyclerView.Adapter<LocaListAdapter.ViewHolder>() {
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
        holder.locaDialogText.text = goodClass.name
        holder.locaDialogLayout.setOnClickListener {
            builderForDialog.updateDialog(goodClass.name, goodClass.pid)
        }
    }

    //获取列表中的项目个数，将其定义为数组的个数
    override fun getItemCount() = classList.size
}