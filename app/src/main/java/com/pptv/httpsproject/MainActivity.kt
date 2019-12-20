package com.pptv.httpsproject

import android.app.Activity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : Activity() {

    //懒加载初始化View
    val mBtnBuy by lazy {
        btn_buy
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //点击监听
        mBtnBuy.setOnClickListener {
            //子线程
            Log.d("test1126", "setOnClickListener")
            sendHttps()
        }

        Thread {
            Log.d("tag", "wwww")
        }.start()

        thread(true) {

        }
//        var www = WeakReference(aaa())
//        var baaa  = ProgressDownSubscriber(DownLoadInfo(), { www })
    }

    var sendHttps = {
        //anko库 轻量级同步实现
        doAsync {
            //val url = URL("https://localhost:8443/alipay.json")
            //var connection: HttpsURLConnection = url.openConnection() as HttpsURLConnection//as 强制类型转换
            val url = URL("http://localhost:8080/alipay.json")
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection//as 强制类型转换
            //获取服务器返回的流
            val ins = connection.inputStream
            //转换成字符串
            val bos = ByteArrayOutputStream()
            var buffer = ByteArray(1024)
            var len = 0
            len = ins.read()
            while (len!= -1) {
                bos.write(buffer,0 , len)
                len = ins.read(buffer)
            }

            val result = bos.toString()
            Log.d("test1126", result)
        }
    }

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
