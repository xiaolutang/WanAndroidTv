package com.txl.tvlib.card.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.leanback.widget.Presenter
import com.txl.tvlib.R
import com.txl.tvlib.card.mode.TextCard
import com.txl.tvlib.widget.BaseCustomCardView

open class TextCardViewHolder(view: View) : BaseViewHolder(view) {
    val tvTitle: TextView = view.findViewById(R.id.tv_text_card_title)
    val customCardView: BaseCustomCardView = view.findViewById(R.id.root_text_card)

    companion object{
        fun createViewHolder(parent: ViewGroup):TextCardViewHolder{
            val root = LayoutInflater.from(parent.context).inflate(R.layout.lib_text_card,parent,false)
            return TextCardViewHolder(root)
        }
    }
    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder?, data: Any?) {
        //RecyclerView重用 ViewHolder的时候会将选中状态一起重用 下面的代码简单处理ViewHolder的重用问题，可能还会有bug 。
        // 考虑能不能通过RecyclerView的重用池相关的来处理
        customCardView.isSelected = false
        customCardView.isChecked = false

        if(data is TextCard){
            tvTitle.text = data.title
        }
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder?) {

    }
}