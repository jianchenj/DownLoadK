package com.pptv.httpsproject.request

import com.pptv.httpsproject.entity.VersionInfo
import com.pptv.httpsproject.utils.Constants
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET(Constants.APK_DOWNLOAD_URL)
    fun checkVersion(): Observable<Response<VersionInfo>>
}