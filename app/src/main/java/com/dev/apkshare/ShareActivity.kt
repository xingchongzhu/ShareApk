package com.dev.apkshare

import android.content.pm.LauncherActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.apkshare.adapter.RecyclerAdapter
import com.dev.apkshare.apphelper.PackageManagerHelper
import com.dev.apkshare.utils.CommonUtils.APP_ITEM_MAX_COLUMNS
import com.royole.globalsearch.convert.AppItemBeanConvert.launcherActivityInfoToAppItemBean

/**
 * @author : xingchong.zhu
 * description :
 * date : 2021/7/30
 * mail : hangchong.zhu@royole.com
 */
class ShareActivity : BaseActivity() {

    val TAG = javaClass.simpleName
    lateinit var mPackageManagerHelper : PackageManagerHelper
    lateinit var mRecyclerView : RecyclerView
    lateinit var mRecyclerAdapter : RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initData()
    }

    override fun init() {

    }

    override fun initView() {
        mRecyclerView = findViewById(R.id.recyclerview)
    }

    override fun initData() {
        mPackageManagerHelper = PackageManagerHelper(this)
        val list = mPackageManagerHelper.getAllApps()
        val appList = launcherActivityInfoToAppItemBean(this,list)
        Log.d(TAG,"list ${list.size}")
        val layoutManager = GridLayoutManager(this, APP_ITEM_MAX_COLUMNS, GridLayoutManager.VERTICAL, false)
        mRecyclerView?.layoutManager = layoutManager
        layoutManager.spanSizeLookup = GridSpanSizer()

        mRecyclerAdapter = RecyclerAdapter(this,appList)
        mRecyclerView.adapter = mRecyclerAdapter
    }

    inner class GridSpanSizer : GridLayoutManager.SpanSizeLookup() {
        init {
            isSpanIndexCacheEnabled = true
        }

        override fun getSpanSize(position: Int): Int {
            return mRecyclerAdapter.getSpanCount(mRecyclerAdapter.getItemViewType(position))
        }
    }
}