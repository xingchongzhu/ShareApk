package com.dev.apkshare.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File

/**
 * @author : xingchong.zhu
 * description :
 * date : 2021/7/30
 * mail : hangchong.zhu@royole.com
 */
object ShareUtils {
    fun share(activity: Activity, path: String) {
        val sourceDir = File(path)
        val mimetype = ShareSystem.ShareContentType.FILE

        if (sourceDir.exists()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val uri = FileProvider.getUriForFile(activity,
                        activity.packageName + ".FileProvider", sourceDir)
                ShareSystem.Builder(activity).setContentType(mimetype)
                        .setShareFileUri(uri)
                        .forcedUseSystemChooser(false)
                        .build()
                        .shareBySystem()
            } else {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.setDataAndType(Uri.fromFile(sourceDir), mimetype)
                activity.startActivity(Intent.createChooser(intent, "分享"))
            }
        } else {
            Toast.makeText(activity, "$path 文件不存在", Toast.LENGTH_LONG).show()
        }
    }
}