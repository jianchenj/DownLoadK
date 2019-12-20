package com.pptv.httpsproject.httpdownload

import android.util.Log
import com.pptv.httpsproject.exception.HttpTimeException
import com.pptv.httpsproject.exception.RetryWhenNetWorkException
import com.pptv.httpsproject.http.HttpService
import com.pptv.httpsproject.httpdownload.downloadlistener.DownloadInterceptor
import com.pptv.httpsproject.httpdownload.listener.HttpProgressOnNextListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.lang.ref.WeakReference
import java.nio.channels.FileChannel
import java.util.concurrent.TimeUnit

object HttpDownLoadManager {
    //记录下载数据, by lazy 用于val ，lateinit var 用于var
    private val downloadInfos: HashSet<DownLoadInfo> by lazy {
        HashSet<DownLoadInfo>()
    }

    //lateinit修饰符只能修饰不可空类型，并且不允许修饰基础类型（四类八种基础类型int， double，boolean等）
    //在调用lateinit修饰的变量时，如果变量还没有初始化，则会抛出未初始化异常，报错
    //回调队列,
    private lateinit var subMap: HashMap<String, ProgressDownSubscriber<*>>

    private val mProgressListenerHashMap: HashMap<String, HttpProgressOnNextListener<DownLoadInfo>> by lazy {
        HashMap<String, HttpProgressOnNextListener<DownLoadInfo>>()
    }

    init {
        subMap = HashMap()
    }

    /**
     * 继续下载
     */
    fun continueDownload(downLoadInfo: DownLoadInfo) {
        val httpProgressOnNextListener = mProgressListenerHashMap.get(downLoadInfo.url)
        httpProgressOnNextListener?.let {
            startDownLoad(downLoadInfo, it)
        }
    }

    fun startDownLoad(info: DownLoadInfo, httpProgressOnNextListener: HttpProgressOnNextListener<DownLoadInfo>) {
        //已经在下载了
        if (info.downLoadState == DownLoadState.DOWNING) {
            httpProgressOnNextListener.onError(Exception("正在下载中"))
            return
        }
        //添加回调处理类
        val subscriber: ProgressDownSubscriber<DownLoadInfo> = ProgressDownSubscriber(
            downLoadInfo = info,
            mHttpProgressOnNextListener = { WeakReference(httpProgressOnNextListener) })
        //url 有误
        if (info.url == null) {
            httpProgressOnNextListener.onError(Exception("url 为空"))
            return
        }
        //记录进度监听器
        mProgressListenerHashMap.put(info.url!!, httpProgressOnNextListener)
        //记录回调subscriber
        subMap.put(info.url!!, subscriber)
        //记录状态为 START
        info.downLoadState = DownLoadState.START
        val httpService: HttpService
        //HttpService 有误
        if (info.service == null) {
            httpProgressOnNextListener.onError(Exception("service 为空"))
            return
        }
        //获取service，多次请求公用一个service
        if (downloadInfos.contains(info)) {
            httpService = info.service!!
        } else { //初始化 HttpService
            val interceptor = DownloadInterceptor(subscriber)
            //手动创建一个 OkHttpClient
            val builder = OkHttpClient.Builder()
            builder.addInterceptor(interceptor)
            builder.connectTimeout(info.DEFAULT_TIMEOUT, TimeUnit.SECONDS)

            val retrofit = Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(info.baseUrl)
                .build()
            httpService = retrofit.create(HttpService::class.java)//javaClass, ::class 是KClass
            info.service = httpService
            downloadInfos.add(info)
        }
        //得到rx对象-上一次下载的位置开始下载
        httpService.downLoad("bytes=${info.readLength}-", info.url!!)
            //指定线程
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .retryWhen(RetryWhenNetWorkException())
            .map(object : Function<ResponseBody, DownLoadInfo> {
                override fun apply(t: ResponseBody): DownLoadInfo {
                    try {
                        writeCache(responseBody = t, file = File(info.savPath), downLoadInfo = info)
                    } catch (e: IOException) {
                        throw HttpTimeException(e.message + "")
                    }
                    return info
                }
            })
            .observeOn(AndroidSchedulers.mainThread())//回调线程
            .subscribe(subscriber)//数据回调
    }

    @Throws(IOException::class)
    fun writeCache(responseBody: ResponseBody, file: File, downLoadInfo: DownLoadInfo) {
        if (!file.parentFile.exists()) {
            if (!file.parentFile.mkdir()) Log.e("www", "文件创建失败")
        }
        var allLength = 0L
        if (downLoadInfo.countLength == 0L) {
            allLength = responseBody.contentLength()
        } else {
            allLength = downLoadInfo.countLength
        }
        val randomAccessFile = RandomAccessFile(file, "rwd")
        val channelOut = randomAccessFile.channel
        val mappedByteBuffer = channelOut.map(
            FileChannel.MapMode.READ_WRITE,
            downLoadInfo.readLength, allLength - downLoadInfo.readLength
        )
        val buffer = ByteArray(1024 * 8)
        var len: Int
        do {
            len = responseBody.byteStream().read(buffer)
            mappedByteBuffer.put(buffer, 0, len)
        } while (len != -1)
        responseBody.byteStream().close()
        channelOut.close()
        randomAccessFile.close()
    }

    fun stopDownLoad(info: DownLoadInfo) {
        info.downLoadState = DownLoadState.STOP
        if (subMap.containsKey(info.url)) {
            subMap.get(info.url)!!.unSubscribe()
            subMap.remove(info.url)
        }
    }

    fun pause(info: DownLoadInfo) {
        info.downLoadState = DownLoadState.PAUSE
        if (subMap.containsKey(info.url)) {
            subMap.get(info.url)!!.unSubscribe()
            subMap.remove(info.url)
        }
    }

    fun stopAll() {
        for (downloadInfo in downloadInfos) {
            pause(downloadInfo)
        }
        subMap.clear()
        downloadInfos.clear()
    }


}