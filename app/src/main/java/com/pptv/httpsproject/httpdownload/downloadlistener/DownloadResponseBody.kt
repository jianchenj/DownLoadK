package com.pptv.httpsproject.httpdownload.downloadlistener

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*

/*
*  自定义进度的body
* */
class DownloadResponseBody constructor(
    private var responseBody: ResponseBody?,
    private var downLoadProgressListener: DownLoadProgressListener?
) : ResponseBody() {

    private var bufferedSource: BufferedSource? = null

    override fun contentLength(): Long {
        if (responseBody == null) {
            return 0
        }
        return responseBody!!.contentLength()
    }

    override fun contentType(): MediaType? {
        if (responseBody == null) {
            return null
        }
        return responseBody!!.contentType()
    }

    override fun source(): BufferedSource {
        if (responseBody == null) return Buffer()
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody!!.source()))
        }
        return bufferedSource!!
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalBytesRead: Long = 0//下载长度

            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)//本次read的长度
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0//bytesRead = -1说明read完毕
                downLoadProgressListener?.update(
                    totalBytesRead, if (responseBody == null) 0 else {
                        responseBody!!.contentLength()//总长度
                    }, bytesRead == -1L
                )
                return bytesRead
            }
        }
    }
}