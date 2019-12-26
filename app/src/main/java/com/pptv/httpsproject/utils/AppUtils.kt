package com.pptv.httpsproject.utils

import android.content.Context

class AppUtils {
    companion object {
        fun getVersionName(context: Context): String {
            return context.packageManager.getPackageInfo(context.packageName, 0).versionName
        }

        //https://zhuanlan.zhihu.com/p/32890550  Deprecated的使用
        @Deprecated("versionCode is Deprecated", ReplaceWith("getVersionName(context: Context)"), DeprecationLevel.WARNING)
        fun getVersionCode(context: Context): Int {
            return context.packageManager.getPackageInfo(context.packageName, 0).versionCode
        }
    }
}