package com.huaqiyun.tvlib.card.presenter

import android.view.View
import android.view.ViewGroup
import android.widget.Checkable
import androidx.leanback.widget.Presenter
import com.huaqiyun.tvlib.card.viewholder.BaseViewHolder
import com.huaqiyun.tvlib.card.viewholder.ViewHolderFactory
import com.huaqiyun.tvlib.widget.BaseCustomCardView
import com.huaqiyun.tvlib.widget.ICheckView
import java.lang.RuntimeException

/**
 * 通过这个类可以创建不同类型的ViewHolder,暂时不知道取什么名字。就这样叫吧 超级强大的Presenter
 * */
class SupperPresenter(factory:ViewHolderFactory): Presenter() {

    private val viewHolderFactory: ViewHolderFactory = factory

    var viewSelectListener:OnViewSelectListener? = null
    var viewCheckedListener:OnViewCheckedListener? = null
    var viewClickListener:OnViewClickListener? = null

    /**
     * Creates a new [View].
     */
    override fun onCreateViewHolder(parent: ViewGroup): BaseViewHolder {
        return viewHolderFactory.createViewHolder(parent)
    }

    /**
     * Binds a [View] to an item.
     */
    override fun onBindViewHolder(viewHolder: ViewHolder?, item: Any?) {
       if(viewHolder is BaseViewHolder){
           val view = viewHolder.view
           viewHolder.view.setOnClickListener { v ->
               if(viewClickListener != null){
                   viewClickListener?.onViewClick(v,item)
               }
           }
           if(viewHolder.view is BaseCustomCardView){
               view as BaseCustomCardView
               view.setOnViewSelectChangeListener(object : BaseCustomCardView.OnViewSelectChangeListener() {
                   override fun onViewSelect(select: Boolean) {
                       if(viewSelectListener != null){
                           viewSelectListener?.onViewSelect(select,item)
                       }
                   }
               })
               view.setOnCheckedChangeListener(ICheckView.OnCheckedChangeListener { checkable, isChecked ->
                   if(viewCheckedListener != null){
                       viewCheckedListener?.onViewChecked(isChecked,item)
                   }
               })
           }
           viewHolder.onBindViewHolder(viewHolder,item)

       }else{
           throw RuntimeException("not support viewHolder is not child of BaseViewHolder")
       }
    }

    /**
     * Unbinds a [View] from an item. Any expensive references may be
     * released here, and any fields that are not bound for every item should be
     * cleared here.
     */
    override fun onUnbindViewHolder(viewHolder: ViewHolder?) {
        if(viewHolder is BaseViewHolder){
            viewHolder.onUnbindViewHolder(viewHolder)
            val view = viewHolder.view
            if(viewHolder.view is BaseCustomCardView){
                view as BaseCustomCardView
                view.setOnViewSelectChangeListener(null)
            }
        }else{
            throw RuntimeException("not support viewHolder is not child of BaseViewHolder")
        }
    }
}

interface OnViewSelectListener{
    fun onViewSelect(select:Boolean, item: Any?)
}
interface OnViewCheckedListener{
    fun onViewChecked(checked:Boolean, item: Any?)
}
interface OnViewClickListener{
    fun onViewClick(view:View?, item: Any?)
}