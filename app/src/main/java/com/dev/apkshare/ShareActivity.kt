package com.dev.apkshare

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev.apkshare.adapter.BindingAdapterItem
import com.dev.apkshare.adapter.RecyclerAdapter
import com.dev.apkshare.apphelper.PackageManagerHelper
import com.dev.apkshare.convert.PackageInfoConvert
import com.dev.apkshare.utils.CommonUtils.APP_ITEM_MAX_COLUMNS
import com.dev.apkshare.utils.FileUtils
import com.dev.apkshare.utils.FileUtils.Companion.getExternalApkPath
import com.dev.apkshare.utils.FileUtils.Companion.openFileManagerByPath
import com.royole.globalsearch.convert.AppItemBeanConvert
import kotlinx.coroutines.launch

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
    lateinit var mTextTitle : TextView
    lateinit var mButtonSwitch : Button
    var mAppType : AppType = AppType.NORMAL

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
        mTextTitle = findViewById(R.id.path)
        mButtonSwitch = findViewById(R.id.button)
    }

    override fun initData() {
        FileUtils.setRootDir(this)
        mPackageManagerHelper = PackageManagerHelper(this)
        val appList = mutableListOf<BindingAdapterItem>()
        val layoutManager = GridLayoutManager(this, APP_ITEM_MAX_COLUMNS, GridLayoutManager.VERTICAL, false)
        mRecyclerView?.layoutManager = layoutManager
        layoutManager.spanSizeLookup = GridSpanSizer()

        mRecyclerAdapter = RecyclerAdapter(this,appList)
        mRecyclerView.adapter = mRecyclerAdapter

        mTextTitle.setText(String.format(resources.getString(R.string.save_path_dir),getExternalApkPath()));

        mTextTitle.setOnClickListener {
            openFileManagerByPath(this@ShareActivity,getExternalApkPath())
        }
        mButtonSwitch.setOnClickListener {
            swatchAppList()
        }
        updateData()
    }

    private fun updateButtonText() {
        when(mAppType){
            AppType.SYSTEM->
            mButtonSwitch.setText(R.string.system_app)
            AppType.NORMAL->
                mButtonSwitch.setText(R.string.normal_app)
        }
    }

    suspend fun getAppList(): MutableList<BindingAdapterItem> {
        when(mAppType){
            AppType.SYSTEM->{
                val list = mPackageManagerHelper.getAllAppsPackageInfo()
                return PackageInfoConvert.launcherActivityInfoToAppItemBean(this,list)
            }
            AppType.NORMAL->{
                val list = mPackageManagerHelper.getAllApps()
                return AppItemBeanConvert.launcherActivityInfoToAppItemBean(this,list)
            }
        }
        return mutableListOf()
    }

    private fun swatchAppList() {
        when(mAppType){
            AppType.SYSTEM->{
                mAppType = AppType.NORMAL
            }
            AppType.NORMAL->{
                mAppType = AppType.SYSTEM
            }
        }
        updateData()
    }

    private fun updateData() {
        updateButtonText()
        lifecycleScope.launch {
            val list = getAppList()
            mRecyclerAdapter.setData(list)
        }
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