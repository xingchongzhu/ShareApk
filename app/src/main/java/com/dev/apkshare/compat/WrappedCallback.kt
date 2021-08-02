package com.royole.globalsearch.compat

import android.content.pm.LauncherApps
import android.content.pm.ShortcutInfo
import android.os.UserHandle

/**
 * @author: ${zhuxingchong}
 * @date: 2021/5/8
 * @description $desc$
 */
internal class WrappedCallback(private val mCallback: LauncherAppsCompat.OnAppsChangedCallbackCompat) : LauncherApps.Callback() {

    override fun onPackageRemoved(packageName: String, user: UserHandle) {
        mCallback.onPackageRemoved(packageName, user)
    }

    override fun onPackageAdded(packageName: String, user: UserHandle) {
        mCallback.onPackageAdded(packageName, user)
    }

    override fun onPackageChanged(packageName: String, user: UserHandle) {
        mCallback.onPackageChanged(packageName, user)
    }

    override fun onPackagesAvailable(packageNames: Array<String>, user: UserHandle, replacing: Boolean) {
        mCallback.onPackagesAvailable(packageNames, user, replacing)
    }

    override fun onPackagesUnavailable(packageNames: Array<String>, user: UserHandle,
                                       replacing: Boolean) {
        mCallback.onPackagesUnavailable(packageNames, user, replacing)
    }

    override fun onPackagesSuspended(packageNames: Array<String>, user: UserHandle) {
        mCallback.onPackagesSuspended(packageNames, user)
    }

    override fun onPackagesUnsuspended(packageNames: Array<String>, user: UserHandle) {
        mCallback.onPackagesUnsuspended(packageNames, user)
    }

    override fun onShortcutsChanged(packageName: String,
                                    shortcuts: List<ShortcutInfo>,
                                    user: UserHandle) {
        mCallback.onShortcutsChanged(packageName,shortcuts, user)
    }
}