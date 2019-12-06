package com.pptv.httpsproject.exception

import android.util.Log
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function

import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class Test : Function<Observable<out Throwable>, ObservableSource<*>> {

    //    retry次数
    private var count = 3
    //    延迟
    private var delay: Long = 3000
    //    叠加延迟
    private val increaseDelay: Long = 3000

    constructor() {}

    constructor(count: Int, delay: Long) {
        this.count = count
        this.delay = delay
    }

    constructor(count: Int, delay: Long, increaseDelay: Long) {
        this.count = count
        this.delay = delay
        this.increaseDelay = increaseDelay
    }

    override fun apply(input: Observable<out Throwable>): Observable<*> {
        return input.zipWith(Observable.range(1, count + 1), object : BiFunction<Throwable, Int, Wrapper> {
            override fun apply(throwable: Throwable, integer: Int?): Wrapper {
                return Wrapper(throwable, integer!!)
            }
        }).flatMap<*>(Function<Wrapper, ObservableSource<*>> { wrapper ->
            if ((wrapper.throwable is ConnectException
                        || wrapper.throwable is SocketTimeoutException
                        || wrapper.throwable is TimeoutException) && wrapper.index < count + 1
            ) { //如果超出重试次数也抛出错误，否则默认是会进入onCompleted
                Log.e("tag", "retry---->" + wrapper.index)
                return@Function Observable.timer(delay + (wrapper.index - 1) * increaseDelay, TimeUnit.MILLISECONDS)
            }
            Observable.error<*>(wrapper.throwable)
        })
    }

    private inner class Wrapper internal constructor(private val throwable: Throwable, private val index: Int)

}