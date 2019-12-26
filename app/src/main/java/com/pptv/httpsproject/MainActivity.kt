package com.pptv.httpsproject


import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import com.pptv.httpsproject.httpdownload.DownLoadInfo
import com.pptv.httpsproject.httpdownload.DownLoadState
import com.pptv.httpsproject.httpdownload.HttpDownLoadManager
import com.pptv.httpsproject.httpdownload.listener.HttpProgressOnNextListener
import com.pptv.httpsproject.utils.UpdateChecker

import kotlinx.android.synthetic.main.activity_main.* //来自 kotlin-android-extensions 但是只能找到本工程下的R文件，第三方的不行
import java.io.File
import kotlinx.android.synthetic.main.activity_main.btn_pauseDown_alipay as btnPauseAli

class MainActivity : Activity(), View.OnClickListener {

    //懒加载初始化View
    val mBtnStartDownQQ by lazy {
        btn_startDown_qq
    }
    val dialog  = ProgressDialog(this@MainActivity)

    val qqDownInfo: DownLoadInfo = DownLoadInfo("https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk")

    companion object {
        const val qqDwonloadUrl = "https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val qqApkFile = File(
            externalCacheDir,
            qqDwonloadUrl.substring(qqDwonloadUrl.lastIndexOf("/") + 1, qqDwonloadUrl.length)
        )
        qqDownInfo.savPath = qqApkFile.absolutePath
        qqDownInfo.downLoadState = DownLoadState.START
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_startDown_qq -> {
                HttpDownLoadManager.startDownLoad(qqDownInfo,
                    object : HttpProgressOnNextListener<DownLoadInfo>() {
                        override fun onNext(t: DownLoadInfo) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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


        fun updateDialog(view: View) {
            UpdateChecker.checkForDialog(this, dialog)
        }

        fun updateNotification(view: View) {
            //UpdateChecker.checkForNotification(this, dialog)
        }
    }

//    var sendHttps = {
//        //anko库 轻量级同步实现
//        doAsync {
//            //val url = URL("https://localhost:8443/alipay.json")
//            //var connection: HttpsURLConnection = url.openConnection() as HttpsURLConnection//as 强制类型转换
//            val url = URL("http://localhost:8080/alipay.json")
//            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection//as 强制类型转换
//            //获取服务器返回的流
//            val ins = connection.inputStream
//            //转换成字符串
//            val bos = ByteArrayOutputStream()
//            var buffer = ByteArray(1024)
//            var len = 0
//            len = ins.read()
//            while (len!= -1) {
//                bos.write(buffer,0 , len)
//                len = ins.read(buffer)
//            }
//
//            val result = bos.toString()
//            Log.d("test1126", result)
//        }
//    }

//    inner class aaa : HttpProgressOnNextListener<DownLoadInfo>() {
//        override fun onNext(t: DownLoadInfo) {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        }
//
//        override fun onComplete() {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        }
//
//        override fun onProgress(readLength: Long, countLength: Long) {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        }
//
//        override fun onError(e: Throwable) {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        }
//
//    }
}
