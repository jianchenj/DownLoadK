package com.pptv.httpsproject.utils

import android.app.Dialog
import android.content.Context
import android.widget.Toast
import com.pptv.httpsproject.entity.VersionInfo
import com.pptv.httpsproject.request.ApiService
import com.pptv.httpsproject.request.OkHttpUtils
import com.pptv.httpsproject.request.RequestSubscriber
import io.reactivex.Observable
import io.reactivex.Observer
import retrofit2.Response

class UpdateChecker {
    companion object {
        fun checkForDialog(context: Context, dialog: Dialog) {
            dialog.show()
            val observable: Observable<Response<VersionInfo>> =
                OkHttpUtils.getRetrofit().create(ApiService::class.java).checkVersion()
            val observer: Observer<Response<VersionInfo>> = object : RequestSubscriber<Response<VersionInfo>>() {
                override fun onSuccess(t: Response<VersionInfo>) {
                    dialog.dismiss()
                    if (t.isSuccessful) {
                        t.body()?.let {
                            if (it.versionCode > AppUtils.getVersionCode(context)) {
                                UpdateDialog.show(
                                    context,
                                    it.updateMessage,
                                    it.downloadUrl
                                )
                            } else {
                                Toast.makeText(
                                    context,
                                    "已经是最新版本",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }

                override fun onFailure(msg: String) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            }

        }
    }
}