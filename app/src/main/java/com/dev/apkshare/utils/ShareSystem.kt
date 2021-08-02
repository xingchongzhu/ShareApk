package com.dev.apkshare.utils
import android.app.Activity
import android.content.ComponentName
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.View

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * @author : xingchong.zhu
 * description :
 * date : 2021/7/30
 * mail : hangchong.zhu@royole.com
 */

class ShareSystem private constructor(builder: Builder) {

    val TAG = javaClass.simpleName

    /**
     * 当前 activity
     */
    private val activity: Activity?

    /**
     * 分享格式
     */
    @ShareContentType
    private val contentType: String

    /**
     * 分享标题
     */
    private var title: String? = null

    /**
     * 分销uri
     */
    private val shareFileUri: Uri?

    /**
     * 分享内容
     */
    private val contentText: String?


    private val componentPackageName: String?


    private val componentClassName: String?


    private val requestCode: Int


    private val forcedUseSystemChooser: Boolean

    init {
        this.activity = builder.activity
        this.contentType = builder.contentType
        this.title = builder.title
        this.shareFileUri = builder.shareFileUri
        this.contentText = builder.textContent
        this.componentPackageName = builder.componentPackageName
        this.componentClassName = builder.componentClassName
        this.requestCode = builder.requestCode
        this.forcedUseSystemChooser = builder.forcedUseSystemChooser
    }

    /**
     * shareBySystem
     */
    fun shareBySystem() {
        if (checkShareParam()) {
            var shareIntent = createShareIntent()

            if (shareIntent == null) {
                Log.e(TAG, "shareBySystem cancel.")
                return
            }

            if (title == null) {
                title = ""
            }

            if (forcedUseSystemChooser) {
                shareIntent = Intent.createChooser(shareIntent, title)
            }

            if (shareIntent!!.resolveActivity(activity!!.packageManager) != null) {
                try {
                    if (requestCode != -1) {
                        activity.startActivityForResult(shareIntent, requestCode)
                    } else {
                        activity.startActivity(shareIntent)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, Log.getStackTraceString(e))
                }
            }
        }
    }

    private fun createShareIntent(): Intent? {
        var shareIntent: Intent? = Intent()
        shareIntent!!.action = Intent.ACTION_SEND
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        shareIntent.addCategory("android.intent.category.DEFAULT")

        if (!TextUtils.isEmpty(this.componentPackageName) && !TextUtils.isEmpty(componentClassName)) {
            val comp = ComponentName(componentPackageName!!, componentClassName!!)
            shareIntent.component = comp
        }

        when (contentType) {
            ShareContentType.TEXT -> {
                shareIntent.putExtra(Intent.EXTRA_TEXT, contentText)
                shareIntent.type = "text/plain"
            }
            ShareContentType.IMAGE, ShareContentType.AUDIO, ShareContentType.VIDEO, ShareContentType.FILE -> {
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.addCategory("android.intent.category.DEFAULT")
                shareIntent.type = contentType
                shareIntent.putExtra(Intent.EXTRA_STREAM, shareFileUri)
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                Log.d(TAG, "Share uri: " + shareFileUri!!.toString()+" VERSION = "+Build.VERSION.SDK_INT+" forcedUseSystemChooser = "+forcedUseSystemChooser)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val resInfoList = activity!!.packageManager.queryIntentActivities(shareIntent, PackageManager.MATCH_DEFAULT_ONLY)
                    for (resolveInfo in resInfoList) {
                        val packageName = resolveInfo.activityInfo.packageName
                        //赋予临时权限
                        activity.grantUriPermission(packageName, shareFileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                }
            }
            else -> {
                Log.e(TAG, "$contentType is not support share type.")
                shareIntent = null
            }
        }

        return shareIntent
    }

    private fun checkShareParam(): Boolean {
        if (this.activity == null) {
            Log.e(TAG, "activity is null.")
            return false
        }

        if (TextUtils.isEmpty(this.contentType)) {
            Log.e(TAG, "Share content type is empty.")
            return false
        }

        if (ShareContentType.TEXT == contentType) {
            if (TextUtils.isEmpty(contentText)) {
                Log.e(TAG, "Share text context is empty.")
                return false
            }
        } else {
            if (this.shareFileUri == null) {
                Log.e(TAG, "Share file path is null.")
                return false
            }
        }

        return true
    }


    /**
     * module TW_APP_LAUNCHER3
     * author 胡冰
     * date 2019/1/4
     * description  分享构造器
     */
    class Builder(val activity: Activity) {
        @ShareContentType
        var contentType = ShareContentType.FILE
        var title: String? = null
        var componentPackageName: String? = null
        var componentClassName: String? = null
        var shareFileUri: Uri? = null
        var textContent: String? = null
        var requestCode = -1
        var forcedUseSystemChooser = true

        /**
         * Set Content Type
         *
         * @param contentType [ShareContentType]
         * @return Builder
         */
        fun setContentType(@ShareContentType contentType: String): Builder {
            this.contentType = contentType
            return this
        }

        /**
         * Set Title
         *
         * @param title title
         * @return Builder
         */
        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        /**
         * Set share file path
         *
         * @param shareFileUri shareFileUri
         * @return Builder
         */
        fun setShareFileUri(shareFileUri: Uri): Builder {
            this.shareFileUri = shareFileUri
            return this
        }

        /**
         * Set text content
         *
         * @param textContent textContent
         * @return Builder
         */
        fun setTextContent(textContent: String): Builder {
            this.textContent = textContent
            return this
        }

        /**
         * Set Share To Component
         *
         * @param componentPackageName componentPackageName
         * @param componentClassName   componentPackageName
         * @return Builder
         */
        fun setShareToComponent(componentPackageName: String, componentClassName: String): Builder {
            this.componentPackageName = componentPackageName
            this.componentClassName = componentClassName
            return this
        }

        /**
         * Set onActivityResult requestCode, default value is -1
         *
         * @param requestCode requestCode
         * @return Builder
         */
        fun setOnActivityResult(requestCode: Int): Builder {
            this.requestCode = requestCode
            return this
        }

        /**
         * Forced Use System Chooser To Share
         *
         * @param enable default is true
         * @return Builder
         */
        fun forcedUseSystemChooser(enable: Boolean): Builder {
            this.forcedUseSystemChooser = enable
            return this
        }

        /**
         * build
         *
         * @return Share2
         */
        fun build(): ShareSystem {
            return ShareSystem(this)
        }

    }

    @Retention(RetentionPolicy.SOURCE)
    annotation class ShareContentType {
        companion object {
            /**
             * Share Text
             */
            val TEXT = "text/plain"

            /**
             * Share Image
             */
            val IMAGE = "image/*"

            /**
             * Share Audio
             */
            val AUDIO = "audio/*"

            /**
             * Share Video
             */
            val VIDEO = "video/*"

            /**
             * Share File
             */
            val FILE = "*/*"
        }
    }
}
