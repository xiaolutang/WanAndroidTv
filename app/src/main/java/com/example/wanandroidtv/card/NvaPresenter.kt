package com.example.wanandroidtv.card

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.leanback.widget.Presenter
import com.example.wanandroidtv.R
import com.txl.tvlib.card.IBaseViewHolder
import com.txl.tvlib.card.TextCard
import com.txl.tvlib.widget.BaseCustomCardView

class NvaPresenter: Presenter() {
    /**
     * Creates a new [View].
     */
    override fun onCreateViewHolder(parent: ViewGroup): NavViewHolder {
        return NavViewHolder.createViewHolder(parent)
    }

    /**
     * Binds a [View] to an item.
     */
    override fun onBindViewHolder(viewHolder: ViewHolder?, item: Any?) {
       if(viewHolder is IBaseViewHolder){
           viewHolder.onBindViewHolder(viewHolder,item)
       }
    }

    /**
     * Unbinds a [View] from an item. Any expensive references may be
     * released here, and any fields that are not bound for every item should be
     * cleared here.
     */
    override fun onUnbindViewHolder(viewHolder: ViewHolder?) {
        if(viewHolder is IBaseViewHolder){
            viewHolder.onUnbindViewHolder(viewHolder)
        }
    }
}

class NavViewHolder(view: View) : Presenter.ViewHolder(view) ,IBaseViewHolder{
    private val _baseCustomCardView = view as BaseCustomCardView
    private val _tvTitle = view.findViewById<TextView>(R.id.tv_vav_title)

    companion object{
        fun createViewHolder(parent: ViewGroup):NavViewHolder{
            val root = LayoutInflater.from(parent.context).inflate(R.layout.card_nav_title_bg,parent,false)
            return NavViewHolder(root)
        }
    }
    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder?, data: Any?) {
        if(data is TextCard){
            _tvTitle.text = data.title
        }
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder?) {

    }

}