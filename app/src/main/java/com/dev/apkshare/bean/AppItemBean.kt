package com.dev.apkshare.bean

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.os.UserHandle
import android.provider.Settings
import android.text.SpannableString
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.startActivityForResult
import com.dev.apkshare.R
import com.dev.apkshare.adapter.BindingAdapterItem
import com.dev.apkshare.utils.FileUtils
import com.dev.apkshare.utils.FileUtils.Companion.APK_DIR
import com.dev.apkshare.utils.FileUtils.Companion.EXTERNAL_STORAGE_ROOT_DIR
import com.dev.apkshare.utils.PackageUtils
import com.dev.apkshare.utils.ShareUtils
import java.io.File

/**
 * @author: ${zhuxingchong}
 * @date: 2021/5/7
 * @description app搜索项
 */
class AppItemBean(var appLable: String? = null,var appIcon: Drawable,var apkSrc : String,
                  var componentName: ComponentName? = null,var userHandle: UserHandle? = null) : BindingAdapterItem() ,View.OnLongClickListener,View.OnClickListener{


    val TAG = javaClass.simpleName
    var mActivity : Activity? = null
    private var mDialog: AlertDialog? = null
    override fun getViewType(): Int {
        return R.layout.adapter_app_item
    }

    override fun onClick(view : View){
        val intent = Intent()
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setComponent(componentName)
        mActivity?.let { PackageUtils.startApp(it,intent,userHandle) }
    }

    override fun onLongClick(v: View): Boolean {
        mDialog?.let {
            if(it.isShowing) {
                it.dismiss()
            }
        }
        mActivity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(appLable)
                    .setMessage(it.resources.getString(R.string.long_click_dialog_message))
                    .setNegativeButton(it.resources.getString(R.string.save_label)) { dialog, id ->
                        mDialog?.let {
                            it.dismiss()
                        }
                        if(save()){
                            Toast.makeText(it,it.resources.getString(R.string.save_sucess),Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(it,it.resources.getString(R.string.save_fail),Toast.LENGTH_LONG).show()
                        }
                    }
                    .setPositiveButton(it.resources.getString(R.string.share_label)) { dialog, id ->
                        mDialog?.let {
                            it.dismiss()
                        }
                        ShareUtils.share(mActivity!!,apkSrc)
                    }
            mDialog = builder.create()
            mDialog?.setCanceledOnTouchOutside(true)
            mDialog?.show()
        }
        return true
    }

    fun save():Boolean{
        FileUtils.makeDir()
        val newName = EXTERNAL_STORAGE_ROOT_DIR+APK_DIR+File.separator+appLable+".apk"
        return FileUtils.copyFile(apkSrc,newName)
    }

    override fun bindData(root: View, position: Int,activity : Activity) {
        mActivity = activity
        val text = root.findViewById<TextView>(R.id.app_label)
        text.setText(appLable)
        text.setOnClickListener(this)
        text.setOnLongClickListener(this)
        text.compoundDrawablePadding = root.context.resources.getDimension(R.dimen.drawable_padding).toInt()
        val iconSize : Int = root.context.resources.getDimension(R.dimen.icon_size).toInt()
        appIcon.setBounds(0,0,iconSize,iconSize)
        text.setCompoundDrawables(null,appIcon,null,null)
    }

    override fun updateView(root: View) {
    }

    override fun toString(): String {
        //return "appLable : $appLable componentName : $componentName appIcon $appIcon"
        return "appLable : $appLable user $userHandle"
    }
}