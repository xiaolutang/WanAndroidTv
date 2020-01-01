package com.huaqiyun.tvlib.card.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide
import com.huaqiyun.tvlib.R
import com.huaqiyun.tvlib.card.mode.ImageTextCard
import com.huaqiyun.tvlib.widget.BaseCustomCardView

open class ImageTextCardViewHolder(view: View) : BaseViewHolder(view) {
    companion object{
        fun createViewHolder(parent: ViewGroup):ImageTextCardViewHolder{
            val root = LayoutInflater.from(parent.context).inflate(R.layout.lib_image_text_card,parent,false)
            return ImageTextCardViewHolder(root)
        }
    }

    val tvTitle: TextView = view.findViewById(R.id.tv_image_text_card_title)
    val image:ImageView = view.findViewById(R.id.image_image_text_card_logo)
    val customCardView: BaseCustomCardView = view.findViewById(R.id.image_text_card_root)

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder?, data: Any?) {
        if(data is ImageTextCard){
            tvTitle.text = data.title
            Glide.with(customCardView).load(data.imageUrl).into(image)
        }
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder?) {

    }
}