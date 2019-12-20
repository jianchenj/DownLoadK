package com.pptv.httpsproject.httpdownload.downloadlistener

//下载进度回调
interface DownLoadProgressListener {

    /**
     *  下载进度
     *  @param read 下载进度
     *  @param count 总长度
     *
     * */
    fun update(read: Long, count: Long, done: Boolean)
}