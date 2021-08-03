package com.dev.apkshare

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import android.content.DialogInterface
import android.net.Uri.fromParts
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog


/**
 * @author: ${zhuxingchong}
 * @date: 2021/4/29
 * @description 抽象基础类提供公共初始化相关方法
 */
open abstract class BaseActivity : AppCompatActivity(){

    private var mDialog: AlertDialog? = null
    private val NOT_NOTICE = 2

    val requetPermissionList = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        updateSystemUiMode()
        myRequetPermission()
    }

    private fun updateSystemUiMode() {
        getWindow().getDecorView()?.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
    }

    private fun setStatusColor() = try {
        var vis = window.decorView.windowInsetsController?.systemBarsBehavior
        //白色
        vis = vis?.and(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv())
        vis = vis?.and(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv())
        window.decorView.windowInsetsController?.setSystemBarsBehavior(vis!!)
        //设置导航栏和状态栏颜色
        window.navigationBarColor = Color.TRANSPARENT
        window.statusBarColor = Color.TRANSPARENT

    } catch (e: Exception) {
        e.printStackTrace()
    }

    private fun myRequetPermission() {
        var mutableMap : MutableList<String> = mutableListOf<String>()
        requetPermissionList.forEach {
            if (ContextCompat.checkSelfPermission(this, it) != PERMISSION_GRANTED){
                mutableMap.add(it)
            }
        }
        if(mutableMap.size > 0){
            ActivityCompat.requestPermissions(this, mutableMap.toTypedArray(), 1)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode === 1) {
            for (i in 0 until permissions.size) {
                if (grantResults[i] === PERMISSION_GRANTED) {//选择了“始终允许”
                    Toast.makeText(this, "" + "权限" + permissions[i] + "申请成功", Toast.LENGTH_SHORT).show()
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {//用户选择了禁止不再询问
                        val builder = AlertDialog.Builder(this@BaseActivity)
                        builder.setTitle("permission")
                                .setMessage("点击允许才可以使用我们的app哦")
                                .setPositiveButton("去允许") { dialog, id ->
                                    mDialog?.let {
                                        it.dismiss()
                                    }
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri = Uri.fromParts("package", packageName, null)//注意就是"package",不用改成自己的包名
                                    intent.setData(uri)
                                    startActivityForResult(intent, NOT_NOTICE)
                                }
                        mDialog = builder.create()
                        mDialog?.setCanceledOnTouchOutside(false)
                        mDialog?.show()
                    } else {//选择禁止
                        val builder = AlertDialog.Builder(this@BaseActivity)
                        builder.setTitle("permission")
                                .setMessage("点击允许才可以使用我们的app哦")
                                .setPositiveButton("去允许") { dialog, id ->
                                    mDialog?.let {
                                        it.dismiss()
                                    }
                                    ActivityCompat.requestPermissions(this@BaseActivity,
                                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                                }
                        mDialog = builder.create()
                        mDialog?.setCanceledOnTouchOutside(false)
                        mDialog?.show()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    abstract fun init()
    abstract fun initView()
    abstract fun initData()
}
