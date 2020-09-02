package com.txl.ui_basic.adapter;

import android.content.Context;

import androidx.collection.CircularArray;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 */

public abstract class BaseRecyclerAdapter<T,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected ArrayList<T> data=new ArrayList<>();
    protected WeakReference<Context> contextRef;
    public BaseRecyclerAdapter(Context context)
    {
        contextRef=new WeakReference<>(context);
    }

    public BaseRecyclerAdapter(Collection<T> data, Context context)
    {
        contextRef=new WeakReference<>(context);
        this.data.addAll(data);
    }

    public final Context getContext()
    {
        return contextRef.get();
    }

    public ArrayList<T> getData() {
        return data;
    }



    public void appendAllFirst(List<T> data){
        Collections.reverse(data);
        for(T item:data){
            this.data.add(0,item);
        }
        notifyDataSetChanged();
    }

    public void setData(Collection<T> data) {
        if(data!=null)
        {
            this.data.clear();
            this.data.addAll(data);
            this.notifyDataSetChanged();
        }
    }

    public void appendData(T data)
    {
        this.data.add(data);
        this.notifyDataSetChanged();
    }
    public void appendData(Collection<T> data)
    {
        int originSize = data.size();
        this.data.addAll(data);
        notifyItemRangeInserted(originSize,data.size());
    }

    @Override
    public int getItemCount()
    {
        if(data!=null)
            return data.size();
        return 0;
    }

    public T getItem(int position)
    {
        if(data!=null&&getItemCount()>0&&position<getItemCount()){
            return data.get(position);
        }
        return null;
    }
}
