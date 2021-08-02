package com.royole.globalsearch.convert

import android.content.Context
import com.dev.apkshare.adapter.BindingAdapterItem

/**
 * @author: ${zhuxingchong}
 * @date: 2021/5/8
 * @description 类型转换接口
 */
interface Convert<P,T : BindingAdapterItem>{
    fun convert(context : Context,obj: P): T
}