package com.dev.apkshare.utils

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Process
import android.os.UserHandle
import android.util.Log
import androidx.annotation.RequiresApi
import java.util.*

/**
 * @author : xingchong.zhu
 * description :
 * date : 2021/7/30
 * mail : hangchong.zhu@royole.com
 */
class PackageUtils {
    companion object{
        val TAG = "PackageUtils"

        fun getApplicationInfo(context : Context, pkgName : String): ApplicationInfo {
            val pm = context.getPackageManager()
            val applicationInfo = pm.getApplicationInfo(pkgName, 0)
            return applicationInfo
        }

        fun getStartAppIntent(context: Context, packageName: String): Intent? {
            return context.packageManager.getLaunchIntentForPackage(packageName)
        }

        /**
         * des 判断应用是否已经安装
         * @param context
         * @param pkg 包名
         * return true已经安装
         */
        fun isAppInstalled(context: Context, pkg: String): Boolean {
            val manager = context.packageManager
            val pkgList = manager.getInstalledPackages(0)
            for (i in pkgList.indices) {
                val pI = pkgList[i]
                if (pI.packageName.equals(pkg, ignoreCase = true)) {
                    return true
                }
            }
            return false
        }

        fun startAppSafe(context: Context, userHandle: UserHandle, packageName: String, oldIntent: Intent) {
            var intent : Intent?
            intent = context.packageManager.getLaunchIntentForPackage(packageName)
            if (intent == null) {
                intent = oldIntent
            }
            startApp(context, intent, userHandle)
        }

        /**
         * @param intent
         * @param userHandle
         */
        fun startApp(context: Context, intent: Intent?, userHandle: UserHandle?) {
            try {
                if (intent == null) {
                    Log.e(TAG, "startApp intent is null")
                    return
                }
                intent.action = Intent.ACTION_MAIN
                intent.addCategory(Intent.CATEGORY_LAUNCHER)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                val componentName = intent.component
                if (null != componentName) {
                    intent.setPackage(componentName.packageName)
                }

                context.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /**
         * 应用商店scheme协议获取Intent启动
         * @param context
         */
        fun openAppStore(context: Context) {
            val intent = getOpenAppStoreIntent()
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

        fun getOpenAppStoreIntent(): Intent {
            return Intent(Intent.ACTION_VIEW, getOpenAppStoreUri())
        }

        /**
         * 获取打开应用商店Scheme协议uri
         *
         * @return Scheme协议uri
         */
        fun getOpenAppStoreUri(): Uri {
            return Uri.Builder()
                    .scheme("scheme")
                    .authority("ry_app_store")
                    .path("/open")
                    .build()
        }

        @RequiresApi(29)
        fun getActivityTopName(context: Context): String? {
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            return manager.getRunningTasks(1)[0].topActivity?.getClassName()
        }


        fun needHideAppIcon(filterPackageList : Array<String>, packageName: String): Boolean {
            for (pkm in filterPackageList) {
                if (packageName == pkm) {
                    return true
                }
            }
            return false
        }
    }
}