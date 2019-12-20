package com.pptv.httpsproject.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.pptv.httpsproject.httpdownload.DownLoadInfo
import com.pptv.httpsproject.httpdownload.HttpDownLoadManager
import com.pptv.httpsproject.httpdownload.listener.HttpProgressOnNextListener
import com.pptv.httpsproject.utils.Constants
import com.pptv.httpsproject.utils.NotificationBarUtil
import com.pptv.httpsproject.utils.NotificationHelper
import com.pptv.httpsproject.utils.StorageUtils
import java.io.File

class DownLoadService : Service() {

    private val downInfo: DownLoadInfo by lazy {
        DownLoadInfo()
    }

    private var onlProgress: Int = 0
    private val notificatonHelper: NotificationHelper by lazy {
        NotificationHelper(this)
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        assert(intent != null)//如果intent != null 返回false，即intent为空 则报出 AssertionError
        val urlStr = intent!!.getStringExtra(Constants.APK_DOWNLOAD_URL)
        downInfo.url = urlStr
        val dir = StorageUtils.getExternalCacheCustomDir(this)
        val apkName = urlStr.substring(urlStr.lastIndexOf("/") + 1, urlStr.length)
        val apkFile = File(dir, apkName)
        downInfo.savPath = apkFile.absolutePath
        downloadFile()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun downloadFile() {
        HttpDownLoadManager.startDownLoad(downInfo, object : HttpProgressOnNextListener<DownLoadInfo>() {
            override fun onNext(t: DownLoadInfo) {
                NotificationBarUtil.setNotificationBarVisibility(this@DownLoadService, false);
                
            }

            override fun onComplete() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onProgress(readLength: Long, countLength: Long) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onError(e: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }
}