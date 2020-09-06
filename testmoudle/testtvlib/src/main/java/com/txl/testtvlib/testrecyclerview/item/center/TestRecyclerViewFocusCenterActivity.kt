package com.txl.testtvlib.testrecyclerview.item.center

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.alibaba.android.vlayout.layout.SingleLayoutHelper
import com.txl.testtvlib.R
import com.txl.testtvlib.basic.adapter.BaseVLayoutAdapter
import com.txl.testtvlib.testrecyclerview.item.center.viewholder.*
import com.txl.ui_basic.adapter.BaseRecyclerFactoryAdapter
import com.txl.ui_basic.viewholder.BaseViewHolder
import com.txl.ui_basic.viewholder.IViewHolderFactory
import kotlinx.android.synthetic.main.activity_test_recycler_view_focus_center.*


/**
 * 测试让RecyclerView焦点元素始终居中  因为涉及到多个数据变化
 * 1.正常列表中元素居中
 * 2.正常容器嵌套焦点居中，嵌套的RecyclerView高度不超过最外层的LibTvRecyclerView2   不能直接嵌套RecyclerView因为按键的处理会导致内部的RecyclerView焦点无法正常查找
 * 3.正常容器嵌套焦点居中，嵌套的LibTvRecyclerView2高度不超过最外层的LibTvRecyclerView2
 * 4.容器嵌套焦点居中，嵌套的RecyclerView高度超过最外层的LibTvRecyclerView2  不能直接嵌套RecyclerView因为按键的处理会导致内部的RecyclerView焦点无法正常查找
 * 5.容器嵌套焦点居中，嵌套的LibTvRecyclerView2高度超过最外层的LibTvRecyclerView2
 * */
class TestRecyclerViewFocusCenterActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
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

        //正常列表元素
        val layoutHelper = GridLayoutHelper(4)
        layoutHelper.setAutoExpand(false)
        layoutHelper.setGap(resources.getDimensionPixelSize(R.dimen.dp_5))
        val adapter1 = BaseVLayoutAdapter<String,ContentViewHolder>(object :IViewHolderFactory<ContentViewHolder>{
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
                return ContentViewHolder.onCreateViewHolder(parent)
            }

            override fun <T : Any?> getItemViewType(position: Int, data: T): Int {
                return 0
            }
        },layoutHelper)
        for (i in 0..20){
            adapter1.appendData("$i")
        }

        //嵌套RecyclerView  Grid
        val singleLayoutHelper = SingleLayoutHelper()
        singleLayoutHelper.marginTop = resources.getDimensionPixelSize(R.dimen.dp_5)
        val adapter2 = BaseVLayoutAdapter<String,RecyclerViewViewHolder>(object :IViewHolderFactory<RecyclerViewViewHolder>{
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewViewHolder {
                return RecyclerViewViewHolder.onCreateViewHolder(parent)
            }

            override fun <T : Any?> getItemViewType(position: Int, data: T): Int {
                return 0
            }
        },singleLayoutHelper)
        adapter2.appendData("我是第二个")

        //嵌套RecyclerView  Grid
        val singleLayoutHelper2 = SingleLayoutHelper()
        singleLayoutHelper.marginTop = resources.getDimensionPixelSize(R.dimen.dp_5)
        val adapter3 = BaseVLayoutAdapter<String,RecyclerViewHorizontalViewHolder>(object :IViewHolderFactory<RecyclerViewHorizontalViewHolder>{
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHorizontalViewHolder {
                return RecyclerViewHorizontalViewHolder.onCreateViewHolder(parent)
            }

            override fun <T : Any?> getItemViewType(position: Int, data: T): Int {
                return 0
            }
        },singleLayoutHelper2)
        adapter3.appendData("我是第三个")

        recycler_title.adapter=  titleAdapter
        titleAdapter.appendData("正常列表中元素居中")
        titleAdapter.appendData("嵌套高度较小RecyclerView")
        titleAdapter.appendData("嵌套高度较小LibTvRecyclerView2")
        titleAdapter.appendData("嵌套高度较大RecyclerView")
        titleAdapter.appendData("嵌套高度较大LibTvRecyclerView2")
        recycler_title.visibility = View.GONE
        delegateAdapter.addAdapter(adapter1)
        delegateAdapter.addAdapter(adapter3)
        delegateAdapter.addAdapter(adapter2)
//        recycler_title.setOnCheckedChangeListener { group, checkedView, position ->
//            Log.d(TAG,"选中 position $position")
//            if(position == 0){
//                delegateAdapter.clear()
//                delegateAdapter.addAdapter(adapter1)
//            }else if(position == 1){//直接嵌套系统的RecyclerView,
//                delegateAdapter.clear()
//                delegateAdapter.addAdapter(adapter1)
//                delegateAdapter.addAdapter(adapter3)
//                delegateAdapter.addAdapter(adapter2)
////                delegateAdapter.addAdapter(adapter1)
//            }
//        }
    }
}