package com.pptv.httpsproject.httpdownload

import android.annotation.SuppressLint
import com.pptv.httpsproject.httpdownload.downloadlistener.DownLoadProgressListener
import com.pptv.httpsproject.httpdownload.listener.HttpProgressOnNextListener
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import java.lang.ref.WeakReference

/**
 * Http请求开始时展示ProgressDialog,
 * 结束的时候自动消失
 */
class ProgressDownSubscriber<T> constructor(
    var downLoadInfo: DownLoadInfo,
    var mHttpProgressOnNextListener: () -> WeakReference<out HttpProgressOnNextListener<T>>//接收一个lambda表达式,这个lambda表达式返回一个弱引用
) : Observer<T>, DownLoadProgressListener {

    private var mDisposable: Disposable? = null

    fun unSubscribe() {
        mDisposable?.dispose()
    }

    override fun onComplete() {
        mDisposable?.dispose()
        mHttpProgressOnNextListener.invoke().get()!!.onComplete()
        downLoadInfo.downLoadState = DownLoadState.FINISH
    }

    override fun onSubscribe(d: Disposable) {
        mDisposable = d
    }

    override fun onNext(t: T) {
        //invoke() 代表执行 lanmbda表达式内的代码
        //这里  mHttpProgressOnNextListener.invoke().get() 肯定不为null的原因是以 它是以lambada表达式传进来的，该lambda指定了返回类型，不会null
        mHttpProgressOnNextListener.invoke().get()!!.onNext(t)
    }

    override fun onError(e: Throwable) {
        mDisposable?.dispose()
        HttpDownLoadManager.stopDownLoad(downLoadInfo)
        mHttpProgressOnNextListener.invoke().get()!!.onError(e)
        downLoadInfo.downLoadState = DownLoadState.ERROR
    }

    @SuppressLint("CheckResult")
    override fun update(read: Long, count: Long, done: Boolean) {
        var readToSet = read
        if (downLoadInfo.countLength > count) {
            readToSet = downLoadInfo.countLength - count + read
        } else {
            downLoadInfo.countLength = count
        }
        downLoadInfo.readLength = readToSet
        //将事件发送到主线程更新UI
        Observable.just(readToSet).observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer {
                //暂停停止状态不发送progress回调
                if (downLoadInfo.downLoadState == DownLoadState.PAUSE
                    || downLoadInfo.downLoadState == DownLoadState.STOP
                ) {
                    return@Consumer
                }
                downLoadInfo.downLoadState = DownLoadState.DOWNING
                mHttpProgressOnNextListener.invoke().get()!!.onProgress(it, downLoadInfo.countLength)
            })
    }
}