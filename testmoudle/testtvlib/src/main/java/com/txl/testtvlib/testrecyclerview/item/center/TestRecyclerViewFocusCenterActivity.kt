package com.txl.testtvlib.testrecyclerview.item.center

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.txl.testtvlib.R
import com.txl.testtvlib.testrecyclerview.item.center.viewholder.ContentViewHolder
import com.txl.testtvlib.testrecyclerview.item.center.viewholder.TitleViewHolder
import com.txl.ui_basic.adapter.BaseRecyclerFactoryAdapter
import com.txl.ui_basic.viewholder.BaseViewHolder
import com.txl.ui_basic.viewholder.IViewHolderFactory
import kotlinx.android.synthetic.main.activity_test_recycler_view_focus_center.*


/**
 * 测试让RecyclerView焦点元素始终居中  因为涉及到多个数据变化
 * 1.正常列表中元素居中
 * 2.正常容器嵌套焦点居中，嵌套的RecyclerView高度不超过最外层的LibTvRecyclerView2
 * 3.正常容器嵌套焦点居中，嵌套的LibTvRecyclerView2高度不超过最外层的LibTvRecyclerView2
 * 4.容器嵌套焦点居中，嵌套的RecyclerView高度超过最外层的LibTvRecyclerView2
 * 5.容器嵌套焦点居中，嵌套的LibTvRecyclerView2高度超过最外层的LibTvRecyclerView2
 * */
class TestRecyclerViewFocusCenterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_recycler_view_focus_center)
        initView()
    }

    private fun initView(){
        recycler_title.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        val titleAdapter = BaseRecyclerFactoryAdapter<String>(object:IViewHolderFactory<BaseViewHolder>{
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
                return TitleViewHolder.onCreateViewHolder(parent)
            }

            override fun <T : Any?> getItemViewType(position: Int, data: T): Int {
                return 0
            }
        })

        val layoutManager = VirtualLayoutManager(this)
        recycler_content.layoutManager = layoutManager
        val delegateAdapter = DelegateAdapter(layoutManager, false)
        recycler_content.adapter = delegateAdapter

        val adapter1 = BaseRecyclerFactoryAdapter<String>(object :IViewHolderFactory<BaseViewHolder>{
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
                return ContentViewHolder.onCreateViewHolder(parent)
            }

            override fun <T : Any?> getItemViewType(position: Int, data: T): Int {
                return 0
            }
        })
        for (i in 0..20){
            adapter1.appendData("$i")
        }

        recycler_title.adapter=  titleAdapter
        titleAdapter.appendData("正常列表中元素居中")
        titleAdapter.appendData("嵌套高度较小RecyclerView")
        titleAdapter.appendData("嵌套高度较小LibTvRecyclerView2")
        titleAdapter.appendData("嵌套高度较大RecyclerView")
        titleAdapter.appendData("嵌套高度较大LibTvRecyclerView2")
        recycler_title.setOnCheckedChangeListener { group, checkedView, position ->
            if(position == 0){

            }
        }
    }
}