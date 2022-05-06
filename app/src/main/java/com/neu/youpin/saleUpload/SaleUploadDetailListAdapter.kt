package com.neu.youpin.saleUpload

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.neu.youpin.R
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class SaleUploadDetailListAdapter(private var imgList: Vector<ImgItem>) :

    RecyclerView.Adapter<SaleUploadDetailListAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val detailImg : ImageView = view.findViewById(R.id.detailImg)
        val selectButton : RadioButton = view.findViewById(R.id.detailImgSelect)
        val moveUpButton : Button = view.findViewById(R.id.detailImgMoveUp)
        val moveDownButton : Button = view.findViewById(R.id.detailImgMoveDown)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.detail_img_edit, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.detailImg.setImageBitmap(getBitmap(imgList[position].picUrl));
    }

    @Throws(IOException::class)
    fun getBitmap(path: String?): Bitmap? {
        try {
            val url = URL(path)
            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 5000
            conn.requestMethod = "GET"
            if (conn.responseCode === 200) {
                val inputStream: InputStream = conn.inputStream
                return BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    override fun getItemCount() = imgList.size
}