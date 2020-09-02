package com.txl.testtvlib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Checkable
import android.widget.RadioButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.txl.tvlib.widget.CardFrameLayout
import com.txl.tvlib.widget.ICheckView
import com.txl.ui_basic.adapter.BaseRecyclerFactoryAdapter
import com.txl.ui_basic.viewholder.BaseViewHolder
import com.txl.ui_basic.viewholder.IViewHolderFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView(){
        tv_recycler_view.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        val adapter = BaseRecyclerFactoryAdapter<String>(this,object :IViewHolderFactory<BaseViewHolder>{
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
                return TestViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_test_librecycler_radiogroup,parent,false))
            }

            override fun <T : Any?> getItemViewType(position: Int, data: T): Int {
                return 0
            }
        })
        for (i in 0..15){
            adapter.appendData("我是$i")
        }
        tv_recycler_view.adapter = adapter
    }

    class TestViewHolder(itemView: View):BaseViewHolder(itemView){
        private val button = itemView.findViewById<RadioButton>(R.id.rb_item)

        init {
            if(itemView is CardFrameLayout){
                itemView.setOnCheckedChangeListener(ICheckView.OnCheckedChangeListener { checkable, isChecked ->
                    itemView.postDelayed(object :Runnable{
                        override fun run() {
                            Log.d("TestViewHolder","$checkable is checked $isChecked  isSelect ${itemView.isSelected}")
                        }
                    },500)
                })
            }
        }
        override fun <T : Any?> onBindViewHolder(position: Int, data: T) {
            super.onBindViewHolder(position, data)
            if(data is String){
                button.text = data
            }
        }
    }
}
