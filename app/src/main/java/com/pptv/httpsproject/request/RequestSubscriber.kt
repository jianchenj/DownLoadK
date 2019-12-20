package com.pptv.httpsproject.request

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.net.ConnectException
import java.net.SocketTimeoutException

abstract class RequestSubscriber<T> : Observer<T> {

    /**
     * 定义一个请求成功的抽象方法，子类必须实现并在实现中处理服务器返回的数据
     * @param t 服务器返回数据
     */
    protected abstract fun onSuccess(t: T)

    protected abstract fun onFailure(msg: String)

    override fun onSubscribe(d: Disposable) {
    }

    override fun onNext(t: T) {
        onSuccess(t)
    }

    override fun onError(e: Throwable) {
        val msg: String = when (e) {
            is SocketTimeoutException -> "请求超时,，请稍后重试！"
            is ConnectException -> "请求超时。请稍后重试！"
            else -> "请求未能成功，请稍后重试！"
        }
        onFailure(msg)
    }

    override fun onComplete() {

    }
}