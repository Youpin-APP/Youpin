package com.neu.youpin.saleUpload

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.neu.youpin.Interface.OnItemClickListener
import com.neu.youpin.R
import java.util.*


class SaleUploadDetailListAdapter(
    private var imgList: Vector<ImgItem>,
    private val context: Context
) :

    RecyclerView.Adapter<SaleUploadDetailListAdapter.ViewHolder>() {
    lateinit var listener: OnItemClickListener

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val detailImg: ImageView = view.findViewById(R.id.detailImg)
        val selectButton: Button = view.findViewById(R.id.detailImgSelect)
        val moveUpButton: Button = view.findViewById(R.id.detailImgMoveUp)
        val moveDownButton: Button = view.findViewById(R.id.detailImgMoveDown)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.detail_img_edit, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (!imgList[position].loaded) {
            imgList[position].loaded = true
            Glide.with(context)
                .load(imgList[position].picUrl)
                .into(holder.detailImg)
        }
        holder.selectButton.setOnClickListener {
            if (listener != null) {
                listener.onItemClick(position, holder.selectButton.id)
            }
        }
        holder.moveUpButton.setOnClickListener {
            if (listener != null) {
                listener.onItemClick(position, holder.moveUpButton.id)
            }
        }
        holder.moveDownButton.setOnClickListener {
            if (listener != null) {
                listener.onItemClick(position, holder.moveDownButton.id)
            }
        }
    }

    public fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun getItemCount() = imgList.size
}