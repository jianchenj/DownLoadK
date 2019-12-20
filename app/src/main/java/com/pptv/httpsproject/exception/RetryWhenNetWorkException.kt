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

class RetryWhenNetWorkException @JvmOverloads constructor(
    var count: Int = 3,//重试次数
    var delay: Long = 3000, //延迟
    var increaseDelay: Long = 3000//叠加延迟
) : Function<Observable<out Throwable>, ObservableSource<Any>> {//out 代表 ? extends Throwable 规定上边界 ,in 代表 ? Super Throwable 规定下边界


    override fun apply(input: Observable<out Throwable>): ObservableSource<Any> {
        // zipWith 打包
        return input.zipWith(Observable.range(1, count + 1),
            BiFunction<Throwable, Int, Wrapper> { t1, t2 -> Wrapper(index = t2, throwable = t1) }
        ).flatMap(Function<Wrapper, ObservableSource<*>> { wrapper ->
            //如果超出重试次数也抛出错误，否则默认是会进入onCompleted
            if ((wrapper.throwable is ConnectException
                        || wrapper.throwable is SocketTimeoutException
                        || wrapper.throwable is TimeoutException) && wrapper.index < count + 1
            ) {
                Log.e("tag", "retry---->" + wrapper.index)
                return@Function Observable.timer(delay + (wrapper.index - 1) * increaseDelay, TimeUnit.MILLISECONDS)
            }
            return@Function Observable.error<Any>(wrapper.throwable)
        })
    }

    /**
     *  kotlin中所有的内部类默认为静态的，这样很好的减少了内存泄漏问题。
     *  如果需要在内部类引用外部类的对象，可以使用inner声明内部类，
     *  使内部类变为非静态的，通过this@外部类名，指向外部类
     * */
    private inner class Wrapper internal constructor(var index: Int, var throwable: Throwable)
}