package com.royole.globalsearch.factory

import android.content.Context
import com.dev.apkshare.bean.AppItemBean
import com.royole.globalsearch.convert.Convert

/**
 * @author: ${zhuxingchong}
 * @date: 2021/5/8
 * @description 数据类型转换工厂
 */
class ConvertFactory<P,Q : AppItemBean> {

    fun produce(context: Context,objType : Any,convert : Convert<P,Q>): AppItemBean {
        return convert.convert(context,objType as P)
    }
}