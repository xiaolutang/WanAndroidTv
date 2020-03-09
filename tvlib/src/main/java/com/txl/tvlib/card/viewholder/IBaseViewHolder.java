package com.txl.tvlib.card.viewholder;

import androidx.leanback.widget.Presenter;

public interface IBaseViewHolder {
    void onBindViewHolder(Presenter.ViewHolder viewHolder, Object data);
    void onUnbindViewHolder(Presenter.ViewHolder viewHolder);
}
