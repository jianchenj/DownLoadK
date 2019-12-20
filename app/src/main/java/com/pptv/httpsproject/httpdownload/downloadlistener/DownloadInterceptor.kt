package com.pptv.httpsproject.httpdownload.downloadlistener

import okhttp3.Interceptor
import okhttp3.Response

/*
*  用于监听下载进度的interceptor
*
* */
class DownloadInterceptor constructor(private var listener: DownLoadProgressListener) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse: Response = chain.proceed(chain.request())
        return originalResponse.newBuilder().body(
            DownloadResponseBody(
                originalResponse.body(),
                listener
            )
        ).build()
    }
}