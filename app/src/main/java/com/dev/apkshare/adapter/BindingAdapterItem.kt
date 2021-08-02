package com.dev.apkshare.adapter

import android.app.Activity
import android.view.View

/**
 * @author: ${zhuxingchong}
 * @date: 2021/5/7
 * @description $desc$
 */
abstract class BindingAdapterItem {
    //每一项基础item数据类型

    abstract fun getViewType(): Int

    abstract fun bindData(root : View,position: Int,activity : Activity)

    abstract fun updateView(root : View)

    abstract fun onClick(view : View)
}