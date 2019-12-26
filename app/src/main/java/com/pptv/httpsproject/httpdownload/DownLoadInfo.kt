package com.pptv.httpsproject.httpdownload

import com.pptv.httpsproject.http.HttpService

class DownLoadInfo{

    constructor() {

    }

    constructor(url: String?) {

    }
    //下载存储位置
    var savPath: String? = null

    //下载url
    var url: String? = null
        set(url) {
            field = url
            baseUrl = getBaseUrl(url)
        }

    //基础url
    var baseUrl: String? = null

    //下载总长度
    var countLength: Long = 0

    //下载长度
    var readLength: Long = 0

    //下载唯一的HttpService
    var service: HttpService? = null

    //超时时限
    var DEFAULT_TIMEOUT : Long = 6

    //下载状态
    var downLoadState: DownLoadState? = null


    private fun getBaseUrl(url: String?): String {
        var url1 = url
        var head: String? = null
        url?.let {
            var index = url.indexOf("://")
            if (index != -1) {
                head = url.substring(0, index + 3)
                url1 = url.substring(index + 3)
            }
            index = url.indexOf("/")
            if (index != -1) {
                url1 = url.substring(0, index + 1)
            }
        }
        return head + url1
    }
}