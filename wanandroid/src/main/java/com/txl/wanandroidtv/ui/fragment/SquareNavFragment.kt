package com.txl.wanandroidtv.ui.fragment

import android.os.Bundle
import com.txl.ui_basic.adapter.BaseRecyclerFactoryAdapter
import com.txl.wan_android_data_provider.viewModel.*
import com.txl.wan_android_data_provider.viewModel.ViewModelContainer.putViewModelClass
import com.txl.wan_android_data_provider.viewModel.ViewModelContainer.putViewModelFactory
import com.txl.wanandroidtv.R

/**
 * wan android 首页导航
 * */
class SquareNavFragment : BaseNavFragment(), BaseRecyclerFactoryAdapter.OnItemClickListener {

    companion object{
        init {
            //初始化factory
            putViewModelFactory(CATEGORY_SQUARE, SquareViewModelFactory())

            //初始化class
            putViewModelClass(CATEGORY_SQUARE, SquareNavItemListViewModel::class.java)
        }

        fun newInstance(category:String):SquareNavFragment{
            val fragment = SquareNavFragment()
            val bundle = Bundle()
            bundle.putString(CATEGORY,category)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun showLoading(currentPage: Int) {

    }

    override fun onDataReady(currentPage: Int, data: Any?) {

    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_lib_nav_base
    }

    override fun onItemClick(position: Int, data: Any?) {

    }
}