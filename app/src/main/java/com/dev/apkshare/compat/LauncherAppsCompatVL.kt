package com.royole.globalsearch.compat

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.*
import android.graphics.Rect
import android.os.Bundle
import android.os.Process
import android.os.UserHandle
import android.util.ArrayMap
import com.dev.apkshare.utils.test

/**
 * @author: ${zhuxingchong}
 * @date: 2021/5/8
 * @description 应用获取工具
 */
class LauncherAppsCompatVL(context: Context) : LauncherAppsCompat() {
    protected val mLauncherApps: LauncherApps

    private val mCallbacks = ArrayMap<OnAppsChangedCallbackCompat, WrappedCallback>()

    init {
        mLauncherApps = context.getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps
    }

    override fun getActivityList(packageName: String?, user: UserHandle): List<LauncherActivityInfo> {
        return mLauncherApps.getActivityList(packageName, user)
    }

    override fun resolveActivity(intent: Intent, user: UserHandle): LauncherActivityInfo {
        return mLauncherApps.resolveActivity(intent, user)
    }

    override fun startActivityForProfile(component: ComponentName, user: UserHandle,
                                sourceBounds: Rect, opts: Bundle) {
        mLauncherApps.startMainActivity(component, user, sourceBounds, opts)
    }

    override fun getApplicationInfo(context: Context, packageName: String, flags: Int, user: UserHandle): ApplicationInfo? {
        val isPrimaryUser = Process.myUserHandle() == user
        if (!isPrimaryUser && flags == 0) {
            // We are looking for an installed app on a secondary profile. Prior to O, the only
            // entry point for work profiles is through the LauncherActivity.
            val activityList = mLauncherApps.getActivityList(packageName, user)
             if (activityList.size > 0){
                 return activityList[0].applicationInfo
             }
        }
        try {
            val info = context.getPackageManager()?.getApplicationInfo(packageName, flags)
            // There is no way to check if the app is installed for managed profile. But for
            // primary profile, we can still have this check.
            if (isPrimaryUser && ((info?.flags?.and(ApplicationInfo.FLAG_INSTALLED)) == 0)  || !info?.enabled!!) {
               return null
            }
            return info
        } catch (e: PackageManager.NameNotFoundException) {
            // Package not found
            return null
        }

    }

    override fun showAppDetailsForProfile(component: ComponentName, user: UserHandle,
                                 sourceBounds: Rect?, opts: Bundle?) {
        mLauncherApps.startAppDetailsActivity(component, user, sourceBounds, opts)
    }

    override fun addOnAppsChangedCallback(callback: OnAppsChangedCallbackCompat) {
        val wrappedCallback = WrappedCallback(callback)
        synchronized(mCallbacks) {
            mCallbacks.put(callback, wrappedCallback)
        }
        mLauncherApps.registerCallback(wrappedCallback)
    }

    override fun removeOnAppsChangedCallback(callback: OnAppsChangedCallbackCompat) {
        val wrappedCallback: WrappedCallback?
        synchronized(mCallbacks) {
            wrappedCallback = mCallbacks.remove(callback)
        }
        if (wrappedCallback != null) {
            mLauncherApps.unregisterCallback(wrappedCallback)
        }
    }

    override  fun isPackageEnabledForProfile(packageName: String, user: UserHandle): Boolean {
        return mLauncherApps.isPackageEnabled(packageName, user)
    }

    override fun isActivityEnabledForProfile(component: ComponentName, user: UserHandle): Boolean {
        return mLauncherApps.isActivityEnabled(component, user)
    }
}

