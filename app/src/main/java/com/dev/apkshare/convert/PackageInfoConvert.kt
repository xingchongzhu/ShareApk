package com.dev.apkshare.convert

import android.content.ComponentName
import android.content.Context
import android.content.pm.LauncherActivityInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Parcel
import android.os.UserHandle
import android.util.Log
import com.dev.apkshare.adapter.BindingAdapterItem
import com.dev.apkshare.bean.AppItemBean
import com.dev.apkshare.utils.PackageUtils
import com.royole.globalsearch.convert.AppItemBeanConvert
import com.royole.globalsearch.convert.Convert
import com.royole.globalsearch.factory.ConvertFactory
import java.util.*

/**
 * @author : xingchong.zhu
 * description :
 * date : 2021/8/4
 * mail : hangchong.zhu@royole.com
 */
object PackageInfoConvert : Convert<PackageInfo, AppItemBean> {

    fun launcherActivityInfoToAppItemBean(context: Context, list: List<PackageInfo>): MutableList<BindingAdapterItem> {
        val convertFactory = ConvertFactory<PackageInfo, AppItemBean>()
        val result = ArrayList<BindingAdapterItem>()
        list.forEach {
            result.add(convertFactory.produce(context, it, this))
        }

        result.sortBy { (it as AppItemBean).appLable}
        return result
    }

    override fun convert(context : Context, obj: PackageInfo): AppItemBean {
        val pm = context.getPackageManager()
        var spLable = obj.applicationInfo.loadLabel(pm)
        val applicationInfo = obj.applicationInfo//PackageUtils.getApplicationInfo(context,obj.componentName.packageName)
        val icon = applicationInfo.loadIcon(pm)
        val sourceDir = applicationInfo.sourceDir
        var className:String  = ""
        if(obj.applicationInfo.className != null){
            className = obj.applicationInfo.className
        }
        val appItemBean = AppItemBean(spLable.toString(),icon,sourceDir, ComponentName(obj.packageName,className), null)
        Log.d("zxc","getAllApps loadLabel = ${appItemBean.appLable}  ${appItemBean.componentName}")
        return appItemBean
    }
}