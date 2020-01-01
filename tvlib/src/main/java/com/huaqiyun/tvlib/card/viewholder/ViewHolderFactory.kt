package com.huaqiyun.tvlib.card.viewholder

import android.view.ViewGroup

interface ViewHolderFactory {
    fun createViewHolder(parent: ViewGroup):BaseViewHolder
}