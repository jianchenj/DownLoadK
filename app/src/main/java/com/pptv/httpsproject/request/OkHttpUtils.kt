package com.pptv.httpsproject.request

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit

class OkHttpUtils {
    companion object {
        var okHttpClient: OkHttpClient? = null
        var retrofit: Retrofit? = null
        fun getRetrofit() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl("https://raw.githubusercontent.com/wj576038874/mvp-rxjava-retrofit-okhttp/master/")
                    .client(getOkHttpClient1())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            }
        }

        private fun getOkHttpClient1(): OkHttpClient {
            if (okHttpClient == null) {
                okHttpClient = OkHttpClient.Builder().connectTimeout(
                    15 * 1000
                    , TimeUnit.MILLISECONDS
                ).readTimeout(15 * 1000, TimeUnit.MILLISECONDS).build()
            }
            return okHttpClient!!
        }
    }
}