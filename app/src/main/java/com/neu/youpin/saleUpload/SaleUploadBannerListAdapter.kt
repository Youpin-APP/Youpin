package com.neu.youpin.saleUpload

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.neu.youpin.Interface.OnItemClickListener
import com.neu.youpin.R
import java.util.*

class SaleUploadBannerListAdapter(
    private var imgList: Vector<ImgItem>,
    private val context: Context
) :
    RecyclerView.Adapter<SaleUploadBannerListAdapter.ViewHolder>() {
    lateinit var listener: OnItemClickListener

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bannerImg: ImageView = view.findViewById(R.id.bannerImg)
        val selectButton: CheckBox = view.findViewById(R.id.bannerImgSelect)
        val moveLeftButton: Button = view.findViewById(R.id.bannerImgMoveLeft)
        val moveRightButton: Button = view.findViewById(R.id.bannerImgMoveRight)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.banner_img_edit, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("banner", "imgList[position].picUrl")
        if (!imgList[position].loaded) {
            imgList[position].loaded = true
            Log.d("banner", "imgList[position].picUrl")
            Glide.with(context)
                .load(imgList[position].picUrl)
                .into(holder.bannerImg)
        }
        holder.selectButton.setOnClickListener {
            if (listener != null) {
                listener.onItemClick(position, holder.selectButton.id)
            }
        }
        holder.moveLeftButton.setOnClickListener {
            if (listener != null) {
                listener.onItemClick(position, holder.moveLeftButton.id)
            }
        }
        holder.moveRightButton.setOnClickListener {
            if (listener != null) {
                listener.onItemClick(position, holder.moveRightButton.id)
            }
        }
    }

    public fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun getItemCount() = imgList.size
}