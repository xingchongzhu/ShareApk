package com.dev.apkshare.bean

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.UserHandle
import android.text.SpannableString
import android.view.View
import android.widget.TextView
import com.dev.apkshare.R
import com.dev.apkshare.adapter.BindingAdapterItem
import com.dev.apkshare.utils.FileUtils
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
        ShareUtils.share(mActivity!!,apkSrc)
        return true
    }

    fun save(){
        FileUtils.makeDir()
        val newName = FileUtils.APK_DIR+ File.separator+componentName!!.packageName+".apk"
        FileUtils.copyFile(apkSrc,newName)
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