package com.royole.globalsearch.compat

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.LauncherActivityInfo
import android.content.pm.ShortcutInfo
import android.graphics.Rect
import android.os.Bundle
import android.os.UserHandle
import com.dev.apkshare.utils.test

/**
 * @author: ${zhuxingchong}
 * @date: 2021/5/8
 * @description $desc$
 */
abstract class LauncherAppsCompat protected constructor() {

    interface OnAppsChangedCallbackCompat {
        fun onPackageRemoved(packageName: String, user: UserHandle)
        fun onPackageAdded(packageName: String, user: UserHandle)
        fun onPackageChanged(packageName: String, user: UserHandle)
        fun onPackagesAvailable(packageNames: Array<String>, user: UserHandle, replacing: Boolean)
        fun onPackagesUnavailable(packageNames: Array<String>, user: UserHandle, replacing: Boolean)
        fun onPackagesSuspended(packageNames: Array<String>, user: UserHandle)
        fun onPackagesUnsuspended(packageNames: Array<String>, user: UserHandle)
        fun onShortcutsChanged(packageName: String, shortcuts: List<ShortcutInfo>,
                               user: UserHandle)
    }

    companion object {
        private var sInstance: LauncherAppsCompat? = null

        fun getInstance(context: Context): LauncherAppsCompat? {
            if (sInstance == null) {
                synchronized(LauncherAppsCompat.javaClass) {
                    sInstance = LauncherAppsCompatVL(context.applicationContext)
                }
            }
            return sInstance
        }
    }

    abstract fun getActivityList(packageName: String?,
                                 user: UserHandle): List<LauncherActivityInfo>

    abstract fun resolveActivity(intent: Intent,
                                 user: UserHandle): LauncherActivityInfo

    abstract fun startActivityForProfile(component: ComponentName, user: UserHandle,
                                         sourceBounds: Rect, opts: Bundle)

    abstract fun getApplicationInfo(context: Context,
            packageName: String, flags: Int, user: UserHandle): ApplicationInfo?

    abstract fun showAppDetailsForProfile(component: ComponentName, user: UserHandle,
                                          sourceBounds: Rect?, opts: Bundle?)

    abstract fun addOnAppsChangedCallback(listener: OnAppsChangedCallbackCompat)
    abstract fun removeOnAppsChangedCallback(listener: OnAppsChangedCallbackCompat)
    abstract fun isPackageEnabledForProfile(packageName: String, user: UserHandle): Boolean
    abstract fun isActivityEnabledForProfile(component: ComponentName,
                                             user: UserHandle): Boolean
}
