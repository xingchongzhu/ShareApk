package com.dev.apkshare.utils

import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * @author : xingchong.zhu
 * description :
 * date : 2021/8/2
 * mail : hangchong.zhu@royole.com
 */
class FileUtils {
    companion object{
        val TAG = "FileUtils"
        val EXTERNAL_STORAGE_ROOT_DIR = Environment.getExternalStorageDirectory().absolutePath
        val APK_DIR = EXTERNAL_STORAGE_ROOT_DIR+File.separator+"shareApk"

        fun makeDir(){
            val file = File(APK_DIR)
            if(!file.exists()){
                file.mkdir()
            }
        }

        fun copyFile(oldPath: String, newPath: String): Boolean {
            try {
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
                Log.e(TAG, "copyFile:  oldPath "+oldPath+" newPath = "+newPath)
                val fileInputStream = FileInputStream(oldPath)    //读入原文件
                val fileOutputStream = FileOutputStream(newPath)
                val buffer = ByteArray(1024)
                var byteRead : Int = 0
                do {
                    byteRead = fileInputStream.read(buffer)
                    fileOutputStream.write(buffer, 0, byteRead)
                }while (byteRead != -1)

                fileInputStream.close()
                fileOutputStream.flush()
                fileOutputStream.close()
                return true
            } catch (e: Exception) {
                Log.e(TAG, "e "+e)
                return false
            }
        }
    }
}