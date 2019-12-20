package com.pptv.httpsproject.http

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

/*
*  Http下载
* */
interface HttpService {
    //断点续传下载接口，start表示下载起始点
    @Streaming//大文件加入这个判断，防止下载过程中写入到内存中
    @Headers("Content-type:application/octet-stream")//下载文件后缀，参照 https://tool.oschina.net/commons, 这里表示二进制流，不知道下载文件类型
    @GET
    fun downLoad(@Header("RANGE") start: String, @Url url: String): Observable<ResponseBody>
}