package com.pptv.httpsproject.httpdownload.listener

/*
*  下载状态生命周期回调
* */
abstract class HttpProgressOnNextListener<T> {
    /**
     *  成功后回调方法
     *
     *  @param t 下载成功后的文件信息
     */
    abstract fun onNext(t: T)

    /**
     *  下载完成
     */
    abstract fun onComplete()

    /**
     * 下载进度
     *
     * @param readLength  当前下载进度
     * @param countLength 文件总长度
     */
    abstract fun onProgress(readLength: Long, countLength: Long)

    /**
     * 错误回调
     */
    abstract fun onError(e: Throwable)
}