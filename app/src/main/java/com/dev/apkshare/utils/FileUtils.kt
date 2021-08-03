package com.dev.apkshare.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import android.content.ActivityNotFoundException
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider


/**
 * @author : xingchong.zhu
 * description :
 * date : 2021/8/2
 * mail : hangchong.zhu@royole.com
 */
class FileUtils {
    companion object{
        val TAG = "FileUtils"
        var EXTERNAL_STORAGE_ROOT_DIR = Environment.getExternalStorageDirectory().absolutePath
        val APK_DIR = File.separator+"shareApk"
        fun setRootDir(activity: Activity){
            EXTERNAL_STORAGE_ROOT_DIR = activity.externalCacheDir?.absolutePath
        }

        fun getExternalApkPath():String{
            return EXTERNAL_STORAGE_ROOT_DIR+APK_DIR
        }

        fun makeDir(){
            val file = File(EXTERNAL_STORAGE_ROOT_DIR+APK_DIR)
            if(!file.exists()){
                file.mkdirs()
            }
            Log.d(TAG,"${file.absolutePath} "+file.exists())
        }

        fun copyFile(oldPath: String, newPath: String): Boolean {
            try {
                val neweFile = File(newPath)
                if(neweFile.exists()){
                    return true
                }

                val oldFile = File(oldPath)
                if (!oldFile.exists()) {
                    Log.e(TAG, "copyFile:  oldFile not exist.")
                    return false
                } else if (!oldFile.isFile) {
                    Log.e(TAG, "copyFile:  oldFile not file.")
                    return false
                } else if (!oldFile.canRead()) {
                    Log.e(TAG, "copyFile:  oldFile cannot read.")
                    return false
                }
                Log.d(TAG, "copyFile:  oldPath "+oldPath+" newPath = "+newPath)

                val fileInputStream = FileInputStream(oldPath)    //读入原文件
                val fileOutputStream = FileOutputStream(newPath)
                val buffer = ByteArray(1024)
                var byteRead : Int = 0
                do {
                    byteRead = fileInputStream.read(buffer)
                    if(byteRead != -1) {
                        fileOutputStream.write(buffer, 0, byteRead)
                    }else{
                        break
                    }
                }while (true)

                fileInputStream.close()
                fileOutputStream.flush()
                fileOutputStream.close()
                return true
            } catch (e: Exception) {
                Log.e(TAG, "copyFile e "+e)
                return false
            }
        }

         fun openFileManagerByPath(activity: Activity,path: String) {
            val file = File(path)
            if (null == file || !file.exists()) {
                return
            }

             val uri = Uri.fromFile(file)
                     //FileProvider.getUriForFile(activity,
                    // activity.packageName + ".FileProvider", file.absoluteFile)

            val mimetype = ShareSystem.ShareContentType.FOLDER
             ShareSystem.Builder(activity).setContentType(mimetype)
                     .setShareFileUri(uri)
                     .forcedUseSystemChooser(false)
                     .build()
                     .shareBySystem()

        }

    }
}