package com.pptv.httpsproject.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import com.pptv.httpsproject.httpdownload.DownLoadInfo
import com.pptv.httpsproject.httpdownload.HttpDownLoadManager
import com.pptv.httpsproject.httpdownload.listener.HttpProgressOnNextListener
import com.pptv.httpsproject.utils.*
import java.io.File

class DownLoadService : Service() {

    private val downInfo: DownLoadInfo by lazy {
        DownLoadInfo()
    }

    private var oldProgress: Int = 0
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
        return null
    }

    private fun downloadFile() {
        HttpDownLoadManager.startDownLoad(downInfo, object : HttpProgressOnNextListener<DownLoadInfo>() {
            override fun onNext(t: DownLoadInfo) {
                //收起通知栏
                NotificationBarUtil.setNotificationBarVisibility(this@DownLoadService, false);
                //安装
                ApkUtils.installApk(this@DownLoadService, File(downInfo.savPath))
            }

            override fun onComplete() {
                notificatonHelper.cancel()
                stopSelf()
            }

            override fun onProgress(readLength: Long, countLength: Long) {
                val progress = (readLength * 100 / countLength).toInt()
                if (progress != oldProgress) {
                    notificatonHelper.updateProgress(progress)
                    oldProgress = progress
                }
            }

            override fun onError(e: Throwable) {
                Toast.makeText(this@DownLoadService, e.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}