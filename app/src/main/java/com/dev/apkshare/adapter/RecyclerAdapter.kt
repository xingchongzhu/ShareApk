package com.dev.apkshare.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dev.apkshare.R
import com.dev.apkshare.utils.CommonUtils.APP_ITEM_MAX_COLUMNS

/**
 * @author : xingchong.zhu
 * description :
 * date : 2021/7/30
 * mail : hangchong.zhu@royole.com
 */
class RecyclerAdapter(var activity: Activity,var items: MutableList<BindingAdapterItem>) : RecyclerView.Adapter<RecyclerAdapter.BindingHolder>(){

    fun setData(list: MutableList<BindingAdapterItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return items.get(position).getViewType()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        return BindingHolder(LayoutInflater.from(parent.context).inflate(viewType,parent,false),activity)
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        items.get(position).let { holder.bindData(it, position) }
    }

    fun getSpanCount(type: Int): Int {
        when (type) {
            R.layout.adapter_app_item -> return 1
            else -> {
                return APP_ITEM_MAX_COLUMNS
            }
        }
    }

    class BindingHolder(var binding: View,var activity : Activity) : RecyclerView.ViewHolder(binding) {
        fun bindData(item: BindingAdapterItem, position: Int) {
            item.bindData(binding, position,activity)
        }
    }
}