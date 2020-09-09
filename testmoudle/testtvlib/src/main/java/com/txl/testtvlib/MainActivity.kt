package com.txl.testtvlib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Checkable
import android.widget.ImageView
import android.widget.RadioButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.txl.tvlib.widget.CardFrameLayout
import com.txl.tvlib.widget.ICheckView
import com.txl.tvlib.widget.dynamic.focus.LibTvRecyclerView2
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
        tv_recycler_view.setOnCheckedChangeListener { group, checkedView, position -> Log.d("TestViewHolder","checked position is $position") }
        val adapter = BaseRecyclerFactoryAdapter<String>(object :IViewHolderFactory<BaseViewHolder>{
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
                if(viewType == 0){
                    return TestViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_test_librecycler_radiogroup,parent,false))
                }else if(viewType == 2){
                    return TestImage2ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image2_librecycler_radiogroup,parent,false))
                }else{
                    return TestImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image_librecycler_radiogroup,parent,false))
                }

            }

            override fun <T : Any?> getItemViewType(position: Int, data: T): Int {
                if(position == 1){
                    return 1
                }else if(position == 3){
                    return 2
                }
                return 0
            }
        })
        for (i in 0..15){
            adapter.appendData("我是$i")
        }
        tv_recycler_view.adapter = adapter
//        tv_recycler_view.postDelayed({},1500)tv_recycler_view.setCheckedPosition(5)
        tv_recycler_view.setCheckedPosition(5)
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

    class TestImageViewHolder(itemView: View):BaseViewHolder(itemView){
        init {
            if(itemView is CardFrameLayout){
                itemView.setOnCheckedChangeListener(ICheckView.OnCheckedChangeListener { checkable, isChecked ->
                    itemView.postDelayed(object :Runnable{
                        override fun run() {
                            Log.d("TestViewHolder","TestImageViewHolder $checkable is checked $isChecked  isSelect ${itemView.isSelected}")
                        }
                    },500)
                })
            }
        }
        override fun <T : Any?> onBindViewHolder(position: Int, data: T) {
            super.onBindViewHolder(position, data)
        }
    }
    class TestImage2ViewHolder(itemView: View):BaseViewHolder(itemView){
        override fun <T : Any?> onBindViewHolder(position: Int, data: T) {
            super.onBindViewHolder(position, data)
            if(itemView is CardFrameLayout){
                itemView.setOnCheckedChangeListener(ICheckView.OnCheckedChangeListener { checkable, isChecked ->
                    val image = itemView.findViewById<ImageView>(R.id.image_item)
                    image.post {
                        if(isChecked || itemView.isSelected){
                            image.setImageResource(R.drawable.vip_checked)
                        }else{
                            image.setImageResource(R.drawable.vip_normal)
                        }
                    }
                })
            }
        }
    }
}
