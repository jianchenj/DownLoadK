package com.pptv.httpsproject.exception

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function

class RetryWhenNetWorkException @JvmOverloads constructor(
    var count: Int = 3,//重试次数
    var delay: Long = 3000, //延迟
    var increaseDelay: Long = 3000//叠加延迟
) : Function<Observable<out Throwable>, ObservableSource<Any>> {//out 代表 ? extends Throwable 规定上边界 ,in 代表 ? Super Throwable 规定下边界


    override fun apply(input: Observable<out Throwable>): ObservableSource<Any> {
        // zipWith
        return input.zipWith(Observable.range(1, count + 1), object : BiFunction<Throwable, Int, Wrapper>)
    }

    /**
     *  kotlin中所有的内部类默认为静态的，这样很好的减少了内存泄漏问题。
     *  如果需要在内部类引用外部类的对象，可以使用inner声明内部类，
     *  使内部类变为非静态的，通过this@外部类名，指向外部类
     * */
    private inner class Wrapper {

    }
}