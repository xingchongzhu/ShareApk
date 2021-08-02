package com.royole.globalsearch.convert

import android.content.Context
import android.content.Intent
import android.content.pm.LauncherActivityInfo
import com.dev.apkshare.adapter.BindingAdapterItem
import com.dev.apkshare.bean.AppItemBean
import com.dev.apkshare.utils.PackageUtils
import com.royole.globalsearch.factory.ConvertFactory
import java.util.ArrayList

/**
 * @author: ${zhuxingchong}
 * @date: 2021/5/8
 * @description LauncherActivityInfo转换为AppItemBean
 */
object AppItemBeanConvert : Convert<LauncherActivityInfo, AppItemBean> {

    fun launcherActivityInfoToAppItemBean(context: Context, list: List<LauncherActivityInfo>): MutableList<BindingAdapterItem> {
        val convertFactory = ConvertFactory<LauncherActivityInfo, AppItemBean>()
        val result = ArrayList<BindingAdapterItem>()
        list.forEach {
            result.add(convertFactory.produce(context, it, AppItemBeanConvert))
        }
        return result
    }

    override fun convert(context : Context,obj: LauncherActivityInfo): AppItemBean {
        var spLable = obj.label.toString()
        val pm = context.getPackageManager()
        val applicationInfo = PackageUtils.getApplicationInfo(context,obj.componentName.packageName)
        val icon = applicationInfo.loadIcon(pm)
        val sourceDir = applicationInfo.sourceDir
        val appItemBean = AppItemBean(spLable,icon,sourceDir,obj.componentName,obj.user)
        return appItemBean
    }
}