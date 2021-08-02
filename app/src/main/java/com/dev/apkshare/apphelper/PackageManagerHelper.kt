package com.dev.apkshare.apphelper

import android.content.Context
import android.content.pm.LauncherActivityInfo
import android.content.pm.PackageManager
import android.os.UserManager
import com.dev.apkshare.adapter.BindingAdapterItem
import com.dev.apkshare.bean.AppItemBean
import com.royole.globalsearch.compat.LauncherAppsCompat
import com.royole.globalsearch.convert.AppItemBeanConvert
import com.royole.globalsearch.factory.ConvertFactory
import java.util.*

/**
 * @author: ${zhuxingchong}
 * @date: 2021/5/6
 * @description 应用工具类
 */
/**
 * Utility methods using package manager
 */
class PackageManagerHelper {
    private val mPm: PackageManager
    private val mLauncherAppsCompat: LauncherAppsCompat?
    private val mUserManager: UserManager

    constructor(context: Context){
        mPm = context.packageManager
        mUserManager = context.getSystemService(Context.USER_SERVICE) as UserManager
        mLauncherAppsCompat = LauncherAppsCompat.getInstance(context)
    }

    fun getAllApps(): MutableList<LauncherActivityInfo> {
        val allApps = ArrayList<LauncherActivityInfo>()
        val userHandles = mUserManager.userProfiles
        for (userHandle in userHandles) {
            mLauncherAppsCompat?.getActivityList(null, userHandle)?.let { allApps.addAll(it) }
        }
        return allApps
    }
}
